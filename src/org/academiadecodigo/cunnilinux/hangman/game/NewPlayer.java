package org.academiadecodigo.cunnilinux.hangman.game;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.cunnilinux.hangman.ui.ConsoleColor;
import org.academiadecodigo.cunnilinux.hangman.ui.DisplayMessages;
import org.academiadecodigo.cunnilinux.hangman.utils.HangmanTime;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewPlayer implements Runnable {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    public static final int ANSWER_DELAY = 10;
    private String playerName;
    private int playerNumber;
    private boolean admin;
    private Socket playerSocket;
    private Room room;
    private boolean gameStarted;
    private boolean ready;
    private BufferedReader inputBufferedReader;
    private PrintWriter outputPrintWriter;

    public NewPlayer(Socket playerSocket, Room room, int playerNumber) {

        this.playerSocket = playerSocket;
        this.room = room;
        this.playerNumber = playerNumber;
        this.gameStarted = false;
        this.admin = (playerNumber == 1);

        try {

            outputPrintWriter = new PrintWriter(new OutputStreamWriter(playerSocket.getOutputStream()), true);
            inputBufferedReader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

        } catch (IOException e) {

            logger.log(Level.SEVERE, ConsoleColor.color(ConsoleColor.RED, "PLAYER: Unable to initialize I/O streams " + e.getMessage()));
            System.err.println(ConsoleColor.color(ConsoleColor.RED, e.getMessage()));

            close();

        }
    }

    @Override
    public void run() {

        try {

            Prompt prompt = new Prompt(playerSocket.getInputStream(),
                    new PrintStream(playerSocket.getOutputStream()));

            StringInputScanner logo = new StringInputScanner();
            logo.setMessage(DisplayMessages.header());
            prompt.displayMessage(logo);
            setName(prompt);

            // TODO: Color themes
            //prompt.displayMessage(logo);
            //setColor(prompt);

            awaitGameStart(prompt);

            while (gameStarted) {


            }

        } catch (IOException e) {

            logger.log(Level.SEVERE, ConsoleColor.color(ConsoleColor.RED, "PLAYER: Unable to initialize I/O streams " + e.getMessage()));
            System.err.println(ConsoleColor.color(ConsoleColor.RED, e.getMessage()));

            close();

        }
    }

    private void setName(Prompt prompt) {

        StringInputScanner name = new StringInputScanner();

        name.setMessage("Please input your name: \n\n> ");
        playerName = prompt.getUserInput(name).trim();

        logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.WHITE_BACKGROUND_BRIGHT,
                ConsoleColor.RED_BOLD,
                "PLAYER #" + playerNumber + " - " + bracketPlayerName() + ":" + " Joined the room #" + room.getRoomNumber()));

    }

    /********************
     *
     *  Waiting Methods
     *
     ********************/
    private void awaitGameStart(Prompt prompt) {

        showRules(prompt);
        this.ready = true;

        room.getPlayers().stream()
                .map(NewPlayer::isAdmin)
                .forEach(System.out::println);

        while (!this.gameStarted) {

            if (admin) {

                String[] menuOptions = new String[]{
                        "Start game",
                        "See connected players",
                        "Wait on more players to connect",
                        "Quit game"
                };

                MenuInputScanner menuInputScanner = new MenuInputScanner(menuOptions);
                menuInputScanner.setMessage(DisplayMessages.header() + bracketPlayerName() + " please choose wisely:");

                switch (prompt.getUserInput(menuInputScanner)) {

                    case 1:

                        sendMessage("\nStarting game...\n");

                        if (checkAllReady()) {

                            room.setGameStarted(true);
                            gameStarted = true;

                        } else {

                            showNotReady(prompt);

                        }
                        break;

                    case 2:

                        sendMessage("\nPlayers connected to the room #" + room.getRoomNumber() + "...\n");
                        showPlayers(prompt);
                        break;

                    case 3:

                        sendMessage("\nWaiting for more players... There are currently " + room.getPlayers().size() + "players connected.");
                        break;

                    case 4:

                        sendMessage("\nGoodbye fellow " + bracketPlayerName() + "!\n");
                        close();
                        break;

                    default:
                        break;

                }

                HangmanTime.sleep(1000);

            }
        }
    }

    private void showPlayers(Prompt prompt) {

        String[] menuOptions = new String[]{
                "Back to previous menu"
        };

        MenuInputScanner menuInputScanner = new MenuInputScanner(menuOptions);
        menuInputScanner.setMessage(DisplayMessages.header() + "Players connected:\n" +
                room.getPlayers().stream()
                        .map(player -> bracketPlayerName() + "\n")
                        .reduce("", (acc, name) -> acc + name));
        prompt.getUserInput(menuInputScanner);

    }

    private void showNotReady(Prompt prompt) {

        String[] menuOptions = new String[]{
                "Back to previous menu"
        };
        MenuInputScanner menuInputScanner = new MenuInputScanner(menuOptions);
        menuInputScanner.setMessage(DisplayMessages.header() + bracketPlayerName() + " - the other players are not ready as yet");
        prompt.getUserInput(menuInputScanner);

    }

    private void showRules(Prompt prompt) {

        showIntro();

        String[] menuOptions = new String[]{
                "Continue",
                "Quit"
        };
        MenuInputScanner stringInputScanner = new MenuInputScanner(menuOptions);
        stringInputScanner.setMessage(bracketPlayerName() + " please choose wisely:");
        switch (prompt.getUserInput(stringInputScanner)) {

            case 1:
                sendMessage("\nLet's play fellow " + bracketPlayerName() + "!\n");
                break;

            case 2:
                sendMessage("\nGoodbye fellow " + bracketPlayerName() + "!\n");
                close();
                break;

            default:
                break;

        }

        HangmanTime.sleep(1000);

    }

    public void showIntro() {

        int sleepTime = 500;

        sendMessage(DisplayMessages.header());

        try {

            Thread.sleep(1000);

            sendMessage(ConsoleColor.CYAN + "                     The legendary game you play on paper, now you can play it with your friends on our server, for free!\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     To play,the Rules are:\n\n");
            Thread.sleep(sleepTime);
            sendMessage(ConsoleColor.GREEN + "                     1: If you know a letter or the word, go ahead and try to guess.\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     2: Each player will have 10 seconds to guess per round.\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     3: When you fail to guess the letter or word, the hangman starts to take form. \n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     4: When the hangman is fully formed, it is a tie and a new game is started.\n\n");
            Thread.sleep(sleepTime);
            sendMessage("                     5: The player with more words completed, wins the game.\n\n");
            Thread.sleep(sleepTime);
            sendMessage(ConsoleColor.YELLOW + "                     To quit the game while playing, write /quit\n\n");
            Thread.sleep(sleepTime);
            sendMessage(ConsoleColor.RED + "                     GO AHEAD & HAVE SOME FUN WITH THIS AMAZING GAME!!!");
            Thread.sleep(sleepTime);
            sendMessage(String.valueOf(ConsoleColor.RESET));

        } catch (InterruptedException e) {

            System.err.println(ConsoleColor.color(ConsoleColor.RED, e.getMessage()));
            logger.log(Level.WARNING, ConsoleColor.color(ConsoleColor.RED, "ERROR - Thread failure " + e.getMessage()));

        }
    }

    public void sendMessage(String line) {

        outputPrintWriter.println(line);

    }

    private void close() {

        try {

            if (inputBufferedReader != null) {

                inputBufferedReader.close();

            }
            if (outputPrintWriter != null) {

                outputPrintWriter.close();

            }
            if (playerSocket != null) {

                logger.log(Level.INFO, ConsoleColor.color(ConsoleColor.WHITE_BACKGROUND_BRIGHT,
                        ConsoleColor.RED_BOLD,
                        "Room #" + room.getRoomNumber() + " - PLAYER #" + playerNumber + " - " + bracketPlayerName() + ": " + "Closing client socket for " + getAddress()));
                playerSocket.close();

            }

            room.removePlayer(this);

        } catch (IOException e) {

            System.err.println(ConsoleColor.color(ConsoleColor.RED, e.getMessage()));
            logger.log(Level.WARNING, ConsoleColor.color(ConsoleColor.RED, "ERROR - Unable to close I/O streams" + e.getMessage()));

        }
    }

    public String getAddress() {

        return playerSocket.getInetAddress().getHostAddress() + ":" + playerSocket.getLocalPort();

    }

    public String bracketPlayerName() {

        return "<" + playerName + ">";

    }

    public void setRoom(Room room) {

        this.room = room;

    }

    private boolean isGameStarted() {

        return gameStarted;

    }

    public void setGameStarted(boolean gameStarted) {

        this.gameStarted = gameStarted;

    }

    public boolean isReady() {

        return ready;

    }

    public void setReady(boolean ready) {

        this.ready = ready;

    }

    private boolean checkAllReady() {

        return room.getPlayers().stream()
                .map(NewPlayer::isReady)
                .reduce(true, (acc, ready) -> acc && ready);

    }

    public boolean isAdmin() {

        return admin;

    }

    public void setAdmin(boolean admin) {

        this.admin = admin;

    }
}
