package org.academiadecodigo.cunnilinux.hangman.game;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.cunnilinux.hangman.utils.ConsoleColor;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewPlayer implements Runnable {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    public static final int ANSWER_DELAY = 10;
    private String playerName;
    private final Socket playerSocket;
    private Room room;
    private boolean gameStarted;
    private boolean ready;

    public NewPlayer(Socket playerSocket, Room room) {

        this.playerSocket = playerSocket;
        this.room = room;
        this.gameStarted = false;

    }

    @Override
    public void run() {

        try {

            Prompt prompt = new Prompt(this.playerSocket.getInputStream(),
                    new PrintStream(this.playerSocket.getOutputStream());

        } catch (IOException e) {

            logger.log(Level.SEVERE, ConsoleColor.color(ConsoleColor.RED, "SERVER: Could not bind to port " + portNumber + e.getMessage()));
            System.err.println(ConsoleColor.color(ConsoleColor.RED, e.getMessage()));

            logger.log(Level.SEVERE, "ERROR - Unable to initialize I/O streams " + e.getMessage());
            close();

        }

    }

    private void awaitGameStart() {



    }

    public void setRoom(Room room) {

        this.room = room;

    }

    public boolean isGameStarted() {

        return gameStarted;

    }

    public void setGameStarted(boolean gameStarted) {

        this.gameStarted = gameStarted;

    }

    private void close() {

        try {

            /*if (in != null) {

                in.close();

            }
            if (out != null) {

                out.close();

            }*/
            if (playerSocket != null) {

                logger.log(Level.INFO, "closing client socket for " + getAddress());
                playerSocket.close();

            }

        } catch (IOException e) {

            logger.log(Level.INFO, e.getMessage());

        }
    }
}
