package byteceps.ui.strings;

//@@author LWachtel1
public class HelpStrings {
    public static final String HELP_LIST_INDENT = "\t\t\t ";

    public static final String HELP_MANAGER_GREETING =
            String.format("%s%s%s%s%s%s%s%s%s%s%s", "To access the help menu for command guidance, please type:",
                    System.lineSeparator(), "help /COMMAND_TYPE_FLAG", System.lineSeparator(),
                    "Available command types (type exactly as shown):", System.lineSeparator(),
                    "exercise", System.lineSeparator(), "workout", System.lineSeparator(), "program");

    public static final String[] EXERCISE_FLAG_FUNCTIONS = {
        "(1) add an exercise",
        "(2) delete an existing exercise",
        "(3) edit an existing exercise's name",
        "(4) list all existing exercises"
    };

    public static final String[] EXERCISE_FLAG_FORMAT = {
        "exercise /add <EXERCISE_NAME [string]>",
        "exercise /delete <EXERCISE_NAME [string]>",
        "exercise /edit <OLD_EXERCISE_NAME [string]> /to <NEW_EXERCISE_NAME [string]>",
        "exercise /list"
    };


    public static final String[] WORKOUT_FLAG_FUNCTIONS = {
        "(1) create a workout plan",
        "(2) delete an existing workout plan",
        "(3) list all existing workout plans",
        "(4) assign an exercise to a specified workout plan",
        "(5) remove an exercise from a specified workout plan",
        "(6) list all exercises in a given workout plan"
    };

    public static final String[] WORKOUT_FLAG_FORMAT = {
        "workout /create <WORKOUT_PLAN_NAME [string]>",
        "workout /delete <WORKOUT_PLAN_NAME [string]>",
        "workout /list",
        "workout /assign <EXERCISE_NAME [string]> /to <WORKOUT_PLAN_NAME [string]>",
        "workout /unassign <EXERCISE_NAME [string]> /from <WORKOUT_PLAN_NAME [string]>",
        "workout /info <WORKOUT_PLAN_NAME [string]>"
    };

    public static final String[] PROGRAM_FLAG_FUNCTIONS = {
        "(1) assign a workout plan to a specific day of the week",
        "(2) view today's workout plan",
        "(3) see all workout plans assigned to each day of the week",
        "(4) remove a workout plan from a given day of the week",
        String.format ("%s%n%s%s", "(5) create a log for the amount of weight, sets & reps completed for an exercise on"
                + " a given day", HELP_LIST_INDENT, "which already has an assigned workout plan"),
        "(6) create a log for a specified date",
        "(7) see all the dates that you have entered at least 1 log entry",
        "(8) view the logs that you have added on a specific date"
    };
    public static final String DAY_STRING = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s%s", System.lineSeparator(),
        "The <DAY [string]> parameter must be a day of the week, and is case insensitive:", System.lineSeparator(),
        "Monday/Mon", System.lineSeparator(), "Tuesday/Tues/Tue", System.lineSeparator(),
        "Wednesday/Wed", System.lineSeparator(), "Thursday/Thurs/Thu", System.lineSeparator(),
        "Friday/Fri", System.lineSeparator(), "Saturday/Sat", System.lineSeparator(),
        "Sunday/Sun");

    public static final String[] PROGRAM_FLAG_FORMAT = {
        "program /assign <WORKOUT_PLAN_NAME [string]> /to <DAY [string]>" + DAY_STRING,
        "program /today",
        "program /list",
        "program /clear <DAY [string]>" + DAY_STRING,
        "program /log <EXERCISE_NAME [string]> /weight <WEIGHT [integer]> /sets <NUMBER_OF_SETS [integer]> "
                + "/reps <NUMBER_OF_REPS [integer]>",
        "program /log <EXERCISE_NAME [string]> /weight <WEIGHT [integer]> /sets <NUMBER_OF_SETS [integer]> "
                + "/reps <NUMBER_OF_REPS [integer]> /date <DATE [yyyy-mm-dd]>", "program /history",
        "program /history <DATE [yyyy-mm-dd]>"
    };
    public static final String HELP_LIST_ITEM = "\t\t\t %s%s";

    public static final String EXERCISE_MESSAGE = "Please enter 'help /exercise LIST_NUMBER'. " +
            "LIST_NUMBER corresponds to the exercise command format you want to see";
    public static final String WORKOUT_MESSAGE ="Please enter 'help /workout LIST_NUMBER'. " +
            "LIST_NUMBER corresponds to the workout command format you want to see";
    public static final String PROGRAM_MESSAGE = "Please enter 'help /program LIST_NUMBER'. " +
            "LIST_NUMBER corresponds to the program command format you want to see";
    public static final String NO_COMMAND_EXCEPTION = "No Command Type Specified";
    public static final String ADDITIONAL_ARGUMENTS_EXCEPTION = "Help menu does not accept additional arguments";
    public static final String INVALID_COMMAND_TYPE = "Command Type Not Recognised";
    public static final String INVALID_COMMAND = "Invalid choice entered: please enter a number from the list";
}
