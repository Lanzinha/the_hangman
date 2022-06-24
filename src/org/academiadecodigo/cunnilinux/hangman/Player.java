package org.academiadecodigo.cunnilinux.hangman;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player implements Runnable {
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    private String name;
    private Socket playerSocket;
    private DataOutputStream out;
    private BufferedReader in;

    public Player(Socket playerSocket) {
        this.playerSocket = playerSocket;
        this.name = "tiago";
    }

    public String getAddress() {
        return playerSocket.getInetAddress().getHostAddress() + ":" + playerSocket.getLocalPort();
    }

    @Override
    public void run() {


        try {

            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            out = new DataOutputStream(playerSocket.getOutputStream());


        } catch (SocketException ex) {

            logger.log(Level.INFO, "client disconnected " + getAddress());

        } catch (IOException ex) {

            logger.log(Level.WARNING, ex.getMessage());
            close();
        }

    }

    public String getName() {
        return name;
    }


    private void reply(String response) throws IOException {
        out.writeBytes(response);
    }

    private void close() {

        try {

            logger.log(Level.INFO, "closing client socket for " + getAddress());
            playerSocket.close();

        } catch (IOException e) {

            logger.log(Level.INFO, e.getMessage());
        }

    }


}
