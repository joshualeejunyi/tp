package byteceps.processing;


import byteceps.activities.Exercise;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.ManagerStrings;
import byteceps.validators.ExerciseValidator;

/**
 * Manages operations related to exercises, such as adding, deleting, editing, listing, and searching exercises.
 */
public class ExerciseManager extends ActivityManager {


    //@@author V4vern
    /**
     * Executes all commands that start with the keyword "exercise".
     *
     * @param parser Parser containing user input.
     * @return Message to user after executing the command.
     * @throws Exceptions.InvalidInput if no command action specified
     * @throws Exceptions.ErrorAddingActivity If there is an error adding an activity.
     * @throws Exceptions.ActivityDoesNotExists if user inputs name of an activity that does not exist.
     * @throws Exceptions.ActivityExistsException if user attempts to create an existing exercise.
     */
    @Override
    public String execute(Parser parser) throws Exceptions.InvalidInput,
            Exceptions.ErrorAddingActivity, Exceptions.ActivityExistsException,
            Exceptions.ActivityDoesNotExists {

        String command = ExerciseValidator.validateExecute(parser);

        String messageToUser;
        switch (command) {
        case CommandStrings.ACTION_ADD:
            messageToUser = executeAddAction(parser);
            break;
        case CommandStrings.ACTION_DELETE:
            messageToUser = executeDeleteAction(parser);
            break;
        //@@author LWachtel1
        case CommandStrings.ACTION_EDIT:
            messageToUser = executeEditAction(parser);
            break;
        //@@author V4vern
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

    private String executeEditAction(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        String newExerciseName = processEditExercise(parser,this);
        return String.format(
                ManagerStrings.EXERCISE_EDITED, parser.getActionParameter().toLowerCase(), newExerciseName
        );
    }

    private String executeListAction(Parser parser) throws Exceptions.InvalidInput {
        ExerciseValidator.validateExecuteListAction(parser);
        return getListString();
    }

    private String executeDeleteAction(Parser parser) throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        assert parser.getAction().equals(CommandStrings.ACTION_DELETE) : "Action must be delete";
        Exercise retrievedExercise =  retrieveExercise(parser);
        delete(retrievedExercise);
        return String.format(ManagerStrings.EXERCISE_DELETED, retrievedExercise.getActivityName());
    }

    private String executeAddAction(Parser parser) throws Exceptions.InvalidInput,
            Exceptions.ActivityExistsException {
        assert parser.getAction().equals(CommandStrings.ACTION_ADD) : "Action must be add";
        Exercise newExercise = processAddExercise(parser);
        add(newExercise);
        return String.format(ManagerStrings.EXERCISE_ADDED, newExercise.getActivityName());
    }

    //@@author V4vern
    private Exercise processAddExercise(Parser parser) throws Exceptions.InvalidInput {
        String activityType = getActivityType(false);
        String exerciseName =  ExerciseValidator.validateProcessAddExercise(parser, activityType);
        return new Exercise(exerciseName);
    }

    //@@author V4vern
    private Exercise retrieveExercise(Parser parser) throws Exceptions.ActivityDoesNotExists {
        String exerciseName = parser.getActionParameter().toLowerCase();
        return (Exercise) retrieve(exerciseName);
    }

    //@@author LWachtel1
    private String processEditExercise(Parser parser, ActivityManager activityManager) throws
            Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        String newExerciseName = ExerciseValidator.validateProcessEditExercise(parser);

        Exercise retrievedExercise = retrieveExercise(parser);
        retrievedExercise.editExerciseName(newExerciseName.toLowerCase(), activityManager);
        return newExerciseName.toLowerCase();
    }

    //@@author joshualeejunyi
    @Override
    public String getActivityType(boolean plural) {
        return plural ? ManagerStrings.EXERCISES : ManagerStrings.EXERCISE;
    }

    //@@author V4vern
    private String executeSearchAction(Parser parser) throws Exceptions.InvalidInput {
        String searchTerm = ExerciseValidator.validateExecuteSearchAction(parser);
        return getSearchResultsString(searchTerm);
    }

}
