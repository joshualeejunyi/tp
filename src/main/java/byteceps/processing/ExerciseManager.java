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

        String command = ExerciseValidator.validateCommand(parser);

        String messageToUser = "";
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
            assert false : "user input should have been validated beforehand";
        }

        return messageToUser;
    }
    //@@author LWachtel1
    private String executeEditAction(Parser parser) throws Exceptions.ActivityDoesNotExists {
        String oldExerciseName = parser.getActionParameter().toLowerCase();
        String newExerciseName = parser.getAdditionalArguments(CommandStrings.ARG_TO);

        if (oldExerciseName.equals(newExerciseName)) {
            return String.format(ManagerStrings.EXERCISE_NAME_SAME, oldExerciseName);
        }

        Exercise retrievedExercise = retrieveExercise(parser);
        retrievedExercise.editExerciseName(newExerciseName.toLowerCase(), this);
        newExerciseName =  newExerciseName.toLowerCase();

        return String.format(
                ManagerStrings.EXERCISE_EDITED, parser.getActionParameter().toLowerCase(), newExerciseName
        );
    }
    //@@author V4vern
    private String executeListAction(Parser parser) {
        return getListString();
    }
    //@@author V4vern
    private String executeDeleteAction(Parser parser) throws Exceptions.ActivityDoesNotExists {
        Exercise retrievedExercise =  retrieveExercise(parser);
        delete(retrievedExercise);
        return String.format(ManagerStrings.EXERCISE_DELETED, retrievedExercise.getActivityName());
    }

    //@@author V4vern
    private String executeAddAction(Parser parser) throws Exceptions.ActivityExistsException {
        String exerciseName =  parser.getActionParameter().toLowerCase();
        Exercise newExercise = new Exercise(exerciseName);
        add(newExercise);
        return String.format(ManagerStrings.EXERCISE_ADDED, newExercise.getActivityName());
    }


    //@@author V4vern
    private Exercise retrieveExercise(Parser parser) throws Exceptions.ActivityDoesNotExists {
        String exerciseName = parser.getActionParameter().toLowerCase();
        return (Exercise) retrieve(exerciseName);
    }

    //@@author joshualeejunyi
    @Override
    public String getActivityType(boolean plural) {
        return plural ? ManagerStrings.EXERCISES : ManagerStrings.EXERCISE;
    }

    //@@author V4vern
    private String executeSearchAction(Parser parser) {
        String searchTerm = parser.getActionParameter();
        return getSearchResultsString(searchTerm);
    }

}
