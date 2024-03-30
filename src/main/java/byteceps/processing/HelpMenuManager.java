package byteceps.processing;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;

public class HelpMenuManager {

    private final String helpManagerGreeting =
            String.format("%s%s%s%s%s%s%s%s%s%s%s", "To access the help menu for command guidance, please type:",
                    System.lineSeparator(), "help /COMMAND_TYPE_FLAG", System.lineSeparator(),
                    "Available command types (type exactly as shown):", System.lineSeparator(),
                    "exercise", System.lineSeparator(), "workout", System.lineSeparator(), "program");


    private final String[] exerciseFlagFunctions = {
        "(1) add an exercise",
        "(2) delete an existing exercise",
        "(3) edit an existing exercise's name",
        "(4) list all existing exercises"
    };

    private final String[] exerciseFlagFormats = {
        "exercise /add <EXERCISE_NAME [string]>",
        "exercise /delete <EXERCISE_NAME [string]>",
        "exercise /edit <OLD_EXERCISE_NAME [string]> /to <NEW_EXERCISE_NAME [string]>",
        "exercise /list"
    };


    private final String[] workoutFlagFunctions = {
        "(1) create a workout plan",
        "(2) delete an existing workout plan",
        "(3) list all existing workout plans",
        "(4) assign an exercise to a specified workout plan",
        "(5) remove an exercise from a specified workout plan",
        "(6) list all exercises in a given workout plan"
    };

    private final String[] workoutFlagFormats = {
        "workout /create <WORKOUT_PLAN_NAME [string]>",
        "workout /delete <WORKOUT_PLAN_NAME [string]>",
        "workout /list",
        "workout /assign <EXERCISE_NAME [string]> /to <WORKOUT_PLAN_NAME [string]>",
        "workout /unassign <EXERCISE_NAME [string]> /from <WORKOUT_PLAN_NAME [string]>",
        "workout /info <WORKOUT_PLAN_NAME [string]>"
    };

    private final String[] programFlagFunctions = {
        "(1) assign a workout plan to a specific day of the week",
        "(2) view today's workout plan",
        "(3) see all workout plans assigned to each day of the week",
        "(4) remove a workout plan from a given day of the week",
        "(5) create a log for the amount of weight, sets & reps completed for an exercise on a given day which"
                + " already has an assigned workout plan",
        "(6) create a log for a specified date",
        "(7) see all the dates that you have entered at least 1 log entry",
        "(8) view the logs that you have added on a specific date"
    };

    private String dayString = String.format ("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s",System.lineSeparator(),
            "The <DAY [string]> parameter must be a day of the week, and is case insensitive:", System.lineSeparator(),
            "Monday/Mon", System.lineSeparator(), "Tuesday/Tues/Tue", System.lineSeparator(),
            "Wednesday/Wed", System.lineSeparator(), "Thursday/Thurs/Thu", System.lineSeparator(),
            "Friday/Fri", System.lineSeparator(), "Saturday/Sat", System.lineSeparator(),
            "Sunday/Sun");

    private final String[] programFlagFormats = {
        "program /assign <WORKOUT_PLAN_NAME [string]> /to <DAY [string]>" + dayString,
        "program /today",
        "program /list",
        "program /clear <DAY [string]>" + dayString,
        "program /log <EXERCISE_NAME [string]> /weight <WEIGHT [integer]> /sets <NUMBER_OF_SETS [integer]> "
                + "/reps <NUMBER_OF_REPS [integer]>",
        "program /log <EXERCISE_NAME [string]> /weight <WEIGHT [integer]> /sets <NUMBER_OF_SETS [integer]> "
                + "/reps <NUMBER_OF_REPS [integer]> /date <DATE [yyyy-mm-dd]>", "program /history",
        "program /history <DATE [yyyy-mm-dd]>"
    };

    public HelpMenuManager() {
    }

    public String printHelpGreeting() {
        return helpManagerGreeting;
    }

    public String execute(Parser parser) throws Exceptions.InvalidInput {

        String menuSelection;

        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";

        if (parser.getAction().isEmpty()) {
            throw new Exceptions.InvalidInput("No command type specified");
        }

        if (parser.hasAdditionalArguments()) {
            throw new Exceptions.InvalidInput("Help menu does not accept additional arguments");
        }

        switch (parser.getAction()) {
        case "exercise":
            menuSelection = showExerciseCommand(parser);
            break;
        case "workout":
            menuSelection = showWorkoutCommand(parser);
            break;
        case "program":
            menuSelection = showProgramCommand(parser);
            break;
        default:
            throw new Exceptions.InvalidInput("Command type is not recognised");
        }

        return menuSelection;
    }

    public String showExerciseCommand(Parser parser) {
        if (parser.getActionParameter().isEmpty()) {
            String exerciseMessage = "Please enter 'help /exercise LIST_NUMBER'. LIST_NUMBER corresponds to the "
                    + "exercise command format you want to see";

            StringBuilder result = new StringBuilder();

            result.append(String.format("%s%s", exerciseMessage, System.lineSeparator()));

            for (int i = 0; i < exerciseFlagFunctions.length; i++) {
                result.append(String.format("\t\t\t %s%s", getExerciseFlagFunctions(i), System.lineSeparator()));

            }

            String exerciseCommandChoices = result.toString();

            return exerciseCommandChoices;

        }

        String commandFormat = getFlagFormat(parser.getActionParameter(), "exercise");

        return commandFormat;

    }

    public String showWorkoutCommand(Parser parser) {
        if (parser.getActionParameter().isEmpty()) {
            String workoutMessage = "Please enter 'help /workout LIST_NUMBER'. LIST_NUMBER corresponds to the "
                    + "workout command format you want to see";

            StringBuilder result = new StringBuilder();

            result.append(String.format("%s%s", workoutMessage, System.lineSeparator()));

            for (int i = 0; i < workoutFlagFunctions.length; i++) {
                result.append(String.format("\t\t\t %s%s", getWorkoutFlagFunctions(i), System.lineSeparator()));

            }

            String workoutCommandChoices = result.toString();

            return workoutCommandChoices;

        }
        String commandFormat = getFlagFormat(parser.getActionParameter(), "workout");

        return commandFormat;

    }

    public String showProgramCommand(Parser parser) {
        if (parser.getActionParameter().isEmpty()) {
            String programMessage = "Please enter 'help /program LIST_NUMBER'. LIST_NUMBER corresponds to the "
                    + "program command format you want to see";

            StringBuilder result = new StringBuilder();

            result.append(String.format("%s%s", programMessage, System.lineSeparator()));

            for (int i = 0; i < programFlagFunctions.length; i++) {
                result.append(String.format("\t\t\t %s%s", getProgramFlagFunctions(i), System.lineSeparator()));

            }

            String programCommandChoices = result.toString();

            return programCommandChoices;

        }
        String commandFormat = getFlagFormat(parser.getActionParameter(), "program");

        return commandFormat;

    }

    public String getFlagFormat(String userFlag,String commandType) {
        try {
            int flagChoice = Integer.parseInt(userFlag);
            int flagIndex = flagChoice - 1;

            switch (commandType) {
            case "exercise":
                return getExerciseFlagFormats(flagIndex);
            case "workout":
                return getWorkoutFlagFormats(flagIndex);
            case "program":
                return getProgramFlagFormats(flagIndex);
            default:
                return "";

            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            String incorrectSelection = "Invalid choice entered: please enter a number from the list";
            return incorrectSelection;
        }

    }

    public String getExerciseFlagFunctions(int index) {
        return exerciseFlagFunctions[index];
    }

    public String getWorkoutFlagFunctions(int index) {
        return workoutFlagFunctions[index];
    }

    public String getProgramFlagFunctions(int index) {
        return programFlagFunctions[index];
    }
    public String getExerciseFlagFormats(int index) {
        return exerciseFlagFormats[index];
    }

    public String getWorkoutFlagFormats(int index) {
        return workoutFlagFormats[index];
    }

    public String getProgramFlagFormats(int index) {
        return programFlagFormats[index];
    }

}
