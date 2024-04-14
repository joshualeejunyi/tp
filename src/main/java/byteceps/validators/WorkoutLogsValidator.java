package byteceps.validators;

import byteceps.activities.WorkoutLog;
import byteceps.errors.Exceptions;
import byteceps.processing.ExerciseManager;
import byteceps.ui.UserInterface;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.ManagerStrings;

import java.util.List;

public class WorkoutLogsValidator extends  Validator {
    public static void exerciseExists(ExerciseManager exerciseManager, String exerciseName)
            throws Exceptions.ActivityDoesNotExist {
        if (exerciseManager.doesNotHaveActivity(exerciseName)) {
            throw new Exceptions.ActivityDoesNotExist(
                    String.format(ManagerStrings.ACTIVITY_DOES_NOT_EXIST_EXCEPTION,
                            CommandStrings.COMMAND_EXERCISE, exerciseName)
            );
        }
    }

    public static void hasNegativeInput(List<Integer> weightsList, List<Integer> repsList, int setsInt) {
        boolean hasNegativeWeights = weightsList.stream().anyMatch(weightInt -> weightInt < 0);
        boolean hasNegativeReps = repsList.stream().anyMatch(repInt -> repInt < 0);

        if (hasNegativeReps || setsInt < 0 || hasNegativeWeights) {
            throw new NumberFormatException();
        }
    }

    public static void removeExerciseIfLogExists(WorkoutLog workoutLog, String exerciseName)
            throws Exceptions.ActivityDoesNotExist {
        if (!workoutLog.hasExerciseName(exerciseName)) {
            throw new Exceptions.ActivityDoesNotExist(
                    String.format(ManagerStrings.ACTIVITY_DOES_NOT_EXIST_EXCEPTION,
                            ManagerStrings.EXERCISE, exerciseName));
        }

        // technically should not print here, but it would be too many layers back to return a message
        UserInterface ui = UserInterface.getInstance();
        ui.printMessageNoSeparator(String.format(ManagerStrings.OVERWRITE_EXERCISE_LOG, exerciseName));
        workoutLog.removeExistingLogEntry(exerciseName);
    }
}
