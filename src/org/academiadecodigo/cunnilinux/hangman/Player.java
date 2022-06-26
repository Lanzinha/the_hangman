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
    private String randomWord;
    ChooseWords chooseWords = new ChooseWords();


    public Player(Socket playerSocket, Server server) {

        this.hangman = new Hangman();
        this.playerSocket = playerSocket;
        this.server = server;
        try {

            out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

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
        server.broadcastMessage(this, "SERVER: " + playerName + " has entered the chat");

        while (playerSocket.isConnected()) { //while (!quit) {


            //play
            // escolhe palavra e desenha o nro de ltras com __
            // desenha hangman
            // loop players
            // input Letra player guess
            // verify letra certa
            // chama hangman
            // break se ganhador ou se forcado total
            //gameover
            randomWord = chooseWords.words[(int) (Math.random() * chooseWords.words.length)];
            String hint = Hint();
            char[] letters = new char[randomWord.length() * 2];
            for (int letter = 0; letter < letters.length; letter++) {

            }
            server.broadcastMessage(String.valueOf(letters));

            //server.broadcastMessage(drawHangman());

        }

        //close();

    }

    public String Hint() {
        String hint;
        if (randomWord.equals(chooseWords.words[0])) {
            return hint = chooseWords.hints[0];

        }
        if (randomWord.equals(chooseWords.words[1])) {
            return hint = chooseWords.hints[1];

        }
        if (randomWord.equals(chooseWords.words[2])) {
            return hint = chooseWords.hints[2];

        }
        if (randomWord.equals(chooseWords.words[3])) {
            return hint = chooseWords.hints[1];

        }
        if (randomWord.equals(chooseWords.words[4])) {
            return hint = chooseWords.hints[3];

        }
        if (randomWord.equals(chooseWords.words[5])) {
            return hint = chooseWords.hints[3];

        }
        if (randomWord.equals(chooseWords.words[6])) {
            return hint = chooseWords.hints[1];

        }
        if (randomWord.equals(chooseWords.words[7])) {
            return hint = chooseWords.hints[5];

        }
        if (randomWord.equals(chooseWords.words[8])) {
            return hint = chooseWords.hints[5];

        }
        if (randomWord.equals(chooseWords.words[9])) {
            return hint = chooseWords.hints[37];

        }
        if (randomWord.equals(chooseWords.words[10])) {
            return hint = chooseWords.hints[10];

        }
        if (randomWord.equals(chooseWords.words[11])) {
            return hint = chooseWords.hints[6];
        }
        if (randomWord.equals(chooseWords.words[12])) {
            return hint = chooseWords.hints[38];
        }
        return hint = chooseWords.hints[38];
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
                out.write("      Type 'play' to start the game");
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

    private String drawHangman() {

        hangman.next();
        return hangman.draw();

    }

    public void mainMenu() throws IOException {

        out.write(ANSI_CYAN + "\n" +
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

            out.write(ANSI_RESET + ANSI_CYAN + "                     The legend game which you play on papers, now u can play it with your friends on our server, for free!\n\n");
            Thread.sleep(100);
            out.write("                     To play, the Rules are:\n\n");
            Thread.sleep(100);
            out.write(ANSI_RESET + ANSI_GREEN + "                     1: If you know a letter or the word, go ahead and try to guess.\n\n");
            Thread.sleep(100);
            out.write("                     2: Each player will have 5 seconds to guess per round.\n\n");
            Thread.sleep(100);
            out.write("                     3: When u fail to guess the letter or word, the hangman starts to take form. \n\n");
            Thread.sleep(100);
            out.write("                     4: When the hangman is fully formed, it´s a tie and a new game is started..\n\n");
            Thread.sleep(100);
            out.write("                     5: The player with more words completed, wins the game.\n\n");
            Thread.sleep(100);
            out.write(ANSI_RESET + ANSI_RED + "                     To quit the game, write /quit\n\n");
            Thread.sleep(100);
            out.write(ANSI_RESET + ANSI_YELLOW + "                     GO AHEAD & HAVE SOME FUN WITH THIS AMAZING GAME!!!\n\n");
            Thread.sleep(100);


        } catch (InterruptedException e) {

            logger.log(Level.WARNING, e.getMessage());

        }
    }
}
