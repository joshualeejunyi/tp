package byteceps.validators;

import byteceps.activities.Exercise;
import byteceps.activities.ExerciseLog;
import byteceps.activities.WorkoutLog;
import byteceps.errors.Exceptions;
import byteceps.processing.ExerciseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkoutLogsValidatorTest {
    private ExerciseManager exerciseManager;
    private WorkoutLog workoutLog;

    void setUp() {
        exerciseManager = new ExerciseManager();
        Exercise newExercise = new Exercise("test");
        try {
            exerciseManager.add(newExercise);
        } catch (Exceptions.ActivityExistsException e) {
            throw new TestInstantiationException("Unable to add exercise to test");
        }

        workoutLog = new WorkoutLog("2024-03-01", "workout");
        ExerciseLog newExerciseLog = new ExerciseLog("test",
                Arrays.asList(1, 1, 1), 3, Arrays.asList(1, 1, 1));
        workoutLog.addExerciseLog(newExerciseLog);
    }
    @Test
    void exerciseExists_exists_success() {
        setUp();
        assertDoesNotThrow(() -> WorkoutLogsValidator.exerciseExists(exerciseManager, "test"));
    }

    @Test
    void exerciseExists_doesNotExist_throwsAcitivityDoesNotExistException() {
        setUp();
        assertThrows(Exceptions.ActivityDoesNotExist.class,
                () -> WorkoutLogsValidator.exerciseExists(exerciseManager, "test1"));
    }

    @Test
    void hasNegativeInput_negativeWeight_throwsNumberFormatException() {
        List<Integer> weightsList = Arrays.asList(-1, 0, 3);
        List<Integer> repsList = Arrays.asList(1, 1, 1);
        int setsInt = 1;
        assertThrows(NumberFormatException.class,
                () -> WorkoutLogsValidator.hasNegativeInput(weightsList, repsList, setsInt));
    }

    @Test
    void hasNegativeInput_negativeRep_throwsNumberFormatException() {
        List<Integer> weightsList = Arrays.asList(1, 0, 3);
        List<Integer> repsList = Arrays.asList(1, -1, 1);
        int setsInt = 1;
        assertThrows(NumberFormatException.class,
                () -> WorkoutLogsValidator.hasNegativeInput(weightsList, repsList, setsInt));
    }

    @Test
    void hasNegativeInput_negativeSet_throwsNumberFormatException() {
        List<Integer> weightsList = Arrays.asList(1, 0, 3);
        List<Integer> repsList = Arrays.asList(1, 1, 1);
        int setsInt = -1;
        assertThrows(NumberFormatException.class,
                () -> WorkoutLogsValidator.hasNegativeInput(weightsList, repsList, setsInt));
    }

    @Test
    void removeExerciseIfLogExists_exists_success() {
        setUp();
        assertDoesNotThrow(()-> WorkoutLogsValidator.removeExerciseIfLogExists(workoutLog, "test"));
    }

    @Test
    void removeExerciseIfLogExists_doesNotExist_throwsActivityDoesNotExist() {
        setUp();
        assertThrows(Exceptions.ActivityDoesNotExist.class,
                ()-> WorkoutLogsValidator.removeExerciseIfLogExists(workoutLog, "test1"));
    }
}
