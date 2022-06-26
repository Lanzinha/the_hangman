package org.academiadecodigo.cunnilinux.hangman;

public class Hangman {
    private static int lives = 6;
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
        lives--;
        switch (lives) {
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
        return image;
    }

    public void checkGameOver()
    {
        if (lives == 0)
            //Game.gameOver();
            System.out.println("GAME OVER");
    }

}