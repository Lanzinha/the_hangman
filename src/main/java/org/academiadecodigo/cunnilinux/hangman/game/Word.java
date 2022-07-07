package org.academiadecodigo.cunnilinux.hangman.game;

import java.util.Arrays;

public class Word {

    String chosenWord;
    String hint;

    char[] chosenWordLetters;
    char[] correctLetters;
    WordList wordList;

    char[] wrongLetters;
    int wrongLettersCount = 0;

    public Word() {

        wordList = new WordList();
        chosenWord = wordList.getRandomWord();
        hint = wordList.getHint(chosenWord);
        chosenWord = chosenWord.toUpperCase();

        initLetters();

    }

    private void initLetters() {

        chosenWordLetters = chosenWord.toCharArray();
        correctLetters = new char[chosenWordLetters.length];
        Arrays.fill(correctLetters, '*');

    }

    public String getChosenWord() {

        return chosenWord;

    }

    public String getCorrectLetters() {

        return new String(correctLetters);

    }

    public boolean checkGameOver() {

        return Arrays.equals(chosenWordLetters, correctLetters);

    }

    // return true if the input letter is in the word
    public boolean compare(String strPlayerGuess) {

        boolean correctLetter = false;

        for (int i = 0; i < chosenWordLetters.length; i++) {

            if (strPlayerGuess.charAt(0) == chosenWordLetters[i]) {

                correctLetters[i] = strPlayerGuess.charAt(0);
                correctLetter = true;

            }
        }

        return correctLetter;

    }
}
