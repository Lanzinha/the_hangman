package org.academiadecodigo.cunnilinux.hangman;

import java.util.*;

public class ChooseWords {


    HashMap<String, String> words = new HashMap<String, String>();


    public ChooseWords() {
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

        int size = keyList.size();
        int randIdx = new Random().nextInt(size);

        String randomKey = keyList.get(randIdx);
        String randomValue = words.get(randomKey);
        return randomKey;
    }

    public int getSize() {
        return words.size();
    }

    public String getHint(String word) {
        return words.get(word);
    }

}
