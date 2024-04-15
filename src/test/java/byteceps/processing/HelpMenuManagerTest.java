package byteceps.processing;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.UserInterface;
import byteceps.ui.strings.HelpStrings;
import byteceps.ui.strings.UiStrings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class HelpMenuManagerTest {

    private Parser parser;
    private HelpMenuManager helpMenuManager;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final UserInterface ui = new UserInterface();

    @BeforeEach
    public void setup() {
        parser = new Parser();
        helpMenuManager = new HelpMenuManager();
    }

    public void setupStreams() {
        System.setOut(new PrintStream(outContent));
    }

    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void execute_helpOnly_success() {
        String helpOnlyInput = "help";
        assertDoesNotThrow(() -> parser.parseInput(helpOnlyInput));

        setupStreams();
        assertDoesNotThrow(() -> ui.printMessage(helpMenuManager.execute(parser)));
        String viewResponse = String.format("%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, HelpStrings.HELP_GUIDANCE_MESSAGE,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());
        assertEquals(viewResponse, outContent.toString());
        restoreStreams();
    }


    @Test
    public void execute_viewFlagMenu_success() {
        String validInputFlagMenu = "help /exercise";
        assertDoesNotThrow(() -> parser.parseInput(validInputFlagMenu));

        setupStreams();
        assertDoesNotThrow(() -> ui.printMessage(helpMenuManager.execute(parser)));
        String flagMenu = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT,
                HelpStrings.EXERCISE_MESSAGE, System.lineSeparator(), HelpStrings.HELP_LIST_INDENT,
                HelpStrings.EXERCISE_FLAG_FUNCTIONS[0], System.lineSeparator(), HelpStrings.HELP_LIST_INDENT,
                HelpStrings.EXERCISE_FLAG_FUNCTIONS[1], System.lineSeparator(), HelpStrings.HELP_LIST_INDENT,
                HelpStrings.EXERCISE_FLAG_FUNCTIONS[2], System.lineSeparator(), HelpStrings.HELP_LIST_INDENT,
                HelpStrings.EXERCISE_FLAG_FUNCTIONS[3], System.lineSeparator(), System.lineSeparator(),
                UiStrings.SEPARATOR, System.lineSeparator());
        assertEquals(flagMenu, outContent.toString());
        restoreStreams();
    }


    @Test
    public void execute_viewSpecificCommandFormat_success() {
        String validInputSpecificCommandFormat = "help /program 1";
        assertDoesNotThrow(() -> parser.parseInput(validInputSpecificCommandFormat));

        setupStreams();
        assertDoesNotThrow(() -> ui.printMessage(helpMenuManager.execute(parser)));
        String commandFormat = String.format("%s%s%s%s%s", UiStrings.BYTECEP_PROMPT,
                HelpStrings.PROGRAM_PARAM_FORMAT[0], System.lineSeparator(), UiStrings.SEPARATOR,
                System.lineSeparator());
        assertEquals(commandFormat, outContent.toString());
        restoreStreams();
    }

    @Test
    public void execute_invalidFlagNoParam_throwsInvalidInput() {
        String invalidFlagInput = "help /schedule";
        assertDoesNotThrow(() -> parser.parseInput(invalidFlagInput));

        setupStreams();
        String errorMessage = HelpStrings.INVALID_COMMAND_TYPE;
        assertEquals(errorMessage, assertThrowsExactly(Exceptions.InvalidInput.class,() ->
                helpMenuManager.execute(parser)).getMessage());
        restoreStreams();
    }

    @Test
    public void execute_invalidFlagValidParam_throwsInvalidInput() {
        String invalidFlagInput = "help /schedule 1";
        assertDoesNotThrow(() -> parser.parseInput(invalidFlagInput));

        setupStreams();
        String errorMessage = HelpStrings.INVALID_COMMAND_TYPE;
        assertEquals(errorMessage, assertThrowsExactly(Exceptions.InvalidInput.class,() ->
                helpMenuManager.execute(parser)).getMessage());
        restoreStreams();
    }


    @Test
    public void execute_outOfBoundsParameterSpecificCommandFormat_returnInvalidCommand() {
        String outOfBoundsParamInput = "help /exercise 10";
        assertDoesNotThrow(() -> parser.parseInput(outOfBoundsParamInput));

        setupStreams();
        String errorMessage = HelpStrings.INVALID_COMMAND;
        assertEquals(errorMessage, assertThrowsExactly(Exceptions.InvalidInput.class,() ->
                helpMenuManager.execute(parser)).getMessage());
        restoreStreams();
    }

    @Test
    public void execute_nonNumericalParameterSpecificCommandFormat_returnInvalidCommand() {
        String outOfBoundsParamInput = "help /exercise abc";
        assertDoesNotThrow(() -> parser.parseInput(outOfBoundsParamInput));

        setupStreams();
        String errorMessage = HelpStrings.INVALID_COMMAND;
        assertEquals(errorMessage, assertThrowsExactly(Exceptions.InvalidInput.class,() ->
                helpMenuManager.execute(parser)).getMessage());
        restoreStreams();
    }
}
