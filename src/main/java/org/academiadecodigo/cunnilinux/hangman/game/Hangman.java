package org.academiadecodigo.cunnilinux.hangman.game;

import org.academiadecodigo.cunnilinux.hangman.ui.ConsoleColor;

public class Hangman {
    private static volatile int lives = 6;
    private String imgHangman = " _____\n" + " |/\n" + " |\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n";

    //take a life and update the hangman image
    public synchronized void next() {

        switch (--lives) {
            case 0:
                imgHangman = " _____\n" + " |/  |\n" + " |   0\n" + " |  -O-\n" + " |   \" \n" + " |\n" + " |\n" + "========\n";
                break;

            case 1:
                imgHangman = " _____\n" + " |/  |\n" + " |   0\n" + " |  -O-\n" + " |\n" + " |\n" + " |\n" + "========\n";
                break;

            case 2:
                imgHangman = " _____\n" + " |/  |\n" + " |   0\n" + " |  -O\n" + " |\n" + " |\n" + " |\n" + "========\n";
                break;

            case 3:
                imgHangman = " _____\n" + " |/  |\n" + " |   0\n" + " |   O\n" + " |\n" + " |\n" + " |\n" + "========\n";
                break;

            case 4:
                imgHangman = " _____\n" + " |/  |\n" + " |   0\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n";
                break;

            case 5:
                imgHangman = " _____\n" + " |/  |\n" + " |\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n";
                break;
        }
    }

    public synchronized String draw() {

        return ConsoleColor.RED + imgHangman + ConsoleColor.RESET;

    }

    public synchronized boolean checkGameOver() {

        return lives == 0;

    }

    public enum HangmanImage {

        ZERO(0, " _____\n" + " |/  |\n" + " |   0\n" + " |  -O-\n" + " |   \" \n" + " |\n" + " |\n" + "========\n"),
        ONE(1, " _____\n" + " |/  |\n" + " |   0\n" + " |  -O-\n" + " |\n" + " |\n" + " |\n" + "========\n"),
        TWO(2, " _____\n" + " |/  |\n" + " |   0\n" + " |  -O\n" + " |\n" + " |\n" + " |\n" + "========\n"),
        THREE(3, " _____\n" + " |/  |\n" + " |   0\n" + " |   O\n" + " |\n" + " |\n" + " |\n" + "========\n"),
        FOUR(4, " _____\n" + " |/  |\n" + " |   0\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n"),
        FIVE(5, " _____\n" + " |/  |\n" + " |\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n"),
        SIX(6, " _____\n" + " |/\n" + " |\n" + " |\n" + " |\n" + " |\n" + " |\n" + "========\n");

        private int lives;
        private String strImage;

        HangmanImage(int lives, String strImage) {

            this.lives = lives;
            this.strImage = strImage;

        }
    }
}
