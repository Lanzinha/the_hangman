package org.academiadecodigo.cunnilinux.hangman.game;

import java.net.Socket;
import java.util.logging.Logger;

public class NewPlayer implements Runnable {

    private static final Logger logger = Logger.getLogger(Player.class.getName());
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
        
        
        
    }

    public void setGameStarted(boolean b) {



    }

    public void setRoom(Room room) {

        this.room = room;

    }
}
