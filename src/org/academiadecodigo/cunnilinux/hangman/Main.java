package org.academiadecodigo.cunnilinux.hangman;

import org.academiadecodigo.cunnilinux.hangman.network.Server;

public class Main {
    public static void main(String[] args) {

        try {

            int port = args.length > 0 ? Integer.parseInt(args[0]) : Server.DEFAULT_PORT;

            Server server = new Server(port);
            server.start();

        } catch (NumberFormatException e) {

            System.err.println("Usage: Server [PORT]");
            System.exit(1);

        }
    }
}
