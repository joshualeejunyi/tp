package byteceps.validators;


import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.HelpStrings;
import byteceps.commands.Parser;


//@@author LWachtel1
public class HelpValidator {


    //@@author LWachtel1
    public static void validateCommand(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";

        String commandAction = parser.getAction();
        if (commandAction.isEmpty()) {
            throw new Exceptions.InvalidInput(HelpStrings.NO_COMMAND_EXCEPTION);
        }

        if (parser.hasAdditionalArguments()) {
            throw new Exceptions.InvalidInput(HelpStrings.ADDITIONAL_ARGUMENTS_EXCEPTION);
        }

        boolean commandToShowIsInvalid = !commandAction.equals(CommandStrings.COMMAND_EXERCISE) &&
                !commandAction.equals(CommandStrings.COMMAND_WORKOUT) &&
                !commandAction.equals(CommandStrings.COMMAND_PROGRAM);
        if (commandToShowIsInvalid) {
            throw new Exceptions.InvalidInput(HelpStrings.INVALID_COMMAND_TYPE);
        }
    }

}
