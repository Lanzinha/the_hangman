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

    public static final String CLEAR_SCREEN = new String(new char[100]).replace("\0", "\n");
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

        setName();


        chooseWords = new ChooseWords();
        randomWord = chooseWords.getRandomWord();
        String hint = chooseWords.getHint(randomWord);
        randomWord = randomWord.toUpperCase();

        char[] charArrWord = randomWord.toCharArray();
        char[] charArrHiddenWord = randomWord.toCharArray();
        Arrays.fill(charArrHiddenWord, '*');

        System.out.print(randomWord + "\n");
        System.out.print(charArrHiddenWord);

        server.broadcastMessage("\n" + String.valueOf(charArrHiddenWord) + "\n");
        server.broadcastMessage(hint + "\n");
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
                for (int i = 0; i < charArrWord.length; i++) {

                    if (charPlayerGuess == charArrWord[i]) {

                        charArrHiddenWord[i] = charPlayerGuess;
                        foundFlag = true;

                    }
                }

                if (foundFlag) {

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

        server.broadcastMessage("\nThe word was: " + randomWord + "\n");

    }

    private boolean checkGameOver(char[] charArrHiddenWord) {

        // checkGameOver
        // break se ganhador ou se forcado total
        // gameover
        if (hangman.checkGameOver()) {

            sendMessage("You lose");
            return true;

        }

        if (checkAllWordsGuess(charArrHiddenWord)) {

            sendMessage("You win");
            sendMessage(ASCII.WINNER);
            return true;

        }

        return false;

    }

    private boolean checkAllWordsGuess(char[] charArrHiddenWord) {

        for (int i = 0; i < charArrHiddenWord.length; i++) {

            if (charArrHiddenWord[i] == '*') {

                return false;

            }

        }
        return true;
    }

    private boolean checkGuess(char[] charArrHiddenWord, char charPlayerGuess) {

        return checkArrayGuess(charArrHiddenWord, charPlayerGuess);

    }

    private boolean checkAlreadyGuessed(char[] charArrHiddenWord, char charPlayerGuess) {

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

    public char getPlayerGuess() {

        HangmanStringInputScanner inputGuess = new HangmanStringInputScanner();
        inputGuess.setMessage("Please input your guess: ");
        inputGuess.setError("\nOnly a single letter is allowed!\n");

        return prompt.getUserInput(inputGuess).charAt(0);

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

        StringInputScanner nameInput = new StringInputScanner();
        nameInput.setMessage("Please input your username: ");
        playerName = prompt.getUserInput(nameInput);

        //sendMessage("Please input your username: ");
        try {

            //playerName = in.readLine();

            if (!(playerName == null)) {
                server.broadcastMessage(this, "SERVER: " + playerName + " has entered the chat");
                Thread.sleep(200);
                StringInputScanner inGuess = new StringInputScanner();
                inGuess.setMessage("\n                                      Type 'play' to start your sentence: ");
                String playerInput = prompt.getUserInput(inGuess);
                while ((!playerInput.equals("play"))) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sendMessage(ANSI_RED + "\n                                                CAN´T U READ??" + ANSI_RESET + ANSI_CYAN);
                    playerInput = prompt.getUserInput(inGuess);

                }

            }
            server.broadcastMessage(ANSI_YELLOW + "                                              LET THE WAR START!" + ANSI_RESET + ANSI_CYAN);

        } catch (InterruptedException e) {

            System.err.println("ERROR -  " + e.getMessage());
            logger.log(Level.WARNING, "ERROR - Thread sleep failure" + e.getMessage());

        }

    }

    public String getPlayerName() {

        return playerName;
    }

    public void mainMenu() throws IOException {

        int sleepTime = 500;

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
            Thread.sleep(1800);

            sendMessage(ANSI_RESET + ANSI_CYAN + "                     The legend game which you play on papers, now u can play it with your friends on our server, for free!\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     To play, the Rules are:\n\n");
            Thread.sleep(sleepTime);
            sendMessage(ANSI_RESET + ANSI_GREEN + "                     1: If you know a letter or the word, go ahead and try to guess.\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     2: Each player will have 5 seconds to guess per round.\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     3: When u fail to guess the letter or word, the hangman starts to take form. \n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     4: When the hangman is fully formed, it´s a tie and a new game is started..\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     5: The player with more words completed, wins the game.\n\n");
            Thread.sleep(sleepTime);
            sendMessage(ANSI_RESET + ANSI_RED + "                     To quit the game, write /quit\n\n");
            Thread.sleep(sleepTime);
            sendMessage(ANSI_RESET + ANSI_CYAN + "                     GO AHEAD & HAVE SOME FUN WITH THIS AMAZING GAME!!!\n\n");
            sendMessage("");
            Thread.sleep(sleepTime);

        } catch (InterruptedException e) {

            logger.log(Level.WARNING, e.getMessage());

        }
    }
}
