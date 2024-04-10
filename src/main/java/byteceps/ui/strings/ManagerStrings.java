package byteceps.ui.strings;



public class ManagerStrings {
    public static final String ACTIVITY_EXISTS_EXCEPTION = "The %s entry: %s already exists";
    public static final String ACTIVITY_DELETE_EXCEPTION = "The %s entry: %s does not exist and cannot be deleted";
    public static final String ACTIVITY_EMPTY_LIST_EXCEPTION = "Unable to retrieve any %s. It is empty!";
    public static final String ACTIVITY_DOES_NOT_EXIST_EXCEPTION = "The %s entry: %s does not exist";
    public static final String ACTIVITY_EMPTY_LIST = "Your List of %s is Empty";
    public static final String ACTIVITY_LIST = "Listing %s:%s";
    public static final String ACTIVITY_LIST_ITEM = "\t\t\t%d. %s%n";
    public static final String NO_ACTION_EXCEPTION = "No action specified";
    public static final String UNEXPECTED_ACTION = "Unexpected value: %s";
    public static final String SPECIAL_CHARS_PATTERN = ".*[{}\\[\\]/\\\\:,#\\-].*";
    public static final String SPEC_CHAR_EXCEPTION = "%s name cannot contain special characters: " +
            "{ } [ ] / \\\\ : , # -";
    public static final String EMPTY_SEARCH = "Search term cannot be empty.";
    public static final String NO_RESULTS = "No results found";
    public static final String SEARCH_RESULTS = "Search Results:%s";


    // Exercise strings
    public static final String EXERCISE = "Exercise";
    public static final String EXERCISES = "Exercises";
    public static final String EXERCISE_EDITED = "Edited Exercise from %s to %s";
    public static final String EXERCISE_DELETED = "Deleted Exercise: %s";
    public static final String EXERCISE_ADDED = "Added Exercise: %s";
    public static final String INVALID_EXERCISE_LIST = "Invalid command. Use 'exercise /list' to list all exercises.";
    public static final String EMPTY_EXCERCISE_NAME = "Exercise name cannot be empty";
    public static final String INCOMPLETE_EDIT = "Edit command not complete. " +
            "Please use: exercise /edit <OLD_EXERCISE_NAME [string]> /to <NEW_EXERCISE_NAME [string]>";

    public static final String INCOMPLETE_DELETE_EXERCISE = "Delete command not complete. " +
            "Please use: exercise /delete <EXERCISE_NAME [string]>";

    // Workout strings
    public static final String WORKOUT = "Workout";
    public static final String WORKOUTS = "Workouts";
    public static final String INCOMPLETE_INFO = "Info command not complete. " +
            "Please use: workout /info <WORKOUT_PLAN_NAME [string]> ";
    public static final String INCOMPLETE_CREATE = "Create command not complete. " +
            "Please use: workout /create <NEW_WORKOUT_PLAN_NAME [string]>";
    public static final String INCOMPLETE_DELETE_WORKOUT = "Delete command not complete." +
            "Please use: workout /delete <WORKOUT_PLAN_NAME [string]>";
    public static final String INCOMPLETE_SEARCH = "Search term cannot be left blank." +
            "Please use: workout /search <WORKOUT_PLAN_NAME [string]>";
    public static final String INVALID_WORKOUT_LIST = "Invalid command. Use 'workout /list' to list all exercises.";
    public static final String UNASSIGNED_EXERCISE = "Unassigned Exercise '%s' from Workout Plan '%s'";
    public static final String ASSIGNED_EXERCISE = "Assigned Exercise '%s' to Workout Plan '%s'";
    public static final String WORKOUT_EDITED = "Edited Workout Plan from %s to %s";
    public static final String WORKOUT_DELETED = "Deleted Workout: %s";
    public static final String WORKOUT_ADDED = "Added Workout Plan: %s";
    public static final String INCOMPLETE_ASSIGN = "assign command not complete. " +
            "Please use: workout /assign <EXERCISE_NAME [string]> /to <WORKOUT_PLAN_NAME [string]>";
    public static final String EXERCISE_ALREADY_ASSIGNED = "Exercise already assigned to workout plan";
    public static final String EMPTY_WORKOUT_PLAN = "Your workout plan %s is empty";
    public static final String LIST_WORKOUT_PLAN = "Listing exercises in workout plan '%s':%n";
    public static final String INCOMPLETE_UNASSIGN = "Unassign command not complete. " +
            "Please use: workout /unassign <EXERCISE_NAME [string]> /from <WORKOUT_PLAN_NAME [string]>";
    public static final String EXERCISE_WORKOUT_DOES_NOT_EXIST = "The exercise is not in the workout";

    // Weekly Program strings
    public static final String WEEKLY_PROGRAM = "Weekly Program";
    public static final String INCOMPLETE_PROGRAM_ASSIGN = "Program /assign command not complete. " +
            "Please use: program /assign <WORKOUT_PLAN_NAME [string]> /to <DAY [string]>";
    public static final String WORKOUT_ALREADY_ASSIGNED = "Workout %s is already assigned to %s. " +
            "Please clear it first.";
    public static final String WORKOUT_ASSIGNED = "Workout %s assigned to %s";
    public static final String LOG_INCOMPLETE = "Log command not complete. " +
            "Please use: program /log <EXERCISE_NAME [string]> /weight <WEIGHT [integer]> " +
            "/sets <NUMBER_OF_SETS [integer]> /reps <NUMBER_OF_REPS [integer]> /date <DATE [yyyy-mm-dd]>.";
    public static final String LOG_SUCCESS = "Successfully logged %skg %s with %s sets and %s reps on %s";
    public static final String NO_WORKOUT_ASSIGNED = "There does not seem to be a workout assigned to the date " +
            "%s (day: %s). Please assign one first!";
    public static final String NO_WORKOUT_ASSIGNED_TODAY = "There is no workout assigned today (%s)";
    public static final String INVALID_DATE_ENTERED = "Invalid date entered";
    public static final String PROGRAMS_CLEARED = "All your workouts have been cleared from the week";
    public static final String WORKOUT_CLEARED = "Your workout on %s has been cleared";
    public static final String PROGRAM_LIST = "Your workouts for the week:";
    public static final String PROGRAM_LIST_ITEM = "\t%s: ";

    // Logs strings
    public static final String WORKOUT_LOGS = "Workout Logs";
    public static final String WORKOUT_LOG = "Workout Log";
    public static final String LOG_INVALID_STATE = "RepsSetsManager is not meant to be executed";
    public static final String INVALID_REPS_SETS = "Invalid weight/reps/sets entered!";
    public static final String EXERCISE_NOT_IN_WORKOUT = "The exercise is not in your workout for today!";
    public static final String LOG_LIST = "Listing Exercises on %s:%n";
    public static final String LOG_LIST_ITEM = "\t\t\t\tSet %d: %d kg, %d reps"
            + System.lineSeparator();
    public static final String TOO_MANY_ARGS = "Extra arguments detected. Make sure you are not using" +
            System.lineSeparator() + "the special character '/' in any of your exercise and workout names,"
            + System.lineSeparator() + "or in any part of your input outside command arguments.";
    public static final String INVALID_WEIGHTS_SETS_MISMATCH = "The number of weights provided (%d) does not match " +
            "the declared number of sets (%d). Each set should have a corresponding weight.";
    public static final String INVALID_REPS_SETS_MISMATCH = "The number of repetitions provided (%d) does not match " +
            "the declared number of sets (%d). Each set should have a corresponding number of repetitions.";
}
