package byteceps.ui.strings;


public class ManagerStrings {
    public static final String ACTIVITY_EXISTS_EXCEPTION = "The %s entry: %s already exists";
    public static final String ACTIVITY_DELETE_EXCEPTION = "The %s entry: %s does not exist and cannot be deleted";
    public static final String ACTIVITY_EMPTY_LIST_EXCEPTION = "The %s List is Empty!";
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
    public static final String INCOMPLETE_EDIT = "Edit command not complete";

    // Workout strings
    public static final String WORKOUT = "Workout";
    public static final String WORKOUTS = "Workouts";
    public static final String INCOMPLETE_INFO = "info command not complete";
    public static final String INVALID_WORKOUT_LIST = "Invalid command. Use 'workout /list' to list all exercises.";
    public static final String UNASSIGNED_EXERCISE = "Unassigned Exercise '%s' from Workout Plan '%s'";
    public static final String ASSIGNED_EXERCISE = "Assigned Exercise '%s' to Workout Plan '%s'";
    public static final String WORKOUT_EDITED = "Edited Workout Plan from %s to %s";
    public static final String WORKOUT_DELETED = "Deleted Workout: %s";
    public static final String WORKOUT_ADDED = "Added Workout Plan: %s";
    public static final String INCOMPLETE_ASSIGN = "assign command not complete";
    public static final String EXERCISE_ALREADY_ASSIGNED = "Exercise already assigned to workout plan";
    public static final String EMPTY_WORKOUT_PLAN = "Your workout plan %s is empty";
    public static final String LIST_WORKOUT_PLAN = "Listing exercises in workout plan '%s':%n";
    public static final String INCOMPLETE_UNASSIGN = "Unassign command not complete";
    public static final String EXERCISE_WORKOUT_DOES_NOT_EXIST = "The exercise is not in the workout";

    // Weekly Program strings
    public static final String WEEKLY_PROGRAM = "Weekly Program";
    public static final String INCOMPLETE_WEEK = "Week command not complete";
    public static final String WORKOUT_ALREADY_ASSIGNED = "Workout %s is already assigned to %s. " +
            "Please clear it first.";
    public static final String WORKOUT_ASSIGNED = "Workout %s assigned to %s";
    public static final String LOG_INCOMPLETE = "log command not complete";
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
    public static final String LOG_LIST_ITEM = "\t\t\t%d. %s (weight: %d, sets: %d, reps: %d)\n";
}
