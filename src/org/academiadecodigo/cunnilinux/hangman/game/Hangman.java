package org.academiadecodigo.cunnilinux.hangman.game;

import org.academiadecodigo.cunnilinux.hangman.utils.Colors;

public class Hangman {
    private static volatile int lives = 6;
    private String image = " _____\n" +
            " |/\n" +
            " |\n" +
            " |\n" +
            " |\n" +
            " |\n" +
            " |\n" +
            "========\n";

    //take a life and update the hangman image
    public synchronized void next() {

        switch (--lives) {
            case 0:
                image = " _____\n" +
                        " |/  |\n" +
                        " |   0\n" +
                        " |  -O-\n" +
                        " |   \" \n" +
                        " |\n" +
                        " |\n" +

                        "========\n";
                break;

            case 1:
                image = " _____\n" +
                        " |/  |\n" +
                        " |   0\n" +
                        " |  -O-\n" +
                        " |\n" +
                        " |\n" +
                        " |\n" +
                        "========\n";
                break;

            case 2:
                image = " _____\n" +
                        " |/  |\n" +
                        " |   0\n" +
                        " |  -O\n" +
                        " |\n" +
                        " |\n" +
                        " |\n" +
                        "========\n";
                break;

            case 3:
                image = " _____\n" +
                        " |/  |\n" +
                        " |   0\n" +
                        " |   O\n" +
                        " |\n" +
                        " |\n" +
                        " |\n" +
                        "========\n";
                break;

            case 4:
                image = " _____\n" +
                        " |/  |\n" +
                        " |   0\n" +
                        " |\n" +
                        " |\n" +
                        " |\n" +
                        " |\n" +
                        "========\n";
                break;

            case 5:
                image = " _____\n" +
                        " |/  |\n" +
                        " |\n" +
                        " |\n" +
                        " |\n" +
                        " |\n" +
                        " |\n" +
                        "========\n";
                break;
        }
    }

    public synchronized String draw() {

        return Colors.ANSI_RED + image + Colors.ANSI_RESET;

    }

    public synchronized boolean checkGameOver() {

        return lives == 0;

    }

    public synchronized static int getLives() {

        return lives;

    }
}