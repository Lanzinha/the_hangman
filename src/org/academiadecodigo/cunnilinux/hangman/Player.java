package org.academiadecodigo.cunnilinux.hangman;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player implements Runnable {
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    private String name;
    private Socket playerSocket;

    private PrintWriter out;
    private BufferedReader terminalIn;
    private BufferedReader in;
    //private final Server server;

    public Player(Socket playerSocket) {
        this.playerSocket = playerSocket;
        try {
            terminalIn = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            out = new PrintWriter(playerSocket.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public String getAddress() {
        return playerSocket.getInetAddress().getHostAddress() + ":" + playerSocket.getLocalPort();
    }

    @Override
    public void run() {


        /*try {

            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            while (!this.clientSocket.isClosed()) {
                String message = in.readLine();
                //if (in == null) {
                //server.clientLeave(this.clientSocket);


            } catch(SocketException ex){

                logger.log(Level.INFO, "client disconnected " + getAddress());

            } catch(IOException ex){

                logger.log(Level.WARNING, ex.getMessage());
                close();
            }


        }*/
        /* private void start() throws IOException {
        System.out.println(serverReply.readLine());
        out.println(terminalIn.readLine());
        System.out.println("Start your chat:  ");
        while (true) {
            String message = terminalIn.readLine();
            if (!message.equalsIgnoreCase("Quit")) {

                out.println(message);
                System.out.println(serverReply.readLine());
            } else {
                break;
            }
        }
        clientSocket.close();
    }*/

    }

        public void receiveMessage (String message){

            out.println(message);


        }

        public synchronized void sendMessage () {
            out.println("Welcome my dear friend. How can i assist u?");
            String lanzas = null;
            try {
                out.println("pls write your name");
                lanzas = in.readLine();
                // while (true) {
                String message = in.readLine();
                out.println(lanzas + ": " + message);
                System.out.println(message);

                //}
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        private void close () {

            try {

                logger.log(Level.INFO, "closing client socket for " + getAddress());
                playerSocket.close();

            } catch (IOException e) {

                logger.log(Level.INFO, e.getMessage());
            }

        }


    }


