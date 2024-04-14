package byteceps.validators;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;

public class SampleExerciseValidator extends Validator {

    public static String validateCommand(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";
        String action = parser.getAction();

        switch(action) {
        default:
            break;
        }

        return action;
    }

}
