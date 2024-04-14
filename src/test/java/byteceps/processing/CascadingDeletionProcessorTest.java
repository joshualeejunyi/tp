package byteceps.processing;

import byteceps.activities.Workout;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CascadingDeletionProcessorTest {

    private Parser parser;
    private WorkoutManager workoutManager;
    private WeeklyProgramManager weeklyProgramManager;
    private ExerciseManager exerciseManager;


    @BeforeEach
    public void setUp() {
        parser = new Parser();
        exerciseManager = new ExerciseManager();
        workoutManager = new WorkoutManager(exerciseManager);
        WorkoutLogsManager workoutLogsManager = new WorkoutLogsManager();
        weeklyProgramManager = new WeeklyProgramManager(exerciseManager, workoutManager, workoutLogsManager);
    }

    @Test
    void checkForCascadingDeletions_nonDeleteAction_noActionTaken() {
        String validInput = "workout /create test";
        parser.parseInput(validInput);
        CascadingDeletionProcessor.checkForCascadingDeletions(parser, workoutManager, weeklyProgramManager);
        assertEquals(0, workoutManager.getActivityList().size());
    }

    @Test
    void checkForCascadingDeletions_deleteExerciseFromWorkouts_exerciseRemoved() throws Exceptions.ErrorAddingActivity,
            Exceptions.ActivityExistsException, Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        // Create an exercise and add it to the system
        String validInput = "exercise /add test";
        parser.parseInput(validInput);
        exerciseManager.execute(parser);

        // Create two workouts
        validInput = "workout /create workout1";
        parser.parseInput(validInput);
        workoutManager.execute(parser);
        validInput = "workout /create workout2";
        parser.parseInput(validInput);
        workoutManager.execute(parser);

        // Assign the created exercise 'test' to both workouts
        validInput = "workout /assign test /to workout1";
        parser.parseInput(validInput);
        workoutManager.execute(parser);
        validInput = "workout /assign test /to workout2";
        parser.parseInput(validInput);
        workoutManager.execute(parser);

        // Delete the exercise
        validInput = "exercise /delete test";
        parser.parseInput(validInput);
        exerciseManager.execute(parser);

        // Check cascading deletions in workoutManager
        CascadingDeletionProcessor.checkForCascadingDeletions(parser, workoutManager, weeklyProgramManager);

        // Assertions to verify that the exercise 'test' has been removed from all workouts
        assertEquals(0, ((Workout) workoutManager.getActivityList().get(0)).getExerciseList().size(),
                "Workout1 should have no exercises");
        assertEquals(0, ((Workout) workoutManager.getActivityList().get(1)).getExerciseList().size(),
                "Workout2 should have no exercises");
    }

    @Test
    void checkForCascadingDeletions_deleteExerciseFromWorkouts_workoutRemoved() throws Exceptions.InvalidInput,
            Exceptions.ActivityDoesNotExists, Exceptions.ActivityExistsException, Exceptions.ErrorAddingActivity {
        // Create an exercise and add it to the system
        String validInput = "exercise /add test";
        parser.parseInput(validInput);
        exerciseManager.execute(parser);

        // Create two workouts
        validInput = "workout /create workout1";
        parser.parseInput(validInput);
        workoutManager.execute(parser);
        validInput = "workout /create workout2";
        parser.parseInput(validInput);
        workoutManager.execute(parser);

        // Assign the created exercise 'test' to both workouts
        validInput = "workout /assign test /to workout1";
        parser.parseInput(validInput);
        workoutManager.execute(parser);
        validInput = "workout /assign test /to workout2";
        parser.parseInput(validInput);
        workoutManager.execute(parser);

        // Delete the workout1
        validInput = "workout /delete workout1";
        parser.parseInput(validInput);
        workoutManager.execute(parser);

        // Check cascading deletions in workoutManager
        CascadingDeletionProcessor.checkForCascadingDeletions(parser, workoutManager, weeklyProgramManager);

        // Assertions to verify that workout1 has been removed and workout2 remains unaffected
        assertEquals(1, workoutManager.getActivityList().size(), "Only workout2 should remain");
        assertEquals(1, ((Workout) workoutManager.getActivityList().get(0)).getExerciseList().size(),
                "Workout2 should still have its exercise");
    }

}
