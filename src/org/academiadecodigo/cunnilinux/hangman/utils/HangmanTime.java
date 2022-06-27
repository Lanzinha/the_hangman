package org.academiadecodigo.cunnilinux.hangman.utils;

import org.academiadecodigo.cunnilinux.hangman.game.Player;

import java.util.logging.Logger;
import java.util.logging.Level;

public class HangmanTime {

    private static final Logger logger = Logger.getLogger(Player.class.getName());
    public static void sleep(int delay) {

        try {

            Thread.sleep(delay);

        } catch (InterruptedException e) {

            System.err.println("ERROR -  " + e.getMessage());
            logger.log(Level.WARNING, "ERROR - Thread sleep failure" + e.getMessage());

        }
    }
}
