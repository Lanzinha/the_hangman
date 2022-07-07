package org.academiadecodigo.cunnilinux.hangman.game;

import java.util.*;

public class WordList {

    HashMap<String, String> wordList = new HashMap<String, String>();

    public WordList() {

        wordList.put("programming", "HINT: Your word is related with programming.");
        wordList.put("abstraction", "HINT: Your word is related with programming.");
        wordList.put("polymorphism", "HINT: Your word is related with programming.");
        wordList.put("apple", "HINT: Your word is related with computers brands.");
        wordList.put("windows", "HINT: Your word is related with software brands.");
        wordList.put("ibm", "HINT: Your word is related with computers brands.");
        wordList.put("lenovo", "HINT: Your word is related with computer brands.");
        wordList.put("java", "HINT: Your word is related with programming languages.");
        wordList.put("python", "HINT: Your word is related with programming languages.");
        wordList.put("javascript", "HINT: Your word is related with programming languages.");
        wordList.put("sql", "HINT: Your word is related with programming languages.");

    }

    public String getRandomWord() {

        //Set<String> keySet = wordList.keySet();
        List<String> keyList = new ArrayList<>(wordList.keySet());

        int randIdx = new Random().nextInt(keyList.size());

        return keyList.get(randIdx);
    }

    public int getSize() {

        return wordList.size();

    }

    public String getHint(String word) {

        return wordList.get(word);

    }
}
