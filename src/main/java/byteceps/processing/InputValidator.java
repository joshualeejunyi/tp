package byteceps.processing;

import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.HelpStrings;
import byteceps.commands.Parser;
import byteceps.ui.strings.ManagerStrings;

public class InputValidator {

    //HelpMenuManager

    public InputValidator() {

    }

    public void validateHelpExecute(Parser parser) throws Exceptions.InvalidInput {
        if (parser.getAction().isEmpty()) {
            throw new Exceptions.InvalidInput(HelpStrings.NO_COMMAND_EXCEPTION);
        }

        if (parser.hasAdditionalArguments()) {
            throw new Exceptions.InvalidInput(HelpStrings.ADDITIONAL_ARGUMENTS_EXCEPTION);
        }
    }

    public boolean validateHelpShow(Parser parser){
        return parser.getActionParameter().isEmpty();
    }

    public void validateWorkoutExecute(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";
        if (parser.getAction().isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.NO_ACTION_EXCEPTION);
        }
    }

    public String validateWorkoutExecuteInfo(Parser parser) throws Exceptions.InvalidInput{
        assert parser.getAction().equals(CommandStrings.ACTION_INFO) : "Action must be info";
        String workoutName = parser.getActionParameter();
        if (workoutName == null || workoutName.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_INFO);
        }
        return workoutName;
    }

    public void validateWorkoutExecuteList(Parser parser) throws Exceptions.InvalidInput{
        String userInput = parser.getActionParameter();
        if (!userInput.isEmpty()) {
            throw new Exceptions.InvalidInput(ManagerStrings.INVALID_WORKOUT_LIST);
        }
    }

    public String validateWorkoutProcessWorkout(Parser parser, String activityType) throws Exceptions.InvalidInput {
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


}
