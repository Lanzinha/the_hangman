package org.academiadecodigo.cunnilinux.hangman;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static final int DEFAULT_PORT = 8080;

    private ServerSocket serverSocket = null;
    private Socket playerSocket;
    private Player player;
    private BufferedReader inputBufferedReader;
    private BufferedWriter outputBufferedWriter;


    public void listen(int port) {

        ExecutorService playersPool = Executors.newFixedThreadPool(2);
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("server listening on port " + port);

            while (true) {
                playerSocket = serverSocket.accept();
                player = new Player(playerSocket);
                inputBufferedReader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                outputBufferedWriter = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

                logger.log(Level.INFO, "server bind to " + getAddress());

                playersPool.submit(player);

                System.out.println(player.getName());

                // System.out.println(player.getAddress());

                String line = inputBufferedReader.readLine();
                outputBufferedWriter.write(line);
                System.out.println(line);

            }
        } catch (IOException e) {

            logger.log(Level.SEVERE, "could not bind to port " + port);
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
    }


    private String getAddress() {

        if (serverSocket == null) {
            return null;
        }

        return serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort();
    }


}


package org.academiadecodigo.cunnilinux;

        import jdk.nashorn.internal.ir.WhileNode;

        import java.io.*;
        import java.net.ServerSocket;
        import java.net.Socket;
        import java.util.LinkedList;
        import java.util.Vector;
        import java.util.concurrent.CopyOnWriteArrayList;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;

public class ChatServer {

    private final ExecutorService fixedPool;
    private CopyOnWriteArrayList<ChatClient> clientList;
    private static int portNumber;
    ServerSocket serverSocket;


    public ChatServer(int portNumber) {
        clientList = new CopyOnWriteArrayList<>();
        fixedPool = Executors.newFixedThreadPool(3);
        ChatServer.portNumber = portNumber;
        start();


    }


    public static void main(String[] args) {
        new ChatServer(9000);

    }


    public void start() {

        ServerSocket serverSocket = null;
        try {

            serverSocket = new ServerSocket(portNumber);
            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection Established");
                ChatClient chatClient = new ChatClient(clientSocket, this);
                clientList.add(chatClient);
                fixedPool.submit(chatClient);
                broadCast("hello");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    /*public void clientLeave(Socket clientSocket) {
        broadCast(clientSocket + "left the chat!!!", clientSocket);
        this.clientList.remove(clientSocket);
    }
*/
    private synchronized void broadCast(String message) {
        for (ChatClient chatClient : clientList) {
            chatClient.receiveMessage(message);
        }

    }
}












