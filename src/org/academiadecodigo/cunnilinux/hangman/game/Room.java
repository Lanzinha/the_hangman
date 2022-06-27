package org.academiadecodigo.cunnilinux.hangman.game;

import org.academiadecodigo.cunnilinux.hangman.network.NewServer;
import org.academiadecodigo.cunnilinux.hangman.utils.HangmanTime;

import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Room implements Runnable {

    private static final Logger logger = Logger.getLogger(NewServer.class.getName());

    public static final int ROOM_SIZE = 1;

    private CopyOnWriteArrayList<NewPlayer> players;
    private ExecutorService playerPool;
    private boolean gameStarted;

    public Room() {

        this.players = new CopyOnWriteArrayList<>();
        this.playerPool = Executors.newFixedThreadPool(ROOM_SIZE);
        this.gameStarted = false;

    }

    public void start() {

        awaitPlayers();
        awaitGameStart();

        while (gameStarted) {


        }
    }

    private void awaitGameStart() {

        while (!gameStarted) {

            HangmanTime.sleep(100);

        }

        startGame();

    }

    private void startGame() {

        this.gameStarted = true;
        this.players.forEach(player -> player.setGameStarted(true));

    }

    private void awaitPlayers() {

        while (this.players.size() < ROOM_SIZE && !gameStarted) {

            HangmanTime.sleep(100);

        }

    }

    @Override
    public void run() {

        start();

    }

    public CopyOnWriteArrayList<NewPlayer> getPlayers() {

       return players;

    }

    public int getMaxRoomSize() {

        return ROOM_SIZE;

    }

    public boolean isGameStarted() {

        return gameStarted;

    }

    public synchronized void addPlayer(Socket socketPlayer, Room room) {

        NewPlayer player = new NewPlayer(socketPlayer, room);
        this.players.add(player);
        this.playerPool.submit(player);

    }
}
