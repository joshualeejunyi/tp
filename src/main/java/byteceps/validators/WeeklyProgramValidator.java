package byteceps.validators;


import byteceps.activities.Day;
import byteceps.activities.Workout;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.processing.ExerciseManager;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.ManagerStrings;

public class WeeklyProgramValidator {


    //@@author pqienso
    public static String validateExecute(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        String commandAction = parser.getAction();
        assert commandAction != null : "Command action must not be null";
        if (commandAction.isEmpty()) {
            throw new Exceptions.InvalidInput("No action specified");
        }
        return commandAction;
    }
    //@@author joshualeejunyi
    public static String validateExecuteAssignAction(Parser parser) throws Exceptions.InvalidInput{
        assert parser.getAction().equals(CommandStrings.ACTION_ASSIGN) : "Action must be assign";
        String day = parser.getAdditionalArguments(CommandStrings.ARG_TO);
        if (day == null || day.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_PROGRAM_ASSIGN);
        }
        return day;
    }
    //@@author joshualeejunyi
    public static void validateAssignWorkoutToDay(Workout chosenDayWorkout, Day selectedDay)
            throws Exceptions.ActivityExistsException{
        if (chosenDayWorkout != null) {
            throw new Exceptions.ActivityExistsException(
                    String.format(ManagerStrings.WORKOUT_ALREADY_ASSIGNED,
                            chosenDayWorkout.getActivityName(), selectedDay.getActivityName()
                    )
            );
        }
    }
    //@@author V4vern
    public static String[] validateLogDetailsExecuteLogAction(Parser parser, ExerciseManager exerciseManager)
            throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        if (!parser.hasAdditionalArguments() || parser.getAdditionalArgumentsLength() < 3) {
            throw new Exceptions.InvalidInput(ManagerStrings.LOG_INCOMPLETE);
        }

        String exerciseName = parser.getActionParameter();
        String sets = parser.getAdditionalArguments(CommandStrings.ARG_SETS);
        String repetition = parser.getAdditionalArguments(CommandStrings.ARG_REPS);
        String weight = parser.getAdditionalArguments(CommandStrings.ARG_WEIGHT);

        if (exerciseName.isBlank() || sets.isBlank() || repetition.isBlank() || weight.isBlank()) {
            throw new Exceptions.InvalidInput(ManagerStrings.LOG_INCOMPLETE);
        }
        if (exerciseManager.doesNotHaveActivity(exerciseName)) {
            throw new Exceptions.ActivityDoesNotExists(
                    String.format(ManagerStrings.ACTIVITY_DOES_NOT_EXIST_EXCEPTION,
                            CommandStrings.COMMAND_EXERCISE, exerciseName)
            );
        }
        String[] logDetails = {exerciseName, sets, repetition, weight};
        return logDetails;
    }

    //MISSING validateDateExecuteLogAction() - WORRIED ABOUT CREATING NEW BUGS

    //@@author pqienso
    public static void validGetTodaysWorkoutString(Workout givenWorkout,Day workoutDay)
            throws Exceptions.ActivityDoesNotExists{
        if (givenWorkout == null) {
            throw new Exceptions.ActivityDoesNotExists(
                    String.format(ManagerStrings.NO_WORKOUT_ASSIGNED_TODAY,
                            workoutDay.getActivityName())
            );
        }
    }
}
