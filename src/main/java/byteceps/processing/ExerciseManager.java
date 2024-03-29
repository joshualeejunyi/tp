package byteceps.processing;


import byteceps.activities.Exercise;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.UserInterface;


public class ExerciseManager extends ActivityManager {
    //@@author V4vern
    @Override
    public String execute(Parser parser) throws Exceptions.InvalidInput,
            Exceptions.ErrorAddingActivity, Exceptions.ActivityExistsException,
            Exceptions.ActivityDoesNotExists {
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";

        if (parser.getAction().isEmpty()) {
            throw new Exceptions.InvalidInput("No action specified");
        }

        String messageToUser;
        switch (parser.getAction()) {
        case "add":
            messageToUser = executeAddAction(parser);
            break;
        case "delete":
            messageToUser = executeDeleteAction(parser);
            break;
        //@@author LWachtel1
        case "edit":
            messageToUser = executeEditAction(parser);
            break;
        //@@author V4vern
        case "list":
            messageToUser = validateListAction(parser);
            break;
        case "search":
            messageToUser = executeSearchAction(parser);
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + parser.getAction());
        }

        return messageToUser;
    }

    private String executeEditAction(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        String newExerciseName = processEditExercise(parser);
        return String.format(
                "Edited Exercise from %s to %s", parser.getActionParameter(), newExerciseName
        );
    }


    public String validateListAction(Parser parser) throws Exceptions.InvalidInput {
        String userInput = parser.getActionParameter();
        if (!userInput.isEmpty()) {
            throw new Exceptions.InvalidInput("Invalid command. Use 'exercise /list' to list all exercises.");
        }
        return executeListAction();
    }

    private String executeDeleteAction(Parser parser) throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        assert parser.getAction().equals("delete") : "Action must be delete";
        Exercise retrievedExercise;
        retrievedExercise = retrieveExercise(parser);
        delete(retrievedExercise);
        return String.format("Deleted Exercise: %s", retrievedExercise.getActivityName());
    }

    private String executeAddAction(Parser parser) throws Exceptions.InvalidInput,
            Exceptions.ActivityExistsException {
        assert parser.getAction().equals("add") : "Action must be add";
        Exercise newExercise = processAddExercise(parser);
        add(newExercise);
        return String.format("Added Exercise: %s", newExercise.getActivityName());
    }

    //@@author V4vern
    public Exercise processAddExercise(Parser parser) throws Exceptions.InvalidInput {
        String exerciseName = parser.getActionParameter();
        if (exerciseName.isEmpty()) {
            throw new Exceptions.InvalidInput("Exercise name cannot be empty");
        } else if (exerciseName.matches(".*[{}\\[\\]/\\\\:,#\\-].*")) {
            throw new Exceptions.InvalidInput("Exercise name cannot contain special characters:" +
                    " { } [ ] / \\\\ : , # -");
        }
        return new Exercise(exerciseName);
    }

    //@@author V4vern
    public Exercise retrieveExercise(Parser parser) throws Exceptions.ActivityDoesNotExists {
        String exerciseName = parser.getActionParameter();
        return (Exercise) retrieve(exerciseName);
    }

    //@@author LWachtel1
    public String processEditExercise(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        String newExerciseName = parser.getAdditionalArguments("to");

        if (newExerciseName == null || newExerciseName.isEmpty()) {
            throw new Exceptions.InvalidInput("Edit command not complete");
        }

        Exercise retrievedExercise = retrieveExercise(parser);
        retrievedExercise.editExerciseName(newExerciseName);
        return newExerciseName;
    }

    //@@author joshualeejunyi
    @Override
    public String getActivityType(boolean plural) {
        return plural ? "Exercises" : "Exercise";
    }

    //@@author V4vern
    private String executeSearchAction(Parser parser) throws Exceptions.InvalidInput {
        String searchTerm = parser.getActionParameter();
        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new Exceptions.InvalidInput("Search term cannot be empty.");
        }
        return getSearchResultsString(searchTerm);
    }

}
