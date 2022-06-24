package org.academiadecodigo.cunnilinux.hangman;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static final int DEFAULT_PORT = 8080;

    private ServerSocket serverSocket = null;
    private Socket playerSocket;
    private Player playerDispatcher;
    private BufferedReader inputBufferedReader;
    private BufferedWriter outputBufferedWriter;


    public void listen(int port) {

        ExecutorService playersPool = Executors.newFixedThreadPool(3);
        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                playerSocket = serverSocket.accept();
                //playerDispatcher = new Player(playerSocket);
                inputBufferedReader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                outputBufferedWriter = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

                logger.log(Level.INFO, "server bind to " + getAddress());

                //playersPool.submit(playerDispatcher);


                String line = inputBufferedReader.readLine();
                outputBufferedWriter.write(line);
                System.out.println(line);

            }
        } catch (IOException e) {

            logger.log(Level.SEVERE, "could not bind to port " + port);
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
    }



    private String getAddress() {

        if (serverSocket == null) {
            return null;
        }

        return serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort();
    }

    public class PlayerConnection implements Runnable{


        Server webServer;
        Player playerDispatcher;


        @Override
        public void run() {

        }
    }





}
