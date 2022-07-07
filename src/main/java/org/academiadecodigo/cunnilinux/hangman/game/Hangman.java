package org.academiadecodigo.cunnilinux.hangman.game;

import org.academiadecodigo.cunnilinux.hangman.ui.ConsoleColor;

import java.util.HashMap;
import java.util.Map;

public class Hangman {
    private static volatile int lives = 6;
    private Map<Integer, String> hangmanMap;
    private String imgHangmanImage = " _____\n" + " |/\n" + " |\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n";

    public Hangman() {

        hangmanMap = buildHangmanMap();

    }


    //take a life and update the hangman image

    public synchronized void next() {

        switch (--lives) {
            case 0:
                imgHangmanImage = " _____\n" + " |/  |\n" + " |   0\n" + " |  -O-\n" + " |   \" \n" + " |\n" + " |\n" + "========\n";
                break;

            case 1:
                imgHangmanImage = " _____\n" + " |/  |\n" + " |   0\n" + " |  -O-\n" + " |\n" + " |\n" + " |\n" + "========\n";
                break;

            case 2:
                imgHangmanImage = " _____\n" + " |/  |\n" + " |   0\n" + " |  -O\n" + " |\n" + " |\n" + " |\n" + "========\n";
                break;

            case 3:
                imgHangmanImage = " _____\n" + " |/  |\n" + " |   0\n" + " |   O\n" + " |\n" + " |\n" + " |\n" + "========\n";
                break;

            case 4:
                imgHangmanImage = " _____\n" + " |/  |\n" + " |   0\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n";
                break;

            case 5:
                imgHangmanImage = " _____\n" + " |/  |\n" + " |\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n";
                break;
        }
    }
    public synchronized String draw() {

        return ConsoleColor.RED + imgHangmanImage + ConsoleColor.RESET;

    }

    private Map<Integer, String> buildHangmanMap() {

        Map<Integer, String> map = new HashMap<>();

        map.put(0, " _____\n" + " |/  |\n" + " |   0\n" + " |  -O-\n" + " |   \" \n" + " |\n" + " |\n" + "========\n");
        map.put(1, " _____\n" + " |/  |\n" + " |   0\n" + " |  -O-\n" + " |\n" + " |\n" + " |\n" + "========\n");
        map.put(2, " _____\n" + " |/  |\n" + " |   0\n" + " |  -O\n" + " |\n" + " |\n" + " |\n" + "========\n");
        map.put(3, " _____\n" + " |/  |\n" + " |   0\n" + " |   O\n" + " |\n" + " |\n" + " |\n" + "========\n");
        map.put(4, " _____\n" + " |/  |\n" + " |   0\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n");
        map.put(5, " _____\n" + " |/  |\n" + " |\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n");
        map.put(6, " _____\n" + " |/\n" + " |\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n");

        return map;

    }

    public synchronized boolean checkGameOver() {

        return lives == 0;

    }
}
