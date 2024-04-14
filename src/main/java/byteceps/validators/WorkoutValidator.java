package byteceps.validators;


import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.ManagerStrings;


public class WorkoutValidator extends Validator {
    //@@author V4vern
    public static String validateCommand(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";
        String command = parser.getAction();
        if (command.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.NO_ACTION_EXCEPTION);
        }

        switch (command) {
        case CommandStrings.ACTION_CREATE:
            validateCreateAction(parser);
            break;
        case CommandStrings.ACTION_DELETE:
            validateDeleteAction(parser);
            break;
        case CommandStrings.ACTION_EDIT:
            validateEditAction(parser);
            break;
        case CommandStrings.ACTION_ASSIGN:
            validateAssignAction(parser);
            break;
        case CommandStrings.ACTION_UNASSIGN:
            validateUnassignAction(parser);
            break;
        case CommandStrings.ACTION_INFO:
            validateInfoAction(parser);
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

        return command;
    }

    //@@author pqienso
    private static void validateInfoAction(Parser parser) throws Exceptions.InvalidInput {
        assert parser.getAction().equals(CommandStrings.ACTION_INFO) : "Action must be info";
        String workoutName = parser.getActionParameter();
        if (hasNoInput(workoutName)) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_INFO);
        }
        validateNumAdditionalArgs(0, 0, parser);
    }

    private static void validateCreateAction(Parser parser) throws Exceptions.InvalidInput {
        String createdWorkoutName = parser.getActionParameter();
        if (hasNoInput(createdWorkoutName)) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_CREATE);
        }

        if (createdWorkoutName.matches(ManagerStrings.SPECIAL_CHARS_PATTERN)) {
            throw new Exceptions.InvalidInput(
                    String.format(ManagerStrings.SPEC_CHAR_EXCEPTION, CommandStrings.COMMAND_WORKOUT));
        }

        validateNumAdditionalArgs(0, 0, parser);
    }

    private static void validateDeleteAction(Parser parser) throws Exceptions.InvalidInput {
        String toDeleteWorkoutName = parser.getActionParameter();
        if (hasNoInput(toDeleteWorkoutName)) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_DELETE_WORKOUT);
        }
        validateNumAdditionalArgs(0, 0, parser);
    }

    //@@author V4vern
    private static void validateEditAction(Parser parser) throws Exceptions.InvalidInput {
        String newWorkoutName = parser.getAdditionalArguments(CommandStrings.ARG_TO);
        String oldWorkoutName = parser.getActionParameter();
        if (hasNoInput(newWorkoutName) || hasNoInput(oldWorkoutName)) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_EDIT);
        }
        validateNumAdditionalArgs(1, 1, parser);
    }

    //@@author V4vern
    private static void validateAssignAction(Parser parser) throws Exceptions.InvalidInput {
        String exerciseName = parser.getActionParameter();
        String workoutPlanName = parser.getAdditionalArguments(CommandStrings.ARG_TO);
        if (hasNoInput(exerciseName) || hasNoInput(workoutPlanName)) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_ASSIGN);
        }
        validateNumAdditionalArgs(1, 1, parser);
    }

    //@@author V4vern
    private static void validateUnassignAction(Parser parser) throws Exceptions.InvalidInput {
        String workoutPlanName = parser.getAdditionalArguments(CommandStrings.ARG_FROM);
        String exerciseName = parser.getActionParameter();
        if (hasNoInput(workoutPlanName) || hasNoInput(exerciseName)) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_UNASSIGN);
        }
        validateNumAdditionalArgs(1, 1, parser);
    }

    //@@author V4vern
    private static void validateSearchAction(Parser parser) throws Exceptions.InvalidInput {
        String searchTerm = parser.getActionParameter();
        if (hasNoInput(searchTerm)) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_SEARCH);
        }
        validateNumAdditionalArgs(0, 0, parser);
    }

}
