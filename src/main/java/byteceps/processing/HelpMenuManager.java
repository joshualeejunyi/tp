package byteceps.processing;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.HelpStrings;
import byteceps.ui.strings.CommandStrings;
import byteceps.validators.HelpValidator;

//@@author LWachtel1
public class HelpMenuManager {

    public HelpMenuManager() {

    }

    public String getHelpGreetingString() {
        return HelpStrings.HELP_MANAGER_GREETING;
    }

    public String execute(Parser parser) throws Exceptions.InvalidInput {
        HelpValidator.validateCommand(parser);

        String commandToShow = parser.getAction().toLowerCase();
        boolean showAllActions = parser.getActionParameter().isEmpty();

        if (showAllActions) {
            return generateAllActions(commandToShow);
        }
        String flag = parser.getActionParameter();
        return getFlagFormat(flag, commandToShow);

    }

    private String generateAllActions(String command) {
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
            flagFunctions = new String[0];
            assert false : "input must be validated before calling this method";
        }

        StringBuilder result = new StringBuilder();
        for (String flagFunction : flagFunctions) {
            result.append(String.format(HelpStrings.HELP_LIST_ITEM, flagFunction, System.lineSeparator()));
        }
        result.delete(0, 4); //remove the first 3 tabs
        return result.toString();
    }

    private String getFlagFormat(String userFlag,String commandType) {
        try {
            int flagChoice = Integer.parseInt(userFlag);
            int flagIndex = flagChoice - 1;

            switch (commandType) {
            case CommandStrings.COMMAND_EXERCISE:
                return getExerciseFlagFormats(flagIndex);
            case CommandStrings.COMMAND_WORKOUT:
                return getWorkoutFlagFormats(flagIndex);
            case CommandStrings.COMMAND_PROGRAM:
                return getProgramFlagFormats(flagIndex);
            default:
                return "";
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return HelpStrings.INVALID_COMMAND;
        }
    }

    private String getExerciseFlagFormats(int index) {
        return HelpStrings.EXERCISE_FLAG_FORMAT[index];
    }
    private String getWorkoutFlagFormats(int index) {
        return HelpStrings.WORKOUT_FLAG_FORMAT[index];
    }
    private String getProgramFlagFormats(int index) {
        return HelpStrings.PROGRAM_FLAG_FORMAT[index];
    }

}
