package byteceps.validators;


import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.ManagerStrings;


public class ExerciseValidator {

    public ExerciseValidator() {

    }

    public String validateExecute(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";

        if (parser.getAction().isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.NO_ACTION_EXCEPTION);
        }
        String command = parser.getAction();
        return command;
    }

    public void validateExecuteListAction(Parser parser) throws Exceptions.InvalidInput {
        String userInput = parser.getActionParameter();
        if (!userInput.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INVALID_EXERCISE_LIST);
        }
    }

    public String validateProcessAddExercise(Parser parser, String activityType)throws Exceptions.InvalidInput{
        String exerciseName = parser.getActionParameter();
        if (exerciseName.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.EMPTY_EXCERCISE_NAME);
        } else if (exerciseName.matches(ManagerStrings.SPECIAL_CHARS_PATTERN)) {
            throw new Exceptions.InvalidInput(
                    String.format(ManagerStrings.SPEC_CHAR_EXCEPTION, activityType));
        }
        return exerciseName;
    }

    public String validateProcessEditExercise(Parser parser)throws Exceptions.InvalidInput{
        String newExerciseName = parser.getAdditionalArguments(CommandStrings.ARG_TO);

        if (newExerciseName == null || newExerciseName.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_EDIT);
        }
        return newExerciseName;

    }

    public String validateExecuteSearchAction(Parser parser) throws Exceptions.InvalidInput {
        String searchTerm = parser.getActionParameter();
        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.EMPTY_SEARCH);
        }
        return searchTerm;
    }

}
