package byteceps.processing;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.UserInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExerciseManagerTest {
    private Parser parser;
    private ExerciseManager exerciseManager;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final UserInterface ui = new UserInterface();

    @BeforeEach
    public void setup() {
        parser = new Parser();
        exerciseManager = new ExerciseManager();
    }

    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void execute_addValidExercise_success() {
        String validInput = "exercise /add Pushups";
        parser.parseInput(validInput);
        assertDoesNotThrow(() -> exerciseManager.execute(parser));
    }

    @Test
    public void execute_addEmptyNameExercise_throwsInvalidInput() {
        String emptyInput = "exercise /add";
        parser.parseInput(emptyInput);
        assertThrows(Exceptions.InvalidInput.class, () -> exerciseManager.execute(parser));
    }

    @Test
    public void execute_addDuplicateExercise_throwsActivityExists() {
        String validInput = "exercise /add Pushups";
        parser.parseInput(validInput);
        assertDoesNotThrow(() -> exerciseManager.execute(parser));
        assertThrows(Exceptions.ActivityExistsException.class, () -> exerciseManager.execute(parser));
    }

    @Test
    public void execute_deleteValidExercise_success() {
        String validInput = "exercise /add Pushups";
        parser.parseInput(validInput);
        assertDoesNotThrow(() -> exerciseManager.execute(parser));

        String deleteInput = "exercise /delete Pushups";
        parser.parseInput(deleteInput);
        assertDoesNotThrow(() -> exerciseManager.execute(parser));
    }

    @Test
    public void execute_deleteInvalidExercise_throwsActivityDoesNotExist() {
        String invalidInput = "exercise /delete Run";
        parser.parseInput(invalidInput);
        assertThrows(Exceptions.ActivityDoesNotExists.class, () -> exerciseManager.execute(parser));
    }

    @Test
    public void execute_listExercises_success() {
        setUpStreams();

        String validInput1 = "exercise /add Pushups";
        String validInput2 = "exercise /add Deadlifts";

        parser.parseInput(validInput1);
        assertDoesNotThrow(() -> ui.printMessage(exerciseManager.execute(parser)));
        parser.parseInput(validInput2);
        assertDoesNotThrow(() -> ui.printMessage(exerciseManager.execute(parser)));

        ui.printMessage(exerciseManager.getListString());
        String expectedOutput = "[BYTE-CEPS]> Added Exercise: pushups\n" +
                "-------------------------------------------------\n" +
                "[BYTE-CEPS]> Added Exercise: deadlifts\n" +
                "-------------------------------------------------\n" +
                "[BYTE-CEPS]> Listing Exercises:\n" +
                "\t\t\t1. pushups\n" +
                "\t\t\t2. deadlifts\n" +
                "\n" +
                "-------------------------------------------------\n";

        assertEquals(expectedOutput.replaceAll("\\s+",""),
                outContent.toString().replaceAll("\\s+",""));

        restoreStreams();
    }

    @Test
    public void execute_invalidExerciseAction_throwsIllegalState() {
        String invalidInput = "exercise /unknown";
        parser.parseInput(invalidInput);

        assertThrows(IllegalStateException.class, () -> exerciseManager.execute(parser));
    }

    //@@author LWachtel1
    @Test
    public void execute_validExerciseEdit_success() {
        setUpStreams();

        String validInput = "exercise /add Pushups";
        parser.parseInput(validInput);
        assertDoesNotThrow(() -> ui.printMessage(exerciseManager.execute(parser)));
        ui.printMessage(exerciseManager.getListString());

        String editedInput = "exercise /edit Pushups /to Push Ups";
        parser.parseInput(editedInput);
        assertDoesNotThrow(() -> ui.printMessage(exerciseManager.execute(parser)));
        ui.printMessage(exerciseManager.getListString());

        String expectedOutput = "[BYTE-CEPS]> Added Exercise: pushups\n" +
                "-------------------------------------------------\n" +
                "[BYTE-CEPS]> Listing Exercises:\n" +
                "\t\t\t1. pushups\n" +
                "\n" +
                "-------------------------------------------------\n" +
                "[BYTE-CEPS]> Edited Exercise from pushups to push ups\n" +
                "-------------------------------------------------\n" +
                "[BYTE-CEPS]> Listing Exercises:\n" +
                "\t\t\t1. push ups\n" +
                "\n" +
                "-------------------------------------------------\n";

        assertEquals(expectedOutput.replaceAll("\\s+",""),
                outContent.toString().replaceAll("\\s+",""));

        restoreStreams();
    }

    //@@author LWachtel1
    @Test
    public void execute_invalidExerciseEdit_throwsInvalidInput() {
        String invalidInput = "exercise /edit";
        parser.parseInput(invalidInput);
        assertThrows(Exceptions.InvalidInput.class, () -> exerciseManager.execute(parser));

        String invalidInput2 = "exercise /edit non existent";
        parser.parseInput(invalidInput2);
        assertThrows(Exceptions.InvalidInput.class, () -> exerciseManager.execute(parser));

    }

    //@@author LWachtel1
    @Test
    public void invalidExerciseEdit_emptyNewExercise_throwsInvalidInput() {
        String validInput = "exercise /add Push ups";
        parser.parseInput(validInput);
        assertDoesNotThrow(() -> exerciseManager.execute(parser));

        String editedInput = "exercise /edit Push ups /to";
        parser.parseInput(editedInput);
        assertThrows(Exceptions.InvalidInput.class, () -> exerciseManager.execute(parser));
    }

    //@@author LWachtel1
    @Test
    public void invalidExerciseEdit_invalidPreviousName_throwsActivityDoesNotExists() {
        String validInput = "exercise /add Push ups";
        parser.parseInput(validInput);
        assertDoesNotThrow(() -> exerciseManager.execute(parser));

        String editedInput = "exercise /edit Pull ups /to Decline Push ups";
        parser.parseInput(editedInput);
        assertThrows(Exceptions.ActivityDoesNotExists.class, () -> exerciseManager.execute(parser));
    }

    //@@author LWachtel1
    @Test
    public void invalidExerciseEdit_emptyPreviousName_throwsActivityDoesNotExists() {
        String validInput = "exercise /add Push ups";
        parser.parseInput(validInput);
        assertDoesNotThrow(() -> exerciseManager.execute(parser));

        String editedInput = "exercise /edit /to Decline Push ups";
        parser.parseInput(editedInput);
        assertThrows(Exceptions.ActivityDoesNotExists.class, () -> exerciseManager.execute(parser));
    }

    //@@author LWachtel1
    @Test
    public void execute_invalidFlag_throwsIllegalStateException() {
        String invalidInput = "exercise /change Push ups";
        parser.parseInput(invalidInput);
        assertThrows(IllegalStateException.class, () -> exerciseManager.execute(parser));
    }


    @Test
    public void execute_searchExistingExercise_success() {
        setUpStreams();

        String addInput = "exercise /add Pushups";
        parser.parseInput(addInput);
        assertDoesNotThrow(() -> ui.printMessage(exerciseManager.execute(parser)));

        String searchInput = "exercise /search Pushups";
        parser.parseInput(searchInput);
        assertDoesNotThrow(() -> ui.printMessage(exerciseManager.execute(parser)));

        String expectedOutput = "[BYTE-CEPS]> AddedExercise: \n" +
                "\t\t\t pushups\n" +
                "\n" +
                "-------------------------------------------------\n" +
                "[BYTE-CEPS]> SearchResults:\n" +
                "\t\t\t1. pushups\n" +
                "\n" +
                "-------------------------------------------------\n";

        assertEquals(expectedOutput.replaceAll("\\s+", ""),
                outContent.toString().replaceAll("\\s+", ""));

        restoreStreams();
    }

    @Test
    public void execute_searchNonexistentExercise_emptyResult() {
        setUpStreams();

        String searchInput = "exercise /search Nonexistent";
        parser.parseInput(searchInput);
        assertDoesNotThrow(() -> ui.printMessage(exerciseManager.execute(parser)));

        String expectedOutput = "[BYTE-CEPS]>Noresultsfound\n" +
                "-------------------------------------------------\n";

        assertEquals(expectedOutput.replaceAll("\\s+", ""),
                outContent.toString().replaceAll("\\s+", ""));

        restoreStreams();
    }

    @Test
    public void execute_searchEmptyQuery_throwsInvalidInput() {
        String emptyInput = "exercise /search ";
        parser.parseInput(emptyInput);
        assertThrows(Exceptions.InvalidInput.class, () -> exerciseManager.execute(parser));
    }
}

