package byteceps.processing;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.HelpStrings;
import byteceps.ui.strings.CommandStrings;
import byteceps.validators.HelpValidator;

/**
 * Displays the correct command formatting for all BYTE-CEPS functionalities. Command formats are divided into 3
 * categories, each one corresponding to one of the 3 main commands (exercise, workout & program).
 */

//@@author LWachtel1
public class HelpMenuManager {

    public HelpMenuManager() {

    }

    /**
     * Returns String that explains to user how to access each of the 3 "help menus" for the
     * 3 main commands (exercise, workout & program).
     *
     * @return String informing user how to access each "help menu" for the main commands (exercise, workout & program).
     */
    public String getHelpGreetingString() {
        return HelpStrings.HELP_MANAGER_GREETING;
    }


    /**
     * Displays either (1) a command help menu (if no valid numerical parameter is provided) or (2) the specific
     * command format for a specific BYTE-CEPS functionality, which corresponds to the help menu entry specified by
     * the provided valid numerical parameter.
     *
     * @param parser Parser containing required user input.
     * @return String detailing either an entire command help menu or a specific command formatting.
     */
    public String execute(Parser parser) throws Exceptions.InvalidInput {
        HelpValidator.validateCommand(parser);

        String commandToShow = parser.getAction().toLowerCase();
        boolean showAllActions = parser.getActionParameter().isEmpty();

        if (showAllActions) {
            return generateAllActions(commandToShow);
        }
        String parameter = parser.getActionParameter();
        return getParamFormat(parameter, commandToShow);

    }

    /**
     * Builds a String containing a command's entire help menu (either exercise, workout  or program) i.e., a command's
     * entire list of associated functionalities.
     *
     * @param command Command for which user wants to view help menu.
     * @return String containing a command's help menu as an indented, numbered list starting from 1.
     */
    private String generateAllActions(String command) throws Exceptions.InvalidInput {
        String[] flagFunctions;
        switch (command) {
        case CommandStrings.COMMAND_EXERCISE:
            flagFunctions = HelpStrings.EXERCISE_FLAG_FUNCTIONS;
            break;
        case CommandStrings.COMMAND_WORKOUT:
            flagFunctions = HelpStrings.WORKOUT_FLAG_FUNCTIONS;
            break;
        case CommandStrings.COMMAND_PROGRAM:
            flagFunctions = HelpStrings.PROGRAM_FLAG_FUNCTIONS;
            break;
        default:
            throw new Exceptions.InvalidInput(HelpStrings.INVALID_COMMAND_TYPE);
        }

        StringBuilder result = new StringBuilder();
        for (String flagFunction : flagFunctions) {
            result.append(String.format(HelpStrings.HELP_LIST_ITEM, flagFunction, System.lineSeparator()));
            //ui.printMessage() adds another lineSeparator so Help Menu will have extra blank line before SEPARATOR
        }
        result.delete(0, 13); //remove the first 12 whitespace characters
        return result.toString();
    }

    /**
     * Returns a String containing the specific command format for a specific BYTE-CEPS functionality, corresponding
     * to the help menu entry specified by the user-provided numerical parameter.
     *
     * @param userParam String corresponding to numerical position of help menu entry of user-desired command format.
     * @param commandType String specifying which command help menu to find command formatting from.
     * @return String containing the specific command format corresponding to provided parameter.
     */

    private String getParamFormat(String userParam, String commandType) throws Exceptions.InvalidInput {
        try {
            int paramChoice = Integer.parseInt(userParam);
            int paramIndex = paramChoice - 1;

            switch (commandType) {
            case CommandStrings.COMMAND_EXERCISE:
                return getExerciseParamFormats(paramIndex);
            case CommandStrings.COMMAND_WORKOUT:
                return getWorkoutParamFormats(paramIndex);
            case CommandStrings.COMMAND_PROGRAM:
                return getProgramParamFormats(paramIndex);
            default:
                throw new Exceptions.InvalidInput(HelpStrings.INVALID_COMMAND_TYPE);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new Exceptions.InvalidInput(HelpStrings.INVALID_COMMAND);
        }
    }

    private String getExerciseParamFormats(int index) {
        return HelpStrings.EXERCISE_PARAM_FORMAT[index];
    }
    private String getWorkoutParamFormats(int index) {
        return HelpStrings.WORKOUT_PARAM_FORMAT[index];
    }
    private String getProgramParamFormats(int index) {
        return HelpStrings.PROGRAM_PARAM_FORMAT[index];
    }

}
