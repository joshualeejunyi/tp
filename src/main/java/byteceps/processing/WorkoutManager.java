package byteceps.processing;

import byteceps.activities.Exercise;
import byteceps.activities.Workout;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.ManagerStrings;
import byteceps.validators.WorkoutValidator;

import java.util.ArrayList;

/**
 * Manages operations related to workout, such as adding, deleting,
 * assigning, unassigning, listing, and searching workout.
 */
public class WorkoutManager extends ActivityManager {
    private final ExerciseManager exerciseManager;

    public WorkoutManager(ExerciseManager exerciseManager) {
        this.exerciseManager = exerciseManager;
    }

    //@@author V4vern
    /**
     * Executes all commands that start with the keyword "workout".
     *
     * @param parser Parser containing user input.
     * @return Message to user after executing the command.
     * @throws Exceptions.InvalidInput            if no command action specified.
     * @throws Exceptions.ActivityDoesNotExists   if user inputs name of an activity that does not exist.
     * @throws Exceptions.ActivityExistsException if user attempts to create an existing workout.
     */
    @Override
    public String execute(Parser parser) throws Exceptions.ActivityExistsException,
            Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        String command = WorkoutValidator.validateCommand(parser);

        String messageToUser;
        switch (command) {
        case CommandStrings.ACTION_CREATE:
            messageToUser = executeCreateAction(parser);
            break;
        case CommandStrings.ACTION_DELETE:
            messageToUser = executeDeleteAction(parser);
            break;
        case CommandStrings.ACTION_EDIT:
            messageToUser = executeEditAction(parser);
            break;
        case CommandStrings.ACTION_ASSIGN:
            messageToUser = executeAssignAction(parser);
            break;
        case CommandStrings.ACTION_UNASSIGN:
            messageToUser = executeUnassignAction(parser);
            break;
        case CommandStrings.ACTION_INFO:
            messageToUser = executeInfoAction(parser);
            break;
        case CommandStrings.ACTION_LIST:
            messageToUser = executeListAction(parser);
            break;
        case CommandStrings.ACTION_SEARCH:
            messageToUser = executeSearchAction(parser);
            break;
        default:
            messageToUser = "";
            assert false : "input should have been validated before switch";
        }

