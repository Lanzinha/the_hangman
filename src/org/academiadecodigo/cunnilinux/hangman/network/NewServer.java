package org.academiadecodigo.cunnilinux.hangman.network;

import org.academiadecodigo.cunnilinux.hangman.game.Room;
import org.academiadecodigo.cunnilinux.hangman.ui.ConsoleColor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.Level;

public class NewServer {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    public static final int DEFAULT_PORT = 9000;
    private final int portNumber;
    private ServerSocket serverSocket;
    private CopyOnWriteArrayList<Room> rooms;
    private ExecutorService roomPool;

    public NewServer(int portNumber) {

        logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.YELLOW_BACKGROUND, ConsoleColor.GREEN_BOLD, "SERVER: Initializing..."));

        this.portNumber = portNumber;
        rooms = new CopyOnWriteArrayList<>();
        roomPool = Executors.newCachedThreadPool();
        createRoom();

    }

    public void listen() {

        try {

            serverSocket = new ServerSocket(portNumber);
            logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.YELLOW_BACKGROUND, ConsoleColor.GREEN_BOLD, "SERVER: Bound to " + getAddress()));

            while (!serverSocket.isClosed()) {

                logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.YELLOW_BACKGROUND, ConsoleColor.GREEN_BOLD, "SERVER: Waiting on players to connect..."));

                serve(serverSocket.accept());

            }
        } catch (IOException e) {

            logger.log(Level.SEVERE, ConsoleColor.color(ConsoleColor.RED, "SERVER: Could not bind to port " + portNumber + e.getMessage()));
            System.err.println(ConsoleColor.color(ConsoleColor.RED, e.getMessage()));

            close();

        }
    }

    private void createRoom() {

        int roomNumber = rooms.size() + 1;
        Room room = new Room(roomNumber);
        rooms.add(room);
        roomPool.submit(room);

        logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.GREEN_BACKGROUND, ConsoleColor.MAGENTA_BOLD, "Room #" + roomNumber + " has been created"));

    }

    private void serve(Socket clientSocket) {

        Room lastRoom = getLastRoom();
        if (lastRoom.getPlayers().size() >= lastRoom.getMaxRoomSize() || lastRoom.isGameStarted()) {

            createRoom();

        }

        lastRoom.addPlayer(clientSocket, lastRoom);

    }

    private String getAddress() {

        String ipAddress;

        if (serverSocket == null) {

            return null;

        }

        try {

            ipAddress = InetAddress.getLocalHost().getHostAddress();

        } catch (UnknownHostException e) {

            logger.log(Level.SEVERE, ConsoleColor.color(ConsoleColor.RED, "SERVER: Unable to get local address" + e.getMessage()));
            System.err.println(ConsoleColor.color(ConsoleColor.RED, e.getMessage()));

            ipAddress = serverSocket.getInetAddress().getHostAddress();

        }

        return ipAddress + ":" + serverSocket.getLocalPort();

    }

    private Room getLastRoom() {

        return rooms.get(rooms.size() - 1);

    }

    private void close() {

        roomPool.shutdownNow();

        try {

            if (serverSocket != null) {

                serverSocket.close();

            }

        } catch (IOException e) {

            logger.log(Level.SEVERE, ConsoleColor.color(ConsoleColor.RED, "SERVER: Unable to close the socket" + e.getMessage()));
            System.err.println(ConsoleColor.color(ConsoleColor.RED, e.getMessage()));

        }

        System.exit(1);

    }
}
