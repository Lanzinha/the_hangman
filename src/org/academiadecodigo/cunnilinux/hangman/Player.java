package org.academiadecodigo.cunnilinux.hangman;

import main.java.org.academiadecodigo.bootcamp.scanners.string.HangmanStringInputScanner;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player implements Runnable {
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    private String playerName;
    private Socket playerSocket;
    private Hangman hangman;
    private String randomWord;
    private ChooseWords chooseWords;
    private BufferedWriter out;
    private BufferedReader in;
    private Server server;
    private boolean quit;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";
    private Prompt prompt;
    private PrintStream printStream;
    private String teste = "PalavraSecreta";


    public Player(Socket playerSocket, Server server) {

        this.playerSocket = playerSocket;
        this.server = server;
        this.hangman = new Hangman();

        try {

            // Networking
            out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

            // Prompt
            printStream = new PrintStream(playerSocket.getOutputStream());
            prompt = new Prompt(playerSocket.getInputStream(), printStream);

            mainMenu();

        } catch (IOException e) {

            System.err.println(e.getMessage());
            logger.log(Level.WARNING, "ERROR - Unable to initialize I/O streams " + e.getMessage());
            close();

        }

    }

    @Override
    public synchronized void run() {

        //play
        // escolhe palavra e desenha o nro de ltras com __
        // desenha hangman
        // loop players
        // input Letra player guess
        // verify letra certa
        // chama hangman
        // break se ganhador ou se forcado total
        //gameover

        setName();
        server.broadcastMessage(this, "SERVER: " + playerName + " has entered the chat");

        chooseWords = new ChooseWords();
        randomWord = chooseWords.words[(int) (Math.random() * chooseWords.words.length)].toUpperCase();
        System.err.println(randomWord);
        //String hint = getHint();

        //boolean[] verifyCorrectLetters = new boolean[randomWord.length()];

        char[] charArrWord = randomWord.toCharArray();
        char[] charArrHiddenWord = randomWord.toCharArray();
        Arrays.fill(charArrHiddenWord, '*');

        System.out.print(charArrHiddenWord);

        /*char[] letters = new char[randomWord.length() * 2];
        for (int letter = 0; letter < letters.length; letter++) {
            // gerar string com espaço entre as letras para melhor visualização
        }
*/
        server.broadcastMessage(hangman.draw());

        char charPlayerGuess;
        while (playerSocket.isConnected()) { //while (!quit) {

            server.broadcastMessage("\n" + String.valueOf(charArrHiddenWord) + "\n");

            charPlayerGuess = getPlayerGuess();
            if (checkAlreadyGuessed(charArrHiddenWord, charPlayerGuess)) {

                continue;

            }

            if (checkGuess(charArrWord, charPlayerGuess)) {
                Boolean foundFlag = false;
                // atualiza HW
                for (int i = 0; i < charArrWord.length; i++) {

                    if (charPlayerGuess == charArrWord[i]) {

                        charArrHiddenWord[i] = charPlayerGuess;
                        foundFlag = true;

                    }
                }

                if(foundFlag) {

                    sendMessage("Correct guess: " + charPlayerGuess);

                }

            } else {

                sendMessage("Wrong guess: " + charPlayerGuess);
                hangman.next();

            }

            server.broadcastMessage(hangman.draw());

            if (checkGameOver(charArrHiddenWord)) {

                break;

            }
        }
    }

    private boolean checkGameOver(char[] charArrHiddenWord) {

        // checkGameOver
        // break se ganhador ou se forcado total
        //gameover
        if (hangman.checkGameOver()) {

            sendMessage("You lose");
            return true;

        }

        if (checkAllWordsGuess(charArrHiddenWord)){

            sendMessage("You win");
            return true;

        }

        return false;

    }

    private boolean checkAllWordsGuess(char[] charArrHiddenWord) {

        for (int i = 0; i < charArrHiddenWord.length; i++) {

            if(charArrHiddenWord[i] == '*') {

                return false;

            }

        }
        return true;
    }


    private boolean checkGuess(char[] charArrHiddenWord, char charPlayerGuess) {

        return checkArrayGuess(charArrHiddenWord, charPlayerGuess);

    }

    private boolean checkArrayGuess(char[] charArr, char charPlayerGuess) {

        for (int j = 0; j < charArr.length; j++) {

            if (charPlayerGuess == charArr[j]) {

                return true;

            }
        }

        return false;
    }

    private boolean checkAlreadyGuessed(char[] charArrHiddenWord, char charPlayerGuess) {

        return checkArrayGuess(charArrHiddenWord, charPlayerGuess);

    }

    public char getPlayerGuess() {

        // input Letra player guess
        StringInputScanner inGuess = new StringInputScanner();
        inGuess.setMessage("Please input your guess: ");
        String playerGuess = prompt.getUserInput(inGuess);

        // logica para aceitar apenas um caracter e somente [A-Z]
        // HangmanInput
        return playerGuess.toUpperCase().charAt(0);

    }


    public String readMessage() throws IOException {

        String line = in.readLine();

        if (line.equals("/quit")) {

            quit = true;


        }

        return playerName + ": " + line;
    }


    public void sendMessage(String line) {

        try {

            out.write(line);
            out.newLine();
            out.flush();

        } catch (IOException e) {

            System.err.println(e.getMessage());
            logger.log(Level.WARNING, "ERROR - Unable to send message " + e.getMessage());

        }
    }

    public void sendMessage(Player player, String line) {

        try {

            out.write(player.getPlayerName() + ": " + line);
            out.newLine();
            out.flush();

        } catch (IOException e) {

            System.err.println(e.getMessage());
            logger.log(Level.WARNING, "ERROR - Unable to send message " + e.getMessage());

        }
    }

    private void close() {

        try {

            if (in != null) {

                in.close();

            }
            if (out != null) {

                out.close();

            }
            if (playerSocket != null) {

                logger.log(Level.INFO, "closing client socket for " + getAddress());
                playerSocket.close();

            }

        } catch (IOException e) {

            logger.log(Level.INFO, e.getMessage());

        }
    }

    public String getAddress() {

        return playerSocket.getInetAddress().getHostAddress() + ":" + playerSocket.getLocalPort();

    }

    private void setName() {

        //StringInputScanner nameInput = new StringInputScanner();
        //nameInput.setMessage("Please input your username: ");
        //playerName = prompt.getUserInput(nameInput);
        //Thread.currentThread().setName(playerName);

        sendMessage("Please input your username: ");
        try {

            playerName = in.readLine();
            if (!(playerName == null)) {
                Thread.sleep(200);
                sendMessage("      Type 'play' to start the game");
            }
        } catch (IOException e) {

            System.err.println("ERROR -  " + e.getMessage());
            logger.log(Level.WARNING, "ERROR - Unable to close the socket" + e.getMessage());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public String getPlayerName() {

        return playerName;
    }

    public void mainMenu() throws IOException {

        sendMessage(ANSI_CYAN + "\n" +
                " .----------------.  .----------------.  .-----------------. .----------------.  .----------------.  .----------------.  .-----------------.\n" +
                "| .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |\n" +
                "| |  ____  ____  | || |      __      | || | ____  _____  | || |    ______    | || | ____    ____ | || |      __      | || | ____  _____  | |\n" +
                "| | |_   ||   _| | || |     /  \\     | || ||_   \\|_   _| | || |  .' ___  |   | || ||_   \\  /   _|| || |     /  \\     | || ||_   \\|_   _| | |\n" +
                "| |   | |__| |   | || |    / /\\ \\    | || |  |   \\ | |   | || | / .'   \\_|   | || |  |   \\/   |  | || |    / /\\ \\    | || |  |   \\ | |   | |\n" +
                "| |   |  __  |   | || |   / ____ \\   | || |  | |\\ \\| |   | || | | |    ____  | || |  | |\\  /| |  | || |   / ____ \\   | || |  | |\\ \\| |   | |\n" +
                "| |  _| |  | |_  | || | _/ /    \\ \\_ | || | _| |_\\   |_  | || | \\ `.___]  _| | || | _| |_\\/_| |_ | || | _/ /    \\ \\_ | || | _| |_\\   |_  | |\n" +
                "| | |____||____| | || ||____|  |____|| || ||_____|\\____| | || |  `._____.'   | || ||_____||_____|| || ||____|  |____|| || ||_____|\\____| | |\n" +
                "| |              | || |              | || |              | || |              | || |              | || |              | || |              | |\n" +
                "| '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |\n" +
                " '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------' \n\n\n");

        try {
            Thread.sleep(1500);

            sendMessage(ANSI_RESET + ANSI_CYAN + "                     The legend game which you play on papers, now u can play it with your friends on our server, for free!\n\n");
            Thread.sleep(100);
            sendMessage("                     To play, the Rules are:\n\n");
            Thread.sleep(100);
            sendMessage(ANSI_RESET + ANSI_GREEN + "                     1: If you know a letter or the word, go ahead and try to guess.\n\n");
            Thread.sleep(100);
            sendMessage("                     2: Each player will have 5 seconds to guess per round.\n\n");
            Thread.sleep(100);
            sendMessage("                     3: When u fail to guess the letter or word, the hangman starts to take form. \n\n");
            Thread.sleep(100);
            sendMessage("                     4: When the hangman is fully formed, it´s a tie and a new game is started..\n\n");
            Thread.sleep(100);
            sendMessage("                     5: The player with more words completed, wins the game.\n\n");
            Thread.sleep(100);
            sendMessage(ANSI_RESET + ANSI_RED + "                     To quit the game, write /quit\n\n");
            Thread.sleep(100);
            sendMessage(ANSI_RESET + ANSI_YELLOW + "                     GO AHEAD & HAVE SOME FUN WITH THIS AMAZING GAME!!!\n\n");
            Thread.sleep(100);

        } catch (InterruptedException e) {

            logger.log(Level.WARNING, e.getMessage());

        }
    }
}
