package byteceps.commands;

import byteceps.errors.Exceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void parseCommand_addExercise_exerciseCommand() throws Exceptions.InvalidInput {
        String validInput = "exercise /add deadlift";
        testParser.parseInput(validInput);

        String outputCommand = testParser.getCommand();
        assertEquals(outputCommand, "exercise");
    }

    @Test
    public void hasAdditionalArguments_noAdditionalArguments_false() throws Exceptions.InvalidInput {
        String input = "exercise";
        testParser.parseInput(input);
        assertFalse(testParser.hasAdditionalArguments());
    }

    @Test
    public void hasAdditionalArguments_multipleAdditionalArguments_true() throws Exceptions.InvalidInput {
        String input = "exercise /add deadlift /reps 10 /weight 150";
        testParser.parseInput(input);
        assertTrue(testParser.hasAdditionalArguments());
        assertEquals(2, testParser.getNumAdditionalArguments());
    }

    @Test
    public void getAdditionalArguments_validKey_correctValue() throws Exceptions.InvalidInput {
        String input = "exercise /add deadlift /reps 10";
        testParser.parseInput(input);
        assertEquals("10", testParser.getAdditionalArguments("reps"));
    }

    @Test
    public void getAdditionalArguments_multipleKeys_correctValues() throws Exceptions.InvalidInput {
        String input = "exercise /add deadlift /reps 10 /weight 150";
        testParser.parseInput(input);
        assertEquals("10", testParser.getAdditionalArguments("reps"));
        assertEquals("150", testParser.getAdditionalArguments("weight"));
    }

    @Test
    public void getAdditionalArgumentsLength_noAdditionalArguments_zero() throws Exceptions.InvalidInput {
        String input = "exercise";
        testParser.parseInput(input);
        assertEquals(0, testParser.getAdditionalArgumentsLength());
    }

    @Test
    public void getAdditionalArgumentsLength_multipleAdditionalArguments_correctNumber()
            throws Exceptions.InvalidInput {
        String input = "exercise /add deadlift /reps 10 /weight 150";
        testParser.parseInput(input);
        assertEquals(2, testParser.getAdditionalArgumentsLength());
    }

    @Test
    public void toString_emptyCommand_emptyDetails() {
        assertEquals("COMMAND: " + System.lineSeparator() + System.lineSeparator() +
                "ARGUMENTS: " + System.lineSeparator() + "{}", testParser.toString());
    }

    @Test
    public void toString_commandWithoutAdditionalArguments_correctOutput() throws Exceptions.InvalidInput {
        testParser.parseInput("exercise");
        assertEquals("COMMAND: " + System.lineSeparator() + "exercise" + System.lineSeparator() +
                "ARGUMENTS: " + System.lineSeparator() + "{}", testParser.toString());
    }

    @Test
    public void toString_commandWithMultipleAdditionalArguments_correctOutput() throws Exceptions.InvalidInput {
        testParser.parseInput("exercise /add deadlift /reps 10 /weight 150");
        assertEquals("COMMAND: " + System.lineSeparator() + "exercise" + System.lineSeparator() +
                "ARGUMENTS: " + System.lineSeparator() + "{reps=10, weight=150}", testParser.toString());
    }


}
