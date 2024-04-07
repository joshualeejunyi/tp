package byteceps.ui;

import byteceps.ui.strings.UiStrings;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class UserInterface {
    private static UserInterface uiInstance;
    private final Scanner in;
    private final PrintStream out;

    public UserInterface() {
        this(System.in, System.out);
    }
    public UserInterface(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    public static UserInterface getInstance() {
        if (uiInstance == null) {
            uiInstance = new UserInterface();
        }
        return uiInstance;
    }

    public void printMessage(String message) {
        System.out.printf(UiStrings.BYTECEP_PROMPT_FORMAT, UiStrings.BYTECEP_PROMPT, message, System.lineSeparator());
        System.out.println(UiStrings.SEPARATOR);
    }

    //@@author pqienso
    public void printWelcomeMessage() {
        out.println(UiStrings.SEPARATOR);
        out.println(UiStrings.MESSAGE_WELCOME);
        out.println(UiStrings.SEPARATOR);
    }

    //@@author pqienso
    public void printGoodbyeMessage() {
        out.println(UiStrings.SEPARATOR);
        out.println(UiStrings.MESSAGE_GOODBYE);
        out.println(UiStrings.SEPARATOR);
    }

    //@@author joshualeejunyi
    public String getUserInput() {
        String userInput;
        do {
            out.printf(UiStrings.USER_PROMPT_FORMAT, UiStrings.USER_PROMPT);
            userInput = in.nextLine().trim();
        } while (userInput.trim().isEmpty());

        return userInput;
    }

}
