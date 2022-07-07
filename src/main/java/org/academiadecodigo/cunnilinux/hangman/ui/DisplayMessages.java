package org.academiadecodigo.cunnilinux.hangman.ui;

import static org.academiadecodigo.cunnilinux.hangman.ui.AsciiArt.LOGO;

public class DisplayMessages {

    public static final int LINE_LENGTH = 80;

    public static final String CLEAR_SCREEN = new String(new char[100]).replace("\0", "\n");
    //public static final String CLEAR_SCREEN = "\033[H\033[2J";
    //public static final String resetColorASCII = "\u001B[0m";

    public static String logo() {

        return ConsoleColor.coloredMessage(ConsoleColor.GREEN_BOLD, LOGO);

    }

    public static String header() {

        return CLEAR_SCREEN + logo();

    }
}




