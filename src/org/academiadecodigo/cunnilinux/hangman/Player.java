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



package org.academiadecodigo.cunnilinux;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.PrintWriter;
        import java.net.InetAddress;
        import java.net.Socket;


public class ChatClient implements Runnable {
    private Socket clientSocket;
    //private final int portNumber;
    private PrintWriter out;
    private BufferedReader terminalIn;
    private BufferedReader in;
    private final ChatServer server;
    //BufferedReader in;


    public ChatClient(Socket clientSocket, ChatServer server) {

        this.clientSocket = clientSocket;
        this.server = server;
        //receiveMessage();
        openStreams();
        sendMessage();
   /*     try {
            //link();
            start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
*/

    }

    public static void main(String[] args) {
        new ChatClient(new Socket(), new ChatServer(9000));

    }
/*

    private void link() {
        try {
            clientSocket = new Socket(InetAddress.getLocalHost(),);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
*/


    private void openStreams() {
        terminalIn = new BufferedReader(new InputStreamReader(System.in));
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void receiveMessage(String message) {

        out.println(message);


    }

    public synchronized void sendMessage() {
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


    @Override
    public void run() {

        try {
            BufferedReader in = null;
            in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            while (!this.clientSocket.isClosed()) {
                String message = in.readLine();
                //if (in == null) {
                //server.clientLeave(this.clientSocket);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }
}

