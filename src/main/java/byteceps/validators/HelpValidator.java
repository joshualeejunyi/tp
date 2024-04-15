package byteceps.validators;

import byteceps.errors.Exceptions;
import byteceps.ui.strings.HelpStrings;
import byteceps.commands.Parser;

/**
 * Parses user input to help menu to ensure input validity for help menu methods and throw exceptions upon detection of
 * invalid input.
 * */
//@@author LWachtel1
public class HelpValidator extends Validator{

    /**
     * Parses user input for HelpMenuManager's execute() method to ensure a non-empty command action and the correct
     * number of arguments.
     *
     * @param parser Provides user input to be parsed by method.
     * */
    //@@author LWachtel1
    public static void validateCommand(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";

        String commandAction = parser.getAction();
        if (commandAction.isEmpty()) {
            throw new Exceptions.InvalidInput(HelpStrings.NO_COMMAND_EXCEPTION);
        }

        try {
            validateNumAdditionalArgs(0, 0, parser);
        } catch (Exceptions.InvalidInput e) {
            throw new Exceptions.InvalidInput(HelpStrings.ADDITIONAL_ARGUMENTS_EXCEPTION);
        }
    }
}
