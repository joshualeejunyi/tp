package byteceps.processing;

import byteceps.activities.Exercise;
import byteceps.activities.Workout;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.ManagerStrings;

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
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";

        if (parser.getAction().isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.NO_ACTION_EXCEPTION);
        }

        String messageToUser;

        switch (parser.getAction()) {
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
            throw new IllegalStateException(String.format(ManagerStrings.UNEXPECTED_ACTION, parser.getAction()));
        }

        return messageToUser;
    }

    private String executeInfoAction(Parser parser) throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        assert parser.getAction().equals(CommandStrings.ACTION_INFO) : "Action must be info";
        String workoutName = parser.getActionParameter();
        if (workoutName == null || workoutName.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_INFO);
        }

        return getFullWorkoutString(workoutName);
    }

    private String executeListAction(Parser parser) throws Exceptions.InvalidInput {
        String userInput = parser.getActionParameter();
        if (!userInput.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INVALID_WORKOUT_LIST);
        }
        return getListString();
    }

    private String executeUnassignAction(Parser parser)
            throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        assert parser.getAction().equals(CommandStrings.ACTION_UNASSIGN) : "Action must be unassign";
        String workoutName = unassignExerciseFromWorkout(parser);
        return String.format(
                ManagerStrings.UNASSIGNED_EXERCISE, parser.getActionParameter(), workoutName
        );
    }

    private String executeEditAction(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        String newExerciseName = processEditWorkout(parser, this);
        return String.format(
                ManagerStrings.WORKOUT_EDITED, parser.getActionParameter(), newExerciseName
        );
    }

    private String processEditWorkout(Parser parser, ActivityManager activityManager) throws
            Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {

        String newWorkoutName = parser.getAdditionalArguments(CommandStrings.ARG_TO);
        String workoutName = parser.getActionParameter();

        if (newWorkoutName == null || newWorkoutName.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_EDIT);
        }
        Workout workoutToEdit = (Workout) retrieve(workoutName);
        workoutToEdit.editWorkoutName(newWorkoutName, activityManager);
        return newWorkoutName;
    }


    private String executeAssignAction(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        assert parser.getAction().equals(CommandStrings.ACTION_ASSIGN) : "Action must be assign";
        String workoutPlan = assignExerciseToWorkout(parser);
        return String.format(ManagerStrings.ASSIGNED_EXERCISE, parser.getActionParameter(), workoutPlan);
    }

    private String executeDeleteAction(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        assert parser.getAction().equals(CommandStrings.ACTION_DELETE) : "Action must be delete";
        Workout existingWorkout = processWorkout(parser);
        delete(existingWorkout);
        return String.format(ManagerStrings.WORKOUT_DELETED, existingWorkout.getActivityName());
    }

    private String executeCreateAction(Parser parser) throws Exceptions.InvalidInput,
            Exceptions.ActivityExistsException {
        assert parser.getAction().equals(CommandStrings.ACTION_CREATE) : "Action must be create";
        Workout newWorkout = processWorkout(parser);
        add(newWorkout);
        return String.format(ManagerStrings.WORKOUT_ADDED, newWorkout.getActivityName());
    }

    //@@author V4vern
    private Workout processWorkout(Parser parser) throws Exceptions.InvalidInput {
        String workoutName = parser.getActionParameter();
        workoutName = workoutName.toLowerCase();
        assert !workoutName.isEmpty() : "Workout name cannot be empty";
        if (workoutName.isEmpty()) {
            throw new Exceptions.InvalidInput("Workout name cannot be empty");
        } else if (workoutName.matches(ManagerStrings.SPECIAL_CHARS_PATTERN)) {
            throw new Exceptions.InvalidInput(
                    String.format(ManagerStrings.SPEC_CHAR_EXCEPTION, getActivityType(false))
            );
        }
        return new Workout(workoutName);
    }

    //@@author V4vern
    private String assignExerciseToWorkout(Parser parser) throws Exceptions.InvalidInput,
            Exceptions.ActivityDoesNotExists {
        String exerciseName = parser.getActionParameter().toLowerCase();
        assert exerciseName != null : "Exercise name cannot be null";
        String workoutPlanName = parser.getAdditionalArguments("to");
        assert workoutPlanName != null : "Workout plan name cannot be null";
        if (workoutPlanName == null) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_ASSIGN);
        }

        Exercise exercise = (Exercise) exerciseManager.retrieve(exerciseName);
        assert exercise != null : "Exercise does not exist";
        Workout workoutPlan = (Workout) retrieve(workoutPlanName);
        assert workoutPlan != null : "Workout plan does not exist";

        if (workoutPlan.getExerciseList().contains(exercise)) {
            throw new Exceptions.InvalidInput(ManagerStrings.EXERCISE_ALREADY_ASSIGNED);
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
    private String unassignExerciseFromWorkout(Parser parser) throws Exceptions.InvalidInput,
            Exceptions.ActivityDoesNotExists {
        String workoutPlanName = parser.getAdditionalArguments(CommandStrings.ARG_FROM);
        assert workoutPlanName != null : "Workout plan name cannot be null";
        String exerciseName = parser.getActionParameter();
        assert exerciseName != null : "Exercise name cannot be null";
        if (workoutPlanName == null) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_UNASSIGN);
        }

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
    private String executeSearchAction(Parser parser) throws Exceptions.InvalidInput {
        String searchTerm = parser.getActionParameter();
        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.EMPTY_SEARCH);
        }
        return getSearchResultsString(searchTerm);
    }

}
