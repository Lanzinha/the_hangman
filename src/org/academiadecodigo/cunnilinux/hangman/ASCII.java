package org.academiadecodigo.cunnilinux.hangman;

public class ASCII {
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static String GAME_LOGO = new String(ANSI_CYAN + "\n" +
            "    .----------------.  .----------------.  .-----------------. .----------------.  .----------------.  .----------------.  .-----------------.\n" +
            "   | .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |\n" +
            "   | |  ____  ____  | || |      __      | || | ____  _____  | || |    ______    | || | ____    ____ | || |      __      | || | ____  _____  | |\n" +
            "   | | |_   ||   _| | || |     /  \\     | || ||_   \\|_   _| | || |  .' ___  |   | || ||_   \\  /   _|| || |     /  \\     | || ||_   \\|_   _| | |\n" +
            "   | |   | |__| |   | || |    / /\\ \\    | || |  |   \\ | |   | || | / .'   \\_|   | || |  |   \\/   |  | || |    / /\\ \\    | || |  |   \\ | |   | |\n" +
            "   | |   |  __  |   | || |   / ____ \\   | || |  | |\\ \\| |   | || | | |    ____  | || |  | |\\  /| |  | || |   / ____ \\   | || |  | |\\ \\| |   | |\n" +
            "   | |  _| |  | |_  | || | _/ /    \\ \\_ | || | _| |_\\   |_  | || | \\ `.___]  _| | || | _| |_\\/_| |_ | || | _/ /    \\ \\_ | || | _| |_\\   |_  | |\n" +
            "   | | |____||____| | || ||____|  |____|| || ||_____|\\____| | || |  `._____.'   | || ||_____||_____|| || ||____|  |____|| || ||_____|\\____| | |\n" +
            "   | |              | || |              | || |              | || |              | || |              | || |              | || |              | |\n" +
            "   | '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |\n" +
            "   '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------' \n\n\n" +
            ANSI_RESET);

    public static String WINNER = new String("             ___________\n" +
            "            '._==_==_=_.'\n" +
            "            .-\\:      /-.\n" +
            "           | (|:.     |) |\n" +
            "            '-|:.     |-'\n" +
            "              \\::.    /\n" +
            "               '::. .'\n" +
            "                 ) (\n" +
            "               _.' '._\n" +
            "              `\"\"\"\"\"\"\"`");

    public static String LOOSER = new String("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@(,,,*%&,,,/&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*,,,,,,,,@*,,,,,*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*,,,,,,,,,,&*,,,,,,,*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@/,,,,,,,,,,,,,,,,,,,,,,,*@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@%,,,,,,,,,,,,,,,,,,,,,,,,,,,%@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@/,,,,,,,,,,,,,,,,,,,,,,,,,,,,,/@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@*,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@%,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,#@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@*,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,*@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&&%#(//*,,,,,,,,*/(#&&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@,                         .@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@,                         ,@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@*    (,*@&       @.#@(    *@*@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@(  ,, (&#         /(,.,,. /@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@%        ,/     (*        %@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@&          #@@@#          &@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@                         @@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@/                       *@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@&                       %@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@.                     .@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@*@@@@@@@@@*                     *@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@%,           ,#@%,           ,%@%,           .%@@@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@(                   .#@,     ,@#.                   /@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@&                                                       &@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@                                                         @@@@@@@@@@@\n" +
            "@@@@@@@@@@@&                                                         &@@@@@@@@@@\n" +
            "@@@@@@@@@@@@.                                                        @@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@                                                       @@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@.                                                   .@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@.                     .,*,.                     .&@@@@@@@@@@@@@@\n" +
            "@@@@@@@@@@@@@@@@@@@%*.         .*#@@@@@@@@@@@@@#*.         .*%@@@@@@@@@@@@@@@@@@");

}
