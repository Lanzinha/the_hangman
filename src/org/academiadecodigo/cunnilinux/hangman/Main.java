package org.academiadecodigo.cunnilinux.hangman;

public class Main {
    public static void main(String[] args) {

        try {

            int port = args.length > 0 ? Integer.parseInt(args[0]) : Server.DEFAULT_PORT;

            Server server = new Server();
            server.listen(port);

        } catch (NumberFormatException e) {

            System.err.println("Usage: WebServer [PORT]");
            System.exit(1);

        }


    }
}
