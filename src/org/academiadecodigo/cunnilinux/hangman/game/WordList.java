package org.academiadecodigo.cunnilinux.hangman.game;

import java.util.*;

public class Words {

    HashMap<String, String> words = new HashMap<String, String>();

    public Words() {

        words.put("programming", "HINT: Your word is related with programming.");
        words.put("abstraction", "HINT: Your word is related with programming.");
        words.put("polymorphism", "HINT: Your word is related with programming.");
        words.put("apple", "HINT: Your word is related with computers brands.");
        words.put("windows", "HINT: Your word is related with software brands.");
        words.put("ibm", "HINT: Your word is related with computers brands.");
        words.put("lenovo", "HINT: Your word is related with computer brands.");
        words.put("java", "HINT: Your word is related with programming languages.");
        words.put("python", "HINT: Your word is related with programming languages.");
        words.put("javascript", "HINT: Your word is related with programming languages.");
        words.put("sql", "HINT: Your word is related with programming languages.");

    }

    public String getRandomWord() {

        Set<String> keySet = words.keySet();
        List<String> keyList = new ArrayList<>(keySet);

        int randIdx = new Random().nextInt(keyList.size());

        return keyList.get(randIdx);
    }

    public int getSize() {

        return words.size();

    }

    public String getHint(String word) {

        return words.get(word);

    }
}
