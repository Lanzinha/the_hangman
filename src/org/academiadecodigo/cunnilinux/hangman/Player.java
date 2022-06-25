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

    public Player(Socket playerSocket, Server server) {

        this.playerSocket = playerSocket;
        this.server = server;

        try {

            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

            setName();
            server.broadcastMessage("SERVER: " + playerName + " has entered the chat");

        } catch (IOException e) {

            System.err.println(e.getMessage());
            logger.log(Level.WARNING, "ERROR - Unable to initialize I/O streams " + e.getMessage());

        }
    }

    @Override
    public void run() {

        try {

            while (!quit) {

                server.broadcastMessage(receiveMessage());

            }

            stop();

        } catch (IOException e) {

            System.err.println(e.getMessage());
            logger.log(Level.WARNING, e.getMessage());

        }
    }

    public String receiveMessage() throws IOException {

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

    private void stop() {

        try {

            logger.log(Level.INFO, "closing client socket for " + getAddress());
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
}