        return messageToUser;
    }

    private String executeInfoAction(Parser parser) throws Exceptions.ActivityDoesNotExists {
        String workoutName = parser.getActionParameter().toLowerCase();
        return getFullWorkoutString(workoutName);
    }

    private String executeListAction(Parser parser) {
        return getListString();
    }

    private String executeUnassignAction(Parser parser) throws Exceptions.ActivityDoesNotExists {
        String workoutName = unassignExerciseFromWorkout(parser);
        return String.format(
                ManagerStrings.UNASSIGNED_EXERCISE, parser.getActionParameter().toLowerCase(), workoutName
        );
    }

    private String executeEditAction(Parser parser) throws Exceptions.ActivityDoesNotExists {
        String newExerciseName = processEditWorkout(parser, this);
        return String.format(
                ManagerStrings.WORKOUT_EDITED, parser.getActionParameter().toLowerCase(), newExerciseName
        );
    }

    private String processEditWorkout(Parser parser, ActivityManager activityManager) throws
            Exceptions.ActivityDoesNotExists {
        String newWorkoutName = parser.getAdditionalArguments(CommandStrings.ARG_TO).toLowerCase();
        String oldWorkoutName = parser.getActionParameter().toLowerCase();

        Workout workoutToEdit = (Workout) retrieve(oldWorkoutName);
        workoutToEdit.editWorkoutName(newWorkoutName, activityManager);
        return newWorkoutName;
    }

    private String executeAssignAction(Parser parser)
            throws Exceptions.ActivityExistsException, Exceptions.ActivityDoesNotExists {
        String workoutPlan = assignExerciseToWorkout(parser);
        return String.format(ManagerStrings.ASSIGNED_EXERCISE, parser.getActionParameter().toLowerCase(), workoutPlan);
    }

    private String executeDeleteAction(Parser parser) throws Exceptions.ActivityDoesNotExists {
        String workoutName = parser.getActionParameter().toLowerCase();
        Workout workoutToDelete = (Workout) retrieve(workoutName);
        delete(workoutToDelete);
        return String.format(ManagerStrings.WORKOUT_DELETED, workoutToDelete.getActivityName());
    }

    private String executeCreateAction(Parser parser) throws Exceptions.ActivityExistsException {
        String newWorkoutName = parser.getActionParameter().toLowerCase();
        Workout newWorkout = new Workout(newWorkoutName);
        add(newWorkout);
        return String.format(ManagerStrings.WORKOUT_ADDED, newWorkout.getActivityName());
    }

    //@@author V4vern
    private String assignExerciseToWorkout(Parser parser) throws
            Exceptions.ActivityExistsException, Exceptions.ActivityDoesNotExists {
        String exerciseName = parser.getActionParameter().toLowerCase();
        String workoutPlanName = parser.getAdditionalArguments(CommandStrings.ARG_TO).toLowerCase();

        Exercise exercise = (Exercise) exerciseManager.retrieve(exerciseName);
        assert exercise != null : "Exercise does not exist";
        Workout workoutPlan = (Workout) retrieve(workoutPlanName);
        assert workoutPlan != null : "Workout plan does not exist";

        if (workoutPlan.getExerciseList().contains(exercise)) {
            throw new Exceptions.ActivityExistsException(ManagerStrings.EXERCISE_ALREADY_ASSIGNED);
        }

        workoutPlan.addExercise(exercise);

        return workoutPlanName;
    }

    //@@author V4vern
    private String getFullWorkoutString(String workoutPlanName) throws Exceptions.ActivityDoesNotExists {
        assert workoutPlanName != null : "Workout plan name cannot be null";
        Workout workout = (Workout) retrieve(workoutPlanName);
        assert workout != null : "Workout plan does not exist";
        StringBuilder message = new StringBuilder();
        ArrayList<Exercise> workoutList = workout.getExerciseList();

        if (workoutList.isEmpty()) {
            return String.format(ManagerStrings.EMPTY_WORKOUT_PLAN, workoutPlanName);
        }

        message.append(String.format(ManagerStrings.LIST_WORKOUT_PLAN, workoutPlanName));

        int index = 1;
        for (Exercise exercise : workoutList) {
            message.append(String.format(ManagerStrings.ACTIVITY_LIST_ITEM, index++, exercise.getActivityName()));
        }
        return message.toString();
    }

    //@@author V4vern
    private String unassignExerciseFromWorkout(Parser parser) throws Exceptions.ActivityDoesNotExists {
        String workoutPlanName = parser.getAdditionalArguments(CommandStrings.ARG_FROM).toLowerCase();
        String exerciseName = parser.getActionParameter();
        Workout workoutPlan = (Workout) retrieve(workoutPlanName);
        assert workoutPlan != null : "Workout plan does not exist";
        ArrayList<Exercise> workoutList = workoutPlan.getExerciseList();

        boolean exerciseIsInWorkout =
                workoutList.removeIf(exercise -> exercise.getActivityName().equalsIgnoreCase(exerciseName));
        if (!exerciseIsInWorkout) {
            throw new Exceptions.ActivityDoesNotExists(ManagerStrings.EXERCISE_WORKOUT_DOES_NOT_EXIST);
        }

        return workoutPlanName;
    }

    @Override
    public String getActivityType(boolean plural) {
        return plural ? ManagerStrings.WORKOUTS : ManagerStrings.WORKOUT;
    }

    //@@author V4vern
    private String executeSearchAction(Parser parser) {
        String searchTerm = parser.getActionParameter();
        return getSearchResultsString(searchTerm);
    }

}
