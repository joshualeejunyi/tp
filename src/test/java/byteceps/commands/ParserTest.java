package byteceps.commands;

import byteceps.errors.Exceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {

    private Parser testParser;

    @BeforeEach
    public void setUp() {
        testParser = new Parser();
    }

    @Test
    public void parseInput_validCommand_success() throws Exceptions.InvalidInput {
        String validInput = "exercise /add deadlift";
        testParser.parseInput(validInput);

        String command = testParser.getCommand();
        String action = testParser.getAction();
        String parameter = testParser.getActionParameter();

        assertEquals(command, "exercise");
        assertEquals(action, "add");
        assertEquals(parameter, "deadlift");
    }

    @Test
    public void parseCommand_addExercise_exerciseCommand() {
        String validInput = "exercise /add deadlift";
        assertDoesNotThrow(() -> testParser.parseInput(validInput));

        String outputCommand = testParser.getCommand();
        assertEquals(outputCommand, "exercise");
    }

    @Test
    public void hasAdditionalArguments_noAdditionalArguments_false() {
        String input = "exercise";
        assertDoesNotThrow(() -> testParser.parseInput(input));
        assertFalse(testParser.hasAdditionalArguments());
    }

    @Test
    public void hasAdditionalArguments_multipleAdditionalArguments_true() {
        String input = "exercise /add deadlift /reps 10 /weight 150";
        assertDoesNotThrow(() -> testParser.parseInput(input));
        assertTrue(testParser.hasAdditionalArguments());
        assertEquals(2, testParser.getNumAdditionalArguments());
    }

    @Test
    public void getAdditionalArguments_validKey_correctValue() {
        String input = "exercise /add deadlift /reps 10";
        assertDoesNotThrow(() -> testParser.parseInput(input));
        assertEquals("10", testParser.getAdditionalArguments("reps"));
    }

    @Test
    public void getAdditionalArguments_multipleKeys_correctValues() {
        String input = "exercise /add deadlift /reps 10 /weight 150";
        assertDoesNotThrow(() -> testParser.parseInput(input));
        assertEquals("10", testParser.getAdditionalArguments("reps"));
        assertEquals("150", testParser.getAdditionalArguments("weight"));
    }

    @Test
    public void getAdditionalArgumentsLength_noAdditionalArguments_zero() {
        String input = "exercise";
        assertDoesNotThrow(() -> testParser.parseInput(input));
        assertEquals(0, testParser.getAdditionalArgumentsLength());
    }

    @Test
    public void getAdditionalArgumentsLength_multipleAdditionalArguments_correctNumber() {
        String input = "exercise /add deadlift /reps 10 /weight 150";
        assertDoesNotThrow(() -> testParser.parseInput(input));
        assertEquals(2, testParser.getAdditionalArgumentsLength());
    }

    @Test
    public void toString_emptyCommand_emptyDetails() {
        assertEquals("COMMAND: " + System.lineSeparator() + System.lineSeparator() +
                "ARGUMENTS: " + System.lineSeparator() + "{}", testParser.toString());
    }

    @Test
    public void toString_commandWithoutAdditionalArguments_correctOutput() {
        assertDoesNotThrow(() -> testParser.parseInput("exercise"));
        assertEquals("COMMAND: " + System.lineSeparator() + "exercise" + System.lineSeparator() +
                "ARGUMENTS: " + System.lineSeparator() + "{}", testParser.toString());
    }

    @Test
    public void toString_commandWithMultipleAdditionalArguments_correctOutput() {
        assertDoesNotThrow(() -> testParser.parseInput("exercise /add deadlift /reps 10 /weight 150"));
        assertEquals("COMMAND: " + System.lineSeparator() + "exercise" + System.lineSeparator() +
                "ARGUMENTS: " + System.lineSeparator() + "{reps=10, weight=150}", testParser.toString());
    }


}
