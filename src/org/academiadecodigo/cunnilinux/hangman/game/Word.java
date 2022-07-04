package org.academiadecodigo.cunnilinux.hangman.game;

public class Word {

    String chosenWord;
    String hint;
    char[] chosenWordLetters;
    char[] currentLetters;
    char[] wrongLetters;
    int wrongLettersCount = 0;

    public Word() {

        WordList wordList = new WordList();
        chosenWord = wordList.getRandomWord();
        hint = wordList.getHint(chosenWord);
        chosenWord = chosenWord.toUpperCase();

        chosenWordLetters = chosenWord.toCharArray();
        currentLetters = new char[chosenWordLetters.length];



    }

    public void revealChosenWord() {

        System.out.println("The word is " + chosenWord);

    }
}
