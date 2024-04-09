package byteceps.validators;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;

public abstract class Validator {
    protected static boolean hasNoInput (String input) {
        return input == null || input.isEmpty();
    }

    protected static void validateListAction (Parser parser) throws Exceptions.InvalidInput {
        //todo
    }
}
