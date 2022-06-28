package org.academiadecodigo.cunnilinux.hangman.game;

import org.academiadecodigo.cunnilinux.hangman.ui.ConsoleColor;
import org.academiadecodigo.cunnilinux.hangman.utils.HangmanTime;

import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room implements Runnable {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    public final int ROOM_SIZE = 1;
    private int roomNumber;
    private CopyOnWriteArrayList<NewPlayer> players;
    private ExecutorService playerPool;
    private boolean gameStarted;

    public Room(int roomNumber) {

        this.roomNumber = roomNumber;
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

        logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.GREEN_BACKGROUND, ConsoleColor.MAGENTA_BOLD, "Room #" + roomNumber + ": Waiting on game to start..."));

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

        logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.GREEN_BACKGROUND, ConsoleColor.MAGENTA_BOLD, "Room #" + roomNumber + ": Waiting on players to join the room..."));

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

        int playerNumber = players.size() + 1;
        NewPlayer player = new NewPlayer(socketPlayer, room, playerNumber);
        this.players.add(player);
        this.playerPool.submit(player);

    }

    private void close() {

        playerPool.shutdownNow();
        System.exit(1);

    }

    public int getRoomNumber() {

        return roomNumber;

    }
}
