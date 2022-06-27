package org.academiadecodigo.cunnilinux.hangman;

import org.academiadecodigo.cunnilinux.hangman.network.NewServer;

public class NewMain {

    public static void main(String[] args) {

        try {

            int port = args.length > 0 ? Integer.parseInt(args[0]) : NewServer.DEFAULT_PORT;

            NewServer server = new NewServer(port);
            server.start();

        } catch (NumberFormatException e) {

            System.err.println("Usage: Server [PORT]");
            System.exit(1);

        }
    }
}
