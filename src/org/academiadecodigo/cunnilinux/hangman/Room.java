package org.academiadecodigo.cunnilinux.hangman;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringSetInputScanner;

import java.util.HashSet;
import java.util.Set;

public class Room {

    Prompt prompt = new Prompt(System.in, System.out);

    // options that you want to be presented are supplied in an array of strings
    String[] options = {"Animals", "Fruits", "mcs"};



    public Room() {
        // create a menu with those options and set the message
        MenuInputScanner promptMenu = new MenuInputScanner(options);
        promptMenu.setMessage("choose a category3");
        // show the menu to the user and get the selected answer
        int answerIndex = prompt.getUserInput(promptMenu);


        System.out.println("User wants to " + options[answerIndex - 1]);
    }

    public static void main(String[] args) {
        //Room room = new Room();

        Prompt prompt = new Prompt(System.in,System.out);
        Set<String> options = new HashSet<>();
        options.add("yes");
        options.add("no");
        StringSetInputScanner question3 = new StringSetInputScanner(options);

        System.out.println("User " + (prompt.getUserInput(question3).equals("yes") ? "agrees." : "does not agree."));
    }


}
