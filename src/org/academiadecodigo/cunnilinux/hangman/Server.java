package org.academiadecodigo.cunnilinux.hangman;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static final int DEFAULT_PORT = 9000;
    public static final int MAX_PLAYERS = 3;

    private final ExecutorService fixedPool;
    private ServerSocket serverSocket = null;
    private Player player;
    private CopyOnWriteArrayList<Player> players;
    private int portNumber;


    public Server(int portNumber) {
        players = new CopyOnWriteArrayList<>();
        fixedPool = Executors.newFixedThreadPool(MAX_PLAYERS);
        this.portNumber = portNumber;
    }


    public void init() {


        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("server listening on port " + portNumber);
            logger.log(Level.INFO, "server bind to " + getAddress());

            while (players.size() < MAX_PLAYERS) {

                Socket playerSocket = serverSocket.accept();
                player = new Player(playerSocket);
                System.out.println("Connection Established");
                Player player = new Player(playerSocket);
                players.add(player);
                fixedPool.submit(player);


            }

            System.out.println("All 3 players in");

        } catch (IOException e) {

            logger.log(Level.SEVERE, "could not bind to port " + portNumber);
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }

    }

    public void start() {

        System.out.println("START");


        //    fixedPool.submit(player);


        while (true) {
            for (Player player : players) {

                try {
                    player.sendMessage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // broadcast("hello");
            }
        }

    }
        private synchronized void broadcast (String message) throws IOException {
            for (Player player : players) {

                player.receiveMessage(message);
            }

        }


        private String getAddress () {

            if (serverSocket == null) {
                return null;
            }

            return serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort();
        }


    }