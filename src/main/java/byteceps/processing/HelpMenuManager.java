package byteceps.processing;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.HelpStrings;
import byteceps.ui.strings.CommandStrings;

public class HelpMenuManager {



    public HelpMenuManager() {
    }

    public String printHelpGreeting() {
        return HelpStrings.HELP_MANAGER_GREETING;
    }

    public String execute(Parser parser) throws Exceptions.InvalidInput {

        String menuSelection;

        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";

        if (parser.getAction().isEmpty()) {
            throw new Exceptions.InvalidInput(HelpStrings.NO_COMMAND_EXCEPTION);
        }

        if (parser.hasAdditionalArguments()) {
            throw new Exceptions.InvalidInput(HelpStrings.ADDITIONAL_ARGUMENTS_EXCEPTION);
        }

        switch (parser.getAction()) {
        case CommandStrings.COMMAND_EXERCISE:
            menuSelection = showExerciseCommand(parser);
            break;
        case CommandStrings.COMMAND_WORKOUT:
            menuSelection = showWorkoutCommand(parser);
            break;
        case CommandStrings.COMMAND_PROGRAM:
            menuSelection = showProgramCommand(parser);
            break;
        default:
            throw new Exceptions.InvalidInput(HelpStrings.INVALID_COMMAND_TYPE);
        }

        return menuSelection;
    }

    public String showExerciseCommand(Parser parser) {
        if (parser.getActionParameter().isEmpty()) {
            StringBuilder result = new StringBuilder();
            result.append(String.format("%s%s", HelpStrings.EXERCISE_MESSAGE, System.lineSeparator()));

            for (int i = 0; i < HelpStrings.EXERCISE_FLAG_FUNCTIONS.length; i++) {
                result.append(String.format(HelpStrings.HELP_LIST_ITEM,
                        getExerciseFlagFunctions(i), System.lineSeparator()));
            }

            return result.toString();
        }
        return getFlagFormat(parser.getActionParameter(), CommandStrings.COMMAND_EXERCISE);
    }

    public String showWorkoutCommand(Parser parser) {
        if (parser.getActionParameter().isEmpty()) {
            StringBuilder result = new StringBuilder();
            result.append(String.format("%s%s", HelpStrings.WORKOUT_MESSAGE, System.lineSeparator()));

            for (int i = 0; i < HelpStrings.WORKOUT_FLAG_FUNCTIONS.length; i++) {
                result.append(String.format(HelpStrings.HELP_LIST_ITEM,
                        getWorkoutFlagFunctions(i), System.lineSeparator()));

            }
            return result.toString();
        }
        return getFlagFormat(parser.getActionParameter(), CommandStrings.COMMAND_WORKOUT);
    }

    public String showProgramCommand(Parser parser) {
        if (parser.getActionParameter().isEmpty()) {
            StringBuilder result = new StringBuilder();
            result.append(String.format("%s%s", HelpStrings.PROGRAM_MESSAGE, System.lineSeparator()));

            for (int i = 0; i < HelpStrings.PROGRAM_FLAG_FUNCTIONS.length; i++) {
                result.append(String.format(HelpStrings.HELP_LIST_ITEM,
                        getProgramFlagFunctions(i), System.lineSeparator()));

            }

            return result.toString();

        }
        return getFlagFormat(parser.getActionParameter(), CommandStrings.COMMAND_PROGRAM);
    }

    public String getFlagFormat(String userFlag,String commandType) {
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

    public String getExerciseFlagFunctions(int index) {
        return HelpStrings.EXERCISE_FLAG_FUNCTIONS[index];
    }

    public String getWorkoutFlagFunctions(int index) {
        return HelpStrings.WORKOUT_FLAG_FUNCTIONS[index];
    }

    public String getProgramFlagFunctions(int index) {
        return HelpStrings.PROGRAM_FLAG_FUNCTIONS[index];
    }
    public String getExerciseFlagFormats(int index) {
        return HelpStrings.EXERCISE_FLAG_FORMAT[index];
    }

    public String getWorkoutFlagFormats(int index) {
        return HelpStrings.WORKOUT_FLAG_FORMAT[index];
    }

    public String getProgramFlagFormats(int index) {
        return HelpStrings.PROGRAM_FLAG_FORMAT[index];
    }

}
