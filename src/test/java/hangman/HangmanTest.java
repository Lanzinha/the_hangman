package hangman;

import org.academiadecodigo.cunnilinux.hangman.game.Hangman;

public class HangmanTest {

    public boolean test() {

        Hangman hangman = new Hangman();

        System.out.println(hangman.draw());

        return true;

    }
}
