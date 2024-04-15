package byteceps.validators;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.HelpStrings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
//@@author LWachtel1
public class HelpValidatorTest {

    private Parser parser;

    @BeforeEach
    public void setup() {
        parser = new Parser();
    }

    @Test
    public void validateCommand_hasFlagNoAdditionalArgs_success() {
        String flagNoAdditionalArgs = "help /exercise";
        parser.parseInput(flagNoAdditionalArgs);
        assertDoesNotThrow( () -> HelpValidator.validateCommand(parser));
    }

    @Test
    public void validateCommand_hasFlagParamNoAdditionalArgs_success() {
        String flagParamNoAdditionalArgs = "help /exercise 1";
        parser.parseInput(flagParamNoAdditionalArgs);
        assertDoesNotThrow( () -> HelpValidator.validateCommand(parser));
    }

    @Test
    public void validateCommand_hasFlagAdditionalArgs_throwsInvalidInput() {
        String flagAdditionalArgs = "help /exercise /error";
        parser.parseInput(flagAdditionalArgs);

        String errorMessage = HelpStrings.ADDITIONAL_ARGUMENTS_EXCEPTION;
        assertEquals(errorMessage, assertThrowsExactly(Exceptions.InvalidInput.class,() ->
                HelpValidator.validateCommand(parser)).getMessage());
    }

    @Test
    public void validateCommand_hasFlagParamAdditionalArgs_throwsInvalidInput() {
        String flagParamAdditionalArgs = "help /exercise 1 /error";
        parser.parseInput(flagParamAdditionalArgs);

        String errorMessage = HelpStrings.ADDITIONAL_ARGUMENTS_EXCEPTION;
        assertEquals(errorMessage, assertThrowsExactly(Exceptions.InvalidInput.class,() ->
                HelpValidator.validateCommand(parser)).getMessage());
    }

    @Test
    public void validateCommand_emptyFlag_throwsInvalidInput() {
        String emptyFlag = "help / ";
        parser.parseInput(emptyFlag);

        String errorMessage = HelpStrings.NO_COMMAND_EXCEPTION;
        assertEquals(errorMessage, assertThrowsExactly(Exceptions.InvalidInput.class,() ->
                HelpValidator.validateCommand(parser)).getMessage());
    }
}
