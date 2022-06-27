package org.academiadecodigo.cunnilinux.hangman.network;

import org.academiadecodigo.cunnilinux.hangman.game.Player;

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
    public static final int MAX_PLAYERS = 2;
    private final ExecutorService threadPool;
    private ServerSocket serverSocket;
    private final CopyOnWriteArrayList<Player> players;
    private final int portNumber;

    public Server(int portNumber) {

        players = new CopyOnWriteArrayList<>();
        threadPool = Executors.newCachedThreadPool();
        this.portNumber = portNumber;

    }

    public void start() {

        try {

            serverSocket = new ServerSocket(portNumber);
            logger.log(Level.INFO, "server bound to " + getAddress());

            while (!serverSocket.isClosed()) {

                System.out.println("Waiting for clients connections...");
                Socket playerSocket = serverSocket.accept();

                System.out.println("Connection established with " + playerSocket);

                Player player = new Player(playerSocket, this);
                players.add(player);
                threadPool.submit(player);

            }

            //System.out.println("All 3 players in");

        } catch (IOException e) {

            logger.log(Level.SEVERE, "could not bind to port " + portNumber);
            logger.log(Level.SEVERE, e.getMessage());
            close();
            System.exit(1);

        }
    }

    public synchronized void broadcastMessage(Player senderPlayer, String message) {

        for (Player player : players) {

            if (!senderPlayer.getPlayerName().equals(player.getPlayerName())) {

                player.sendMessage("\n" + message + "\n");


            }
        }
    }

    public synchronized void broadcastMessage(String message) {

        for (Player player : players) {

            player.sendMessage("\n" + message + "\n");

        }
    }

    private void removePlayer(Player player) {

        players.remove(player);
        broadcastMessage(player, "SERVER: <" + player.getPlayerName() + "> has left the game!");

    }

    public void addPlayer(Player player) {

        broadcastMessage("SERVER: <" + player.getPlayerName() + "> has joined the game!");

    }

    private String getAddress() {

        if (serverSocket == null) {

            return null;

        }

        return serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort();

    }

    public CopyOnWriteArrayList<Player> getPlayers() {

        return players;

    }

    private void close() {

        try {

            if (serverSocket != null) {

                serverSocket.close();

            }

        } catch (IOException e) {

            System.err.println("ERROR -  " + e.getMessage());
            logger.log(Level.WARNING, "ERROR - Unable to close the socket" + e.getMessage());

        }

        System.exit(1);

    }
}
