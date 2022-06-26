package org.academiadecodigo.cunnilinux.hangman;

public class ChooseWords {


    public String[] getWords() {
        return words;

    }

    public String[] getHints() {
        return hints;
    }

    String[] words = new String[]{
            "programming",
            "abstraction",
            "polymorphism",
            "encapsulation",
            "streams",
            "threads",
            "functional",
            "networking",
            "web",
            "concurrency",
            "linux",
            "function",

    };
    String[] hints = new String[]{
            "HINT: Your word is related with programming.",
            "HINT: Your word is related with computer hardware.",
            "HINT: You use to work, and have fun.",
    };

}