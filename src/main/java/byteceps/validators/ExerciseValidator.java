package byteceps.validators;


import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.ManagerStrings;


public class ExerciseValidator extends Validator {
    //@@author V4vern
    public static String validateCommand(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";

        if (parser.getAction().isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.NO_ACTION_EXCEPTION);
        }

        String action = parser.getAction();
        switch (action) {
        case CommandStrings.ACTION_EDIT:
            validateEditAction(parser);
            break;
        case CommandStrings.ACTION_ADD:
            validateAddAction(parser);
            break;
        case CommandStrings.ACTION_DELETE:
            validateDeleteAction(parser);
            break;
        case CommandStrings.ACTION_LIST:
            validateListAction(parser);
            break;
        case CommandStrings.ACTION_SEARCH:
            validateSearchAction(parser);
            break;
        default:
            throw new Exceptions.InvalidInput(String.format(ManagerStrings.UNEXPECTED_ACTION, parser.getAction()));
        }
        return action;
    }

    private static void validateDeleteAction(Parser parser) throws Exceptions.InvalidInput {
        String exerciseToBeDeleted = parser.getActionParameter();
        if (hasNoInput(exerciseToBeDeleted)) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_DELETE_EXERCISE);
        }
        validateNumAdditionalArgs(0, 0, parser);
    }

    //@@author joshualeejunyi
    private static void validateAddAction(Parser parser) throws Exceptions.InvalidInput{
        String exerciseName = parser.getActionParameter();
        if (hasNoInput(exerciseName)) {
            throw new Exceptions.InvalidInput(ManagerStrings.EMPTY_EXCERCISE_NAME);
        }

        if (exerciseName.matches(ManagerStrings.SPECIAL_CHARS_PATTERN)) {
            throw new Exceptions.InvalidInput(
                    String.format(ManagerStrings.SPEC_CHAR_EXCEPTION, CommandStrings.COMMAND_EXERCISE));
        }
        validateNumAdditionalArgs(0, 0, parser);
    }
    //@@author LWachtel1
    private static void validateEditAction(Parser parser)throws Exceptions.InvalidInput{
        String oldExerciseName = parser.getActionParameter();
        String newExerciseName = parser.getAdditionalArguments(CommandStrings.ARG_TO);
        if (hasNoInput(oldExerciseName) || hasNoInput(newExerciseName)) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_EDIT);
        }
        validateNumAdditionalArgs(1, 1, parser);
    }
    //@@author V4vern
    private static void validateSearchAction(Parser parser) throws Exceptions.InvalidInput {
        String searchTerm = parser.getActionParameter();
        if (hasNoInput(searchTerm)) {
            throw new Exceptions.InvalidInput(ManagerStrings.EMPTY_SEARCH);
        }
        validateNumAdditionalArgs(0, 0, parser);
    }
}
