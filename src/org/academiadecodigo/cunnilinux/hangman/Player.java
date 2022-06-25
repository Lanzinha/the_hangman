package org.academiadecodigo.cunnilinux.hangman;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player implements Runnable {
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    private String playerName;
    private Socket playerSocket;

    private BufferedWriter out;
    private BufferedReader in;
    private Server server;
    private boolean quit;

//    private Prompt prompt;
//
//    private StringInputScanner stringInputScanner;


    public Player(Socket playerSocket, Server server) {

        this.playerSocket = playerSocket;
        this.server = server;
        try {

            out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

//            prompt = new Prompt(playerSocket.getInputStream(), playerSocket.getOutputStream());
//            StringInputScanner inOut = new StringInputScanner();
//            inOut.setMessage("What is your name?");
            mainMenu();
            setName();
            server.broadcastMessage(this, "SERVER: " + playerName + " has entered the chat");

        } catch (IOException e) {

            System.err.println(e.getMessage());
            logger.log(Level.WARNING, "ERROR - Unable to initialize I/O streams " + e.getMessage());
            close();

        }
    }

    @Override
    public synchronized void run() {

        while (playerSocket.isConnected()) { //while (!quit) {

            try {

                server.broadcastMessage(this, readMessage());


            } catch (IOException e) {

                System.err.println(e.getMessage());
                logger.log(Level.WARNING, e.getMessage());
                close();
                break;

            }
        }

        //close();

    }

    public String readMessage() throws IOException {

        String line = in.readLine();

        if (line.equals("/quit")) {

            quit = true;

        }

        return playerName + ": " + line;
    }

    public void sendMessage(String line) throws IOException {

        out.write(line);
        out.newLine();
        out.flush();

    }

    private void close() {

        try {

            logger.log(Level.INFO, "closing client socket for " + getAddress());
            in.close();
            out.close();
            playerSocket.close();

        } catch (IOException e) {

            logger.log(Level.INFO, e.getMessage());

        }
    }

    public String getAddress() {

        return playerSocket.getInetAddress().getHostAddress() + ":" + playerSocket.getLocalPort();

    }

    private void setName() throws IOException {


        sendMessage("Please input your username: ");
        playerName = in.readLine();

    }


    public String getPlayerName() {

        return playerName;
    }

    public void mainMenu() throws IOException {
        out.write("\n" +
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
                " '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------' \n");

        try {
            Thread.sleep(1500);


            out.write("                     The legend game which you play on papers, now u can play it with your friends on our server, for free!\n");
            Thread.sleep(100);
            out.write("                     To play, the Rules are:\n");
            Thread.sleep(100);
            out.write("                     1: If you know a letter, u go ahead and have to try to guess the word.\n");
            Thread.sleep(100);
            out.write("                     2: When u fail to guess the letter or word, the hangman starts to take form.\n");
            Thread.sleep(100);
            out.write("                     3: When the hangman is fully formed, it´s a tie and a new game is started.\n");
            Thread.sleep(100);
            out.write("                     5: The player with more words completed, wins the game.\n");
            Thread.sleep(100);
            out.write("                     6: The player with more words completed, wins the game.\n");
            Thread.sleep(100);


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
