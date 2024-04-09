package byteceps.validators;


import byteceps.activities.Exercise;
import byteceps.activities.Workout;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.ManagerStrings;


public class WorkoutValidator {

    public WorkoutValidator() {

    }

    //@@author V4vern
    //@@author pqienso
    public static String validateExecute(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";
        String command = parser.getAction();
        if (command.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.NO_ACTION_EXCEPTION);
        }

        return command;
    }


    //@@author pqienso
    public static String validateExecuteInfoAction(Parser parser) throws Exceptions.InvalidInput{
        assert parser.getAction().equals(CommandStrings.ACTION_INFO) : "Action must be info";
        String workoutName = parser.getActionParameter().toLowerCase();
        if (workoutName == null || workoutName.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_INFO);
        }
        return workoutName;
    }

    //@@author V4vern
    //@@author pqienso
    public static void validateExecuteListAction(Parser parser) throws Exceptions.InvalidInput{
        String userInput = parser.getActionParameter();
        if (!userInput.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INVALID_WORKOUT_LIST);
        }
    }

    //@@author V4vern
    public static String[] validateProcessEditWorkout(Parser parser) throws Exceptions.InvalidInput {
        String newWorkoutName = parser.getAdditionalArguments(CommandStrings.ARG_TO).toLowerCase();
        String workoutName = parser.getActionParameter().toLowerCase();

        if (newWorkoutName == null || newWorkoutName.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_EDIT);
        }

        String[] workoutNames = {newWorkoutName, workoutName};

        return workoutNames;
    }

    //@@author V4vern
    public static String validateProcessWorkout(Parser parser, String activityType) throws Exceptions.InvalidInput {
        String workoutName = parser.getActionParameter();
        assert !workoutName.isEmpty() : "Workout name cannot be empty";
        if (workoutName.isEmpty()) {
            throw new Exceptions.InvalidInput("Workout name cannot be empty");
        } else if (workoutName.matches(ManagerStrings.SPECIAL_CHARS_PATTERN)) {
            throw new Exceptions.InvalidInput(
                    String.format(ManagerStrings.SPEC_CHAR_EXCEPTION, activityType)
            );
        }

        return workoutName;
    }
    //@@author V4vern
    public static String[] validateNamesAssignExerciseToWorkout(Parser parser) throws Exceptions.InvalidInput {
        String exerciseName = parser.getActionParameter().toLowerCase();
        assert exerciseName != null : "Exercise name cannot be null";
        String workoutPlanName = parser.getAdditionalArguments("to").toLowerCase();
        assert workoutPlanName != null : "Workout plan name cannot be null";
        if (workoutPlanName == null) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_ASSIGN);
        }

        String[] exerciseWorkout = {exerciseName, workoutPlanName};

        return exerciseWorkout;

    }
    //@@author V4vern
    public static void validateExerciseAssignExerciseToWorkout(Parser parser, Exercise exercise, Workout workoutPlan)
            throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {

        if (workoutPlan.getExerciseList().contains(exercise)) {
            throw new Exceptions.InvalidInput(ManagerStrings.EXERCISE_ALREADY_ASSIGNED);
        }

    }
    //@@author V4vern
    public static String[] validateNamesUnassignExerciseFromWorkout(Parser parser) throws Exceptions.InvalidInput {
        String workoutPlanName = parser.getAdditionalArguments(CommandStrings.ARG_FROM).toLowerCase();
        assert workoutPlanName != null : "Workout plan name cannot be null";
        String exerciseName = parser.getActionParameter();
        assert exerciseName != null : "Exercise name cannot be null";
        if (workoutPlanName == null) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_UNASSIGN);
        }

        String[] exerciseWorkout = {exerciseName, workoutPlanName};

        return exerciseWorkout;

    }
    //@@author V4vern
    public static void validateExerciseUnassignExerciseFromWorkout(boolean exerciseIsInWorkout)
            throws Exceptions.ActivityDoesNotExists{
        if (!exerciseIsInWorkout) {
            throw new Exceptions.ActivityDoesNotExists(ManagerStrings.EXERCISE_WORKOUT_DOES_NOT_EXIST);
        }
    }

    //@@author V4vern
    public static String validateExecuteSearchAction(Parser parser) throws Exceptions.InvalidInput {
        String searchTerm = parser.getActionParameter();
        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.EMPTY_SEARCH);
        }
        return searchTerm;
    }


}
