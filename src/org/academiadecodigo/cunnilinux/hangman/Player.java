package org.academiadecodigo.cunnilinux.hangman;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player implements Runnable {
    private static final Logger logger = Logger.getLogger(Player.class.getName());

    private String name;
    private Socket playerSocket;

    private BufferedWriter out;
    private BufferedReader terminalIn;
    private BufferedReader in;
    //private final Server server;

    public Player(Socket playerSocket) {

        this.playerSocket = playerSocket;

        try {
            terminalIn = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

        } catch (IOException e) {

            throw new RuntimeException(e);

        }
    }


    public String getAddress() {
        return playerSocket.getInetAddress().getHostAddress() + ":" + playerSocket.getLocalPort();
    }

    @Override
    public void run() {

        while (!this.playerSocket.isClosed()) {

            sendMessage();

        }

           /* } catch(SocketException ex){

                logger.log(Level.INFO, "client disconnected " + getAddress());

            } catch(IOException ex){

                logger.log(Level.WARNING, ex.getMessage());
                close();
            }*/


    }
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


    public void receiveMessage(String message) throws IOException {

        out.write(message);

    }

    public synchronized void sendMessage() {

        String userName = null;

        try {
            out.write("Welcome my dear friend. How can i assist u?");
            out.write("pls write your name");
            userName = in.readLine();
            // while (true) {
            String message = in.readLine();
            out.write(userName + ": " + message);
            //System.out.println(message);

            //}
        } catch (IOException e) {

            throw new RuntimeException(e);

        }
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
