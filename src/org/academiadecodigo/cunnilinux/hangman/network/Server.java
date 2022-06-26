package org.academiadecodigo.cunnilinux.hangman.network;

import org.academiadecodigo.cunnilinux.hangman.Player;

import java.net.ServerSocket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class Server {

    private static final Logger logger = Logger.getLogger(org.academiadecodigo.cunnilinux.hangman.Server.class.getName());
    public static final int DEFAULT_PORT = 9000;
    public static final int MAX_PLAYERS = 2;

    private int portNumber;
    private ServerSocket serverSocket;
    private CopyOnWriteArrayList<Player> rooms;
    private ExecutorService roomPool;

    private CopyOnWriteArrayList<Player> players;
    private ExecutorService playerPool;

    public Server(int portNumber) {


    }
}
