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

            setName();
            server.broadcastMessage("SERVER: " + playerName + " has entered the chat");

        } catch (IOException e) {

            System.err.println(e.getMessage());
            logger.log(Level.WARNING, "ERROR - Unable to initialize I/O streams " + e.getMessage());
            close();

        }
    }

    @Override
    public void run() {

        while(playerSocket.isConnected()) { //while (!quit) {

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

        out.write("Welcome to The Hangman my dear friend. How can I assist you? \n");
        sendMessage("Please input your username: ");
        playerName = in.readLine();

    }


    public String getPlayerName() {

        return playerName;

    }
}
