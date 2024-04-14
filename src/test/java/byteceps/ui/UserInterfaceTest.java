package byteceps.ui;

import byteceps.ui.strings.UiStrings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

class UserInterfaceTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    private UserInterface ui;

    @BeforeEach
    public void setUp() {
        outContent.reset();
        System.setOut(new PrintStream(outContent));
        System.setIn(originalIn);
        UserInterface.resetInstance();
        ui = UserInterface.getInstance();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void printMessage_message_success() {
        String message = "Test message";
        ui.printMessage(message);
        String expectedOutput = String.format(UiStrings.BYTECEP_PROMPT_FORMAT, UiStrings.BYTECEP_PROMPT, message,
                System.lineSeparator()) + UiStrings.SEPARATOR + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void printMessageNoSeparator_message_success() {
        String message = "Test message";
        ui.printMessageNoSeparator(message);
        String expectedOutput = String.format(UiStrings.BYTECEP_PROMPT_FORMAT, UiStrings.BYTECEP_PROMPT, message,
                System.lineSeparator()) + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void printWelcomeMessage_message_success() {
        ui.printWelcomeMessage();
        String expectedOutput = UiStrings.SEPARATOR + System.lineSeparator() +
                UiStrings.MESSAGE_WELCOME + System.lineSeparator() +
                UiStrings.SEPARATOR + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void printGoodbyeMessage_message_success() {
        ui.printGoodbyeMessage();
        String expectedOutput = UiStrings.SEPARATOR + System.lineSeparator() +
                UiStrings.MESSAGE_GOODBYE + System.lineSeparator() +
                UiStrings.SEPARATOR + System.lineSeparator();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void getUserInput_returnsTrimmedInput_success() {
        String simulatedUserInput = "   hello   \n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
        // Create a new UI instance if necessary after setting System.in
        ui = new UserInterface(System.in, System.out);
        String input = ui.getUserInput();
        assertEquals("hello", input);
    }
}
