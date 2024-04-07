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

class WorkoutManagerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private Parser parser;
    private WorkoutManager workoutManager;
    private ExerciseManager exerciseManager;
    private final UserInterface ui = new UserInterface();

    @BeforeEach
    void setUp() {
        parser = new Parser();
        exerciseManager = new ExerciseManager();
        workoutManager = new WorkoutManager(exerciseManager);
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
    public void execute_createValidWorkout_success() {
        String validInput = "workout /create LegDay";
        parser.parseInput(validInput);
        assertDoesNotThrow(() -> workoutManager.execute(parser));
    }

    @Test
    public void execute_createEmptyNameWorkout_throwsInvalidInput() {
        String emptyInput = "workout /create";
        parser.parseInput(emptyInput);
        assertThrows(AssertionError.class, () -> workoutManager.execute(parser));
    }

    @Test
    public void execute_createDuplicateWorkout_throwsActivityExists() {
        String validInput = "workout /create LegDay";
        parser.parseInput(validInput);
        assertDoesNotThrow(() -> workoutManager.execute(parser));
        assertThrows(Exceptions.ActivityExistsException.class, () -> workoutManager.execute(parser));
    }

    @Test
    public void execute_deleteExistingWorkoutPlan_success() {
        String workoutInput = "workout /create chest day";
        parser.parseInput(workoutInput);
        assertDoesNotThrow(() -> workoutManager.execute(parser));

        String deleteInput = "workout /delete chest day";
        parser.parseInput(deleteInput);
        assertDoesNotThrow(() -> workoutManager.execute(parser));

        // Ensure the workout plan is deleted
        assertThrows(Exceptions.ActivityDoesNotExists.class, () -> workoutManager.retrieve("LegDay"));
    }

    @Test
    public void execute_deleteNonExistingWorkoutPlan_throwsActivityDoesNotExists() {
        String deleteInput = "workout /delete NonExistingWorkout";
        parser.parseInput(deleteInput);
        assertThrows(Exceptions.ActivityDoesNotExists.class, () -> workoutManager.execute(parser));
    }

    @Test
    public void execute_assignExerciseToWorkout_success() {
        String exerciseInput = "exercise /add Squat";
        parser.parseInput(exerciseInput);
        assertDoesNotThrow(() -> exerciseManager.execute(parser));

        String workoutInput = "workout /create legday";
        parser.parseInput(workoutInput);
        assertDoesNotThrow(() -> workoutManager.execute(parser));

        String assignInput = "workout /assign Squat /to legday";
        parser.parseInput(assignInput);
        assertDoesNotThrow(() -> workoutManager.execute(parser));
    }

    @Test
    public void execute_assignExerciseToNonexistentWorkout_throwsActivityDoesNotExist() {
        String exerciseInput = "exercise /add Squat";
        parser.parseInput(exerciseInput);
        assertDoesNotThrow(() -> exerciseManager.execute(parser));

        String validInput = "workout /assign Squat /to NonexistentWorkout";
        parser.parseInput(validInput);
        assertThrows(Exceptions.ActivityDoesNotExists.class, () -> workoutManager.execute(parser));
    }

    @Test
    public void execute_unassignExerciseFromWorkout_success() {
        String exerciseInput = "exercise /add Pushups";
        parser.parseInput(exerciseInput);
        assertDoesNotThrow(() -> exerciseManager.execute(parser));

        String workoutInput = "workout /create chestday";
        parser.parseInput(workoutInput);
        assertDoesNotThrow(() -> workoutManager.execute(parser));

        String assignInput = "workout /assign Pushups /to chestday";
        parser.parseInput(assignInput);
        assertDoesNotThrow(() -> workoutManager.execute(parser));

        String unassignInput = "workout /unassign Pushups /from chestday";
        parser.parseInput(unassignInput);
        assertDoesNotThrow(() -> workoutManager.execute(parser));
    }

    @Test
    public void execute_unassignNonexistentExerciseFromWorkout_throwsActivityDoesNotExist() {
        String validInput = "workout /unassign NonexistentExercise /from LegDay";
        parser.parseInput(validInput);
        assertThrows(Exceptions.ActivityDoesNotExists.class, () -> workoutManager.execute(parser));
    }

    @Test
    public void execute_listWorkouts_success() {
        setUpStreams();

        String validInput1 = "workout /create LegDay";
        String validInput2 = "workout /create ArmDay";

        parser.parseInput(validInput1);
        assertDoesNotThrow(() -> ui.printMessage(workoutManager.execute(parser)));
        parser.parseInput(validInput2);
        assertDoesNotThrow(() -> ui.printMessage(workoutManager.execute(parser)));

        ui.printMessage(workoutManager.getListString());
        String expectedOutput = "[BYTE-CEPS]> Added Workout Plan: legday\n" +
                "-------------------------------------------------\n" +
                "[BYTE-CEPS]> Added Workout Plan: armday\n" +
                "-------------------------------------------------\n" +
                "[BYTE-CEPS]> Listing Workouts:\n" +
                "\t\t\t1. legday\n" +
                "\t\t\t2. armday\n" +
                "\n" +
                "-------------------------------------------------\n";

        assertEquals(expectedOutput.replaceAll("\\s+", ""),
                outContent.toString().replaceAll("\\s+", ""));

        restoreStreams();
    }

    @Test
    public void execute_info_success() {
        String exerciseInput1 = "exercise /add Squat";
        String exerciseInput2 = "exercise /add lunges";
        parser.parseInput(exerciseInput1);
        assertDoesNotThrow(() -> ui.printMessage(exerciseManager.execute(parser)));
        parser.parseInput(exerciseInput2);
        assertDoesNotThrow(() -> ui.printMessage(exerciseManager.execute(parser)));

        String workoutInput = "workout /create legday";
        parser.parseInput(workoutInput);
        assertDoesNotThrow(() -> ui.printMessage(workoutManager.execute(parser)));

        String assignInput1 = "workout /assign Squat /to legday";
        String assignInput2 = "workout /assign lunges /to legday";
        parser.parseInput(assignInput1);
        assertDoesNotThrow(() -> ui.printMessage(workoutManager.execute(parser)));
        parser.parseInput(assignInput2);
        assertDoesNotThrow(() -> ui.printMessage(workoutManager.execute(parser)));

        String infoInput = "workout /info legday";
        parser.parseInput(infoInput);

        setUpStreams();
        assertDoesNotThrow(() -> ui.printMessage(workoutManager.execute(parser)));
        String expectedOutput = "[BYTE-CEPS]> Listing exercises in workout plan 'legday':\n" +
                "\t\t\t1. squat\n" +
                "\t\t\t2. lunges\n" +
                "-------------------------------------------------\n";


        assertEquals(expectedOutput.replaceAll("\\s+", ""), outContent.toString().replaceAll("\\s+", ""));
        restoreStreams();
    }

    @Test
    public void execute_invalidWorkoutAction_throwsIllegalState() {
        String invalidInput = "workout /unknown";
        parser.parseInput(invalidInput);

        assertThrows(IllegalStateException.class, () -> workoutManager.execute(parser));
    }

    @Test
    public void execute_searchExistingWorkout_success() {
        setUpStreams();

        String addInput = "workout /create LegDay";
        parser.parseInput(addInput);
        assertDoesNotThrow(() -> ui.printMessage(workoutManager.execute(parser)));

        String searchInput = "workout /search LegDay";
        parser.parseInput(searchInput);
        assertDoesNotThrow(() -> ui.printMessage(workoutManager.execute(parser)));

        String expectedOutput = "[BYTE-CEPS]> Added Workout Plan: legday\n" +
                "-------------------------------------------------\n" +
                "[BYTE-CEPS]> SearchResults:\n" +
                "\t\t\t1. legday\n" +
                "\n" +
                "-------------------------------------------------\n";

        assertEquals(expectedOutput.replaceAll("\\s+", ""),
                outContent.toString().replaceAll("\\s+", ""));

        restoreStreams();
    }

    @Test
    public void execute_searchNonExistingWorkout_returnsEmptyResults() {
        setUpStreams();

        String searchInput = "workout /search NonExistentWorkout";
        parser.parseInput(searchInput);
        assertDoesNotThrow(() -> ui.printMessage(workoutManager.execute(parser)));

        String expectedOutput = "[BYTE-CEPS]>Noresultsfound\n" +
                "\n" +
                "-------------------------------------------------\n";

        assertEquals(expectedOutput.replaceAll("\\s+", ""),
                outContent.toString().replaceAll("\\s+", ""));

        restoreStreams();
    }

    @Test
    public void execute_searchEmptyQuery_throwsInvalidInput() {
        String searchInput = "workout /search";
        parser.parseInput(searchInput);
        assertThrows(Exceptions.InvalidInput.class, () -> workoutManager.execute(parser));
    }




}
