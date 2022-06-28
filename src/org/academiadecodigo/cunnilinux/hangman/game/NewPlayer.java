package org.academiadecodigo.cunnilinux.hangman.game;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.cunnilinux.hangman.ui.ConsoleColor;
import org.academiadecodigo.cunnilinux.hangman.ui.DisplayMessages;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewPlayer implements Runnable {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    public static final int ANSWER_DELAY = 10;
    private String playerName;
    private int playerNumber;
    private final Socket playerSocket;
    private Room room;
    private boolean gameStarted;
    private boolean ready;

    public NewPlayer(Socket playerSocket, Room room, int playerNumber) {

        this.playerSocket = playerSocket;
        this.room = room;
        this.playerNumber = playerNumber;
        this.gameStarted = false;

    }

    @Override
    public void run() {

        try {

            Prompt prompt = new Prompt(playerSocket.getInputStream(),
                            new PrintStream(playerSocket.getOutputStream()));

            StringInputScanner logo = new StringInputScanner();
            logo.setMessage(DisplayMessages.CLEAR_SCREEN + DisplayMessages.logo());

            prompt.displayMessage(logo);
            setName(prompt);

            // TODO: Color themes
            //prompt.displayMessage(logo);
            //setColor(prompt);

            // TODO: Ask theme for the ADMIN (first player who joined the room)
            //       Need to get the player number inside the room

            awaitGameStart(prompt);

            while(gameStarted) {




            }

        } catch (IOException e) {

            logger.log(Level.SEVERE, ConsoleColor.color(ConsoleColor.RED, "PLAYER: Unable to initialize I/O streams " + e.getMessage()));
            System.err.println(ConsoleColor.color(ConsoleColor.RED, e.getMessage()));

            close();

        }

    }

    private void setName(Prompt prompt) {

        StringInputScanner name = new StringInputScanner();

        name.setMessage("Please input your username: \n> ");
        playerName = prompt.getUserInput(name).trim();

        logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.WHITE_BACKGROUND_BRIGHT,
                                                  ConsoleColor.RED_BOLD,
                                             "PLAYER #" + playerNumber + " - <" + playerName + ">:" + " Joined the room #" + room.getRoomNumber()));

    }

    private void awaitGameStart(Prompt prompt) {

        this.ready = true;

        while (!this.gameStarted) {



        }

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

                logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.RED_BACKGROUND,
                                                          ConsoleColor.WHITE_BOLD,
                                                     "Room # " + room.getRoomNumber() + " - PLAYER # " + playerNumber + " - <" + playerName + ">: " + "closing client socket for " + getAddress()));
                playerSocket.close();

            }

        } catch (IOException e) {

            logger.log(Level.INFO, e.getMessage());

        }
    }

    public String getAddress() {

        return playerSocket.getInetAddress().getHostAddress() + ":" + playerSocket.getLocalPort();

    }
}
