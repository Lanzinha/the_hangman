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
    public final int ROOM_SIZE = 2;
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

        logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.GREEN_BACKGROUND,
                ConsoleColor.MAGENTA_BOLD,
                "Room #" + roomNumber + ": Waiting on game to start..."));

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

        logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.GREEN_BACKGROUND,
                ConsoleColor.MAGENTA_BOLD,
                "Room #" + roomNumber + ": Waiting on players to join the room..."));

        while (this.players.size() < ROOM_SIZE && !gameStarted) {

            HangmanTime.sleep(1000);

        }

    }

    @Override
    public void run() {

        start();

    }

    public synchronized void addPlayer(Socket socketPlayer, Room room) {

        NewPlayer player = new NewPlayer(socketPlayer, room, players.size() + 1);
        this.players.add(player);
        this.playerPool.submit(player);

    }

    public synchronized void removePlayer(NewPlayer player) {

        logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.GREEN_BACKGROUND,
                ConsoleColor.MAGENTA_BOLD,
                "Room #" + roomNumber + ": Removing " + player.bracketPlayerName() + " from the room..."));
        players.remove(player);

        if (players.size() != 0) {

            players.stream()
                    .map(NewPlayer::isAdmin)
                    .forEach(admin -> System.out.println("ROOM #" + roomNumber + ": " + (admin ? "Admin" : "Regular")));

        } else {

            System.out.println("Empty room!");

        }
    }

    private void close() {

        playerPool.shutdownNow();
        System.exit(1);

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

    public void setGameStarted(boolean gameStarted) {

        this.gameStarted = gameStarted;

    }

    public int getRoomNumber() {

        return roomNumber;

    }
}
