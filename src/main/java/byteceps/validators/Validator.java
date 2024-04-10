package byteceps.validators;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.ManagerStrings;

public abstract class Validator {
    protected static boolean hasNoInput(String input) {
        return input == null || input.isEmpty();
    }

    protected static void validateListAction(Parser parser) throws Exceptions.InvalidInput {
        validateNumAdditionalArgs(0, 0, parser);
    }

    protected static void validateNumAdditionalArgs(int minNumArgs, int maxNumArgs, Parser parser)
            throws Exceptions.InvalidInput {
        int numArgs = parser.getNumAdditionalArguments();
        if(numArgs < minNumArgs || numArgs > maxNumArgs) {
            throw new Exceptions.InvalidInput(ManagerStrings.TOO_MANY_ARGS);
        }
    }
}
