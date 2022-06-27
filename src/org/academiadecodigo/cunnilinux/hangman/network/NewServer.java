package org.academiadecodigo.cunnilinux.hangman.network;

import org.academiadecodigo.cunnilinux.hangman.game.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewServer {

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static final int DEFAULT_PORT = 9000;
    private int portNumber;
    private ServerSocket serverSocket;
    private CopyOnWriteArrayList<Room> rooms;
    private ExecutorService roomPool;

    public NewServer(int portNumber) {

        this.portNumber = portNumber;
        rooms = new CopyOnWriteArrayList<>();
        roomPool = Executors.newCachedThreadPool();
        createRoom();

    }

    public void start() {

        try {

            serverSocket = new ServerSocket(portNumber);
            logger.log(Level.INFO, "server bound to " + getAddress());

            while (!serverSocket.isClosed()) {

                System.out.println("Waiting for clients connections...");
                serve(serverSocket.accept());

            }
        } catch (IOException e) {

            logger.log(Level.SEVERE, "could not bind to port " + portNumber);
            logger.log(Level.SEVERE, e.getMessage());
            close();

        }
    }

    private void createRoom() {

        Room room = new Room();
        rooms.add(room);
        roomPool.submit(room);

    }

    private void serve(Socket clientSocket) {

        System.out.println("Connection established with " + clientSocket);

        Room lastRoom = rooms.get(rooms.size() - 1);

        if (lastRoom.getPlayers().size() >= lastRoom.getMaxRoomSize() || lastRoom.isGameStarted()) {

            createRoom();

        }

        lastRoom.addPlayer(clientSocket, lastRoom);

    }

    private String getAddress() {

        if (serverSocket == null) {

            return null;

        }

        return serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort();

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
