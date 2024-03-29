package org.academiadecodigo.cunnilinux.hangman.game;

import org.academiadecodigo.bootcamp.scanners.string.HangmanStringInputScanner;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.cunnilinux.hangman.ui.AsciiArt;
import org.academiadecodigo.cunnilinux.hangman.network.Server;
import org.academiadecodigo.cunnilinux.hangman.ui.DisplayMessages;
import org.academiadecodigo.cunnilinux.hangman.utils.Chronometer;
import org.academiadecodigo.cunnilinux.hangman.ui.ConsoleColor;
import org.academiadecodigo.cunnilinux.hangman.utils.HangmanTime;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player implements Runnable {

    public static final int ANSWER_DELAY = 10;
    private static final Logger logger = Logger.getLogger(Player.class.getName());
    private String playerName;
    private final Socket playerSocket;
    private final Hangman hangman;
    private BufferedWriter out;
    private BufferedReader in;
    private final Server server;
    private boolean quit;
    private Prompt prompt;

    public Player(Socket playerSocket, Server server) {

        this.playerSocket = playerSocket;
        this.server = server;
        this.hangman = new Hangman();

        try {

            // Networking
            out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

            // Prompt
            PrintStream printStream = new PrintStream(playerSocket.getOutputStream());
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

        Chronometer chronometer = new Chronometer();

        WordList wordList = new WordList();
        String randomWord = wordList.getRandomWord();
        String hint = wordList.getHint(randomWord);
        randomWord = randomWord.toUpperCase();

        char[] charArrWord = randomWord.toCharArray();
        char[] charArrHiddenWord = randomWord.toCharArray();
        Arrays.fill(charArrHiddenWord, '*');

        System.out.print(randomWord + "\n");

        char charPlayerGuess;
        while (playerSocket.isConnected()) {

            drawStandardScreen(hint, hangman.draw(), charArrHiddenWord);

            chronometer.start();
            charPlayerGuess = getPlayerGuess();
            chronometer.stop();
            if (chronometer.getSeconds() > ANSWER_DELAY) {

                sendMessage("Your time is up dummy! You lost a life!");
                hangman.next();
                HangmanTime.sleep(3000);
                continue;

            }

            if (checkAlreadyGuessed(charArrHiddenWord, charPlayerGuess)) {

                continue;

            }

            if (checkGuess(charArrWord, charPlayerGuess)) {

                boolean foundFlag = false;
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

            HangmanTime.sleep(2000);

            if (checkGameOver(charArrHiddenWord, randomWord)) {

                break;

            }
        }

        close();

    }

    public void drawStandardScreen(String hint, String hangmanImage, char[] charArrHiddenWord) {

        server.broadcastMessage(DisplayMessages.CLEAR_SCREEN);
        server.broadcastMessage(DisplayMessages.logo());
        server.broadcastMessage(hint);
        server.broadcastMessage(hangmanImage);
        server.broadcastMessage(String.valueOf(charArrHiddenWord));
        server.broadcastMessage("List of wrong guesses available in the PREMIUM version..." + "\n");

    }

    private boolean checkGameOver(char[] charArrHiddenWord, String word) {

        if (hangman.checkGameOver()) {

            server.broadcastMessage("The word was: " + word);
            HangmanTime.sleep(3000);

            server.broadcastMessage(DisplayMessages.CLEAR_SCREEN);
            server.broadcastMessage(DisplayMessages.logo());

            sendMessage("You now have a 6k debt, hang in there boy (x");
            sendMessage(AsciiArt.LOOSER);

            return true;

        }

        if (checkAllWordsGuess(charArrHiddenWord)) {

            server.broadcastMessage("The word was: " + word);
            HangmanTime.sleep(3000);

            server.broadcastMessage(DisplayMessages.CLEAR_SCREEN);
            server.broadcastMessage(DisplayMessages.logo());

            sendMessage("YOU WIN!!!");
            sendMessage(AsciiArt.WINNER);

            return true;

        }

        return false;

    }

    private boolean checkAllWordsGuess(char[] charArrHiddenWord) {

        for (char c : charArrHiddenWord) {

            if (c == '*') {

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

        for (char c : charArr) {

            if (charPlayerGuess == c) {

                return true;

            }
        }

        return false;
    }

    public synchronized char getPlayerGuess() {

        HangmanStringInputScanner inputGuess = new HangmanStringInputScanner();
        inputGuess.setMessage("Please input your guess: ");
        inputGuess.setError("\nOnly a single letter is allowed!\n");

        //server.broadcastMessage("Please input your guess: ");
        //sendMessage("Please input your guess: ");

        //wait();
        return prompt.getUserInput(inputGuess).charAt(0);
//        String answer = null;
//        try {
//
//            answer = readMessage().toUpperCase();
//
//        } catch (IOException e) {
//
//            throw new RuntimeException(e);
//
//        }
//
//        server.broadcastMessage(answer);
//
//        //notifyAll();
//
//        return answer.charAt(0);

//        while (isFull()) {
//
//            try {
//
//                wait();
//
//            } catch (InterruptedException e) {
//
//                System.err.println("ERROR: Thread interrupted - " + e.getMessage());
//
//            }
//        }
//
//        queue.offerLast(data);
//
//        notifyAll();

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

            out.newLine();
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

        try {

            if (playerName != null) {

                server.addPlayer(this);
                //server.broadcastMessage("\nSERVER: " + playerName + " has entered the game");
                Thread.sleep(200);
                StringInputScanner inGuess = new StringInputScanner();
                inGuess.setMessage("\n                                      Type 'play' to start your sentence: ");
                String playerInput = prompt.getUserInput(inGuess);
                while ((!playerInput.equals("play"))) {

                    HangmanTime.sleep(1000);
                    sendMessage(ConsoleColor.RED + "\n                                                CAN´T U READ??" + ConsoleColor.RESET + ConsoleColor.CYAN);
                    playerInput = prompt.getUserInput(inGuess);

                }
            }

            //server.broadcastMessage(Colors.YELLOW + "\n                                              LET THE WAR START!\n" + Colors.RESET + Colors.CYAN);
            sendMessage(ConsoleColor.YELLOW + "\n                                              LET THE WAR START!\n" + ConsoleColor.RESET + ConsoleColor.CYAN);
            HangmanTime.sleep(3000);

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

        sendMessage(DisplayMessages.CLEAR_SCREEN + DisplayMessages.logo());

        try {

            Thread.sleep(1800);

            sendMessage(ConsoleColor.CYAN + "                     The legend game which you play on papers, now u can play it with your friends on our server, for free!\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     To play, the Rules are:\n\n");
            Thread.sleep(sleepTime);
            sendMessage(ConsoleColor.GREEN + "                     1: If you know a letter or the word, go ahead and try to guess.\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     2: Each player will have 10 seconds to guess per round.\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     3: When you fail to guess the letter or word, the hangman starts to take form. \n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     4: When the hangman is fully formed, it´s a tie and a new game is started.\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     5: The player with more words completed, wins the game.\n\n");
            Thread.sleep(sleepTime);
            sendMessage(ConsoleColor.RED + "                     To quit the game, write /quit\n\n");
            Thread.sleep(sleepTime);
            sendMessage(ConsoleColor.CYAN + "                     GO AHEAD & HAVE SOME FUN WITH THIS AMAZING GAME!!!\n\n");
            sendMessage("");
            Thread.sleep(sleepTime);

        } catch (InterruptedException e) {

            System.err.println("ERROR -  " + e.getMessage());
            logger.log(Level.WARNING, "ERROR - Thread sleep failure" + e.getMessage());

        }
    }
}
