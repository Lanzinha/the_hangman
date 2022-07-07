package hangman;

public class Test {

    public static void main(String[] args) {

        Test test = new Test();
        test.testHangman();

    }

    private static String getResult(boolean result) {

        return result ? "OK" : "FAIL";

    }

    private void testHangman() {

        HangmanTest hangmanTest = new HangmanTest();
        System.out.println("Hangman: " + Test.getResult(hangmanTest.test()));

    }
}
