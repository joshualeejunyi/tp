package byteceps.processing;

import byteceps.activities.Day;
import byteceps.activities.Workout;
import byteceps.activities.Exercise;
import byteceps.activities.Activity;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;

public class WeeklyProgramManager extends ActivityManager {
    public static final String[] DAYS = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
    private final ExerciseManager exerciseManager;
    private final WorkoutManager workoutManager;
    private final WorkoutLogsManager workoutLogsManager;

    public WeeklyProgramManager(ExerciseManager exerciseManager, WorkoutManager workoutManager,
                                WorkoutLogsManager workoutLogsManager) {
        this.exerciseManager = exerciseManager;
        this.workoutManager = workoutManager;
        this.workoutLogsManager = workoutLogsManager;
        initializeDays();
    }

    private void initializeDays() {
        for (String day : DAYS) {
            Day newDay = new Day(day);
            newDay.setAssignedWorkout(null);
            activitySet.add(newDay);
        }
    }

    private Day getDay(String day) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        switch (day.toLowerCase()) {
        case "mon":
        case "monday":
            return (Day) retrieve(DAYS[0]);
        case "tue":
        case "tues":
        case "tuesday":
            return (Day) retrieve(DAYS[1]);
        case "wed":
        case "wednesday":
            return (Day) retrieve(DAYS[2]);
        case "thu":
        case "thurs":
        case "thursday":
            return (Day) retrieve(DAYS[3]);
        case "fri":
        case "friday":
            return (Day) retrieve(DAYS[4]);
        case "sat":
        case "saturday":
            return (Day) retrieve(DAYS[5]);
        case "sun":
        case "sunday":
            return (Day) retrieve(DAYS[6]);
        default:
            throw new Exceptions.InvalidInput("Not a valid day");
        }
    }

    /**
     * Executes all commands that start with the keyword "program".
     *
     * @param parser Parser containing user input
     * @return Message to user after executing the command
     * @throws Exceptions.InvalidInput if no command action specified
     * @throws Exceptions.ActivityDoesNotExists if user inputs name of an activity that does not exist
     * @throws Exceptions.ActivityExistsException if user assigns a workout to an occupied day
     */
    public String execute(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists,
            Exceptions.ActivityExistsException {
        assert parser != null : "Parser must not be null";
        String commandAction = parser.getAction();
        assert commandAction != null : "Command action must not be null";

        if (commandAction.isEmpty()) {
            throw new Exceptions.InvalidInput("No action specified");
        }
        
        String messageToUser;

        switch (commandAction) {
        case "assign":
            messageToUser = executeAssignAction(parser);
            break;
        case "clear":
            messageToUser = executeClearAction(parser);
            break;
        case "today":
            messageToUser = executeTodayAction();
            break;
        case "log":
            messageToUser = executeLogAction(parser);
            break;
        case "list":
            messageToUser = executeListAction();
            break;
        case "history":
            messageToUser = executeHistoryAction(parser);
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + parser.getAction());
        }

        return messageToUser;
    }

    private String executeListAction() {
        return getListString();
    }

    /**
     * Executes the command "program /assign {workout} /to {day}".
     *
     * @param parser Parser containing user input
     * @return Message to user after executing the command
     * @throws Exceptions.InvalidInput if user does not specify the day to assign the workout to
     * @throws Exceptions.ActivityDoesNotExists if user inputs name of a workout that does not exist
     * @throws Exceptions.ActivityExistsException if user assigns a workout to an occupied day
     */
    private String executeAssignAction(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists,
            Exceptions.ActivityExistsException {
        assert parser.getAction().equals("assign") : "Action must be assign";
        String day = parser.getAdditionalArguments("to");
        if (day == null || day.isEmpty()) {
            throw new Exceptions.InvalidInput("Week command not complete");
        }
        String workoutName = parser.getActionParameter();
        Activity workout = workoutManager.retrieve(workoutName);
        return assignWorkoutToDay(workout, day);
    }

    /**
     * Assigns a workout to a given day.
     *
     * @param workout Workout to be assigned
     * @param day The day the workout is to be assigned to
     * @return Message to user after executing the command
     * @throws Exceptions.InvalidInput if user inputs an invalid day string
     * @throws Exceptions.ActivityDoesNotExists if user inputs name of a workout that does not exist
     * @throws Exceptions.ActivityExistsException if user assigns a workout to an occupied day
     */
    public String assignWorkoutToDay(Activity workout, String day)
            throws Exceptions.InvalidInput, Exceptions.ActivityExistsException,
            Exceptions.ActivityDoesNotExists {
        Day selectedDay = getDay(day);
        Workout chosenDayWorkout = selectedDay.getAssignedWorkout();

        if (chosenDayWorkout != null) {
            throw new Exceptions.ActivityExistsException(
                    String.format("Workout %s is already assigned to %s. Please clear it first.",
                            chosenDayWorkout.getActivityName(), selectedDay.getActivityName()
                    )
            );
        }

        selectedDay.setAssignedWorkout((Workout) workout);

        return String.format("Workout %s assigned to %s", workout.getActivityName(), day);

    }

    private Day getDayFromDate(LocalDate date) throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        DayOfWeek dayFromDate = date.getDayOfWeek();
        return getDay(dayFromDate.toString());
    }

    private Day getDayFromDate(String dateString) throws Exceptions.ActivityDoesNotExists,
            Exceptions.InvalidInput, DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd");
        formatter = formatter.withLocale(formatter.getLocale());
        LocalDate date = LocalDate.parse(dateString, formatter);
        DayOfWeek dayFromDate = date.getDayOfWeek();
        return getDay(dayFromDate.toString());
    }

    private String executeLogAction(Parser parser)
            throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        if (!parser.hasAdditionalArguments() || parser.getAdditionalArgumentsLength() < 3) {
            throw new Exceptions.InvalidInput("log command not complete");
        }

        String exerciseName = parser.getActionParameter();
        String sets = parser.getAdditionalArguments("sets");
        String repetition = parser.getAdditionalArguments("reps");
        String weight = parser.getAdditionalArguments("weight");
        String workoutDate = parser.getAdditionalArguments("date");

        if (exerciseName.isBlank() || sets.isBlank() || repetition.isBlank() || weight.isBlank()) {
            throw new Exceptions.InvalidInput("log command not complete");
        }

        if (exerciseManager.doesNotHaveActivity(exerciseName)) {
            throw new Exceptions.ActivityDoesNotExists(
                    String.format("The exercise %s does not exist", exerciseName)
            );
        }

        Day selectedDay;
        if (workoutDate.isBlank()) {
            workoutDate = LocalDate.now().toString();
            selectedDay = getDayFromDate(workoutDate);
        } else {
            try {
                selectedDay = getDayFromDate(workoutDate);
            } catch (DateTimeParseException e) {
                throw new Exceptions.InvalidInput("The date must be in the format yyyy-mm-dd!");
            }
        }

        String workoutName = getWorkoutName(selectedDay, workoutDate);
        workoutLogsManager.addWorkoutLog(workoutDate, workoutName);
        workoutLogsManager.addExerciseLog(workoutDate, exerciseName, weight, sets, repetition);
        return String.format("Successfully logged %skg %s with %s sets and %s reps on %s",
                        weight, exerciseName, sets, repetition, workoutDate);
    }

    private static String getWorkoutName(Day selectedDay, String workoutDate) throws Exceptions.ActivityDoesNotExists {
        Workout assignedWorkout = selectedDay.getAssignedWorkout();
        if (assignedWorkout == null) {
            throw new Exceptions.ActivityDoesNotExists(
                    String.format(
                            "There does not seem to be a workout assigned to the date %s (day: %s). " +
                                    "Please assign one first!", workoutDate, selectedDay.getActivityName())
            );
        }

        return assignedWorkout.getActivityName();
    }

    private String executeTodayAction() throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        LocalDate currentDate = LocalDate.now();
        Day today = getDayFromDate(currentDate);
        Workout todaysWorkout = today.getAssignedWorkout();
        String todayDate = currentDate.toString();

        return getTodaysWorkoutString(todaysWorkout, todayDate, today);
    }

    private String getTodaysWorkoutString(Workout givenWorkout, String workoutDate, Day workoutDay) {
        try {
            if (givenWorkout == null) {
                throw new Exceptions.ActivityDoesNotExists(
                        String.format("There is no workout assigned today (%s)",
                                workoutDay.getActivityName())
                );
            }
            String workoutName = workoutDay.getAssignedWorkout().getActivityName();
            HashSet<Exercise> workoutHashSet = givenWorkout.getExerciseSet();
            workoutLogsManager.addWorkoutLog(workoutDate, workoutName);
            return workoutLogsManager.getWorkoutLogString(workoutDate, workoutHashSet);

        } catch (Exceptions.ActivityDoesNotExists e) {
            // catch so that it does not show error
            return e.getMessage();
        }
    }

    private String executeHistoryAction(Parser parser)
            throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        String parameter = parser.getActionParameter();
        if (parameter.isBlank()) {
            return getHistoryString();
        }

        String workoutDate = parser.getActionParameter();
        Workout retrievedWorkout = (Workout) workoutLogsManager.retrieve(workoutDate);
        Day day = getDayFromDate(workoutDate);
        return getTodaysWorkoutString(retrievedWorkout, workoutDate, day);
    }

    private String getHistoryString() {
        return workoutLogsManager.getListString();
    }

    private String executeClearAction(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        String day = parser.getActionParameter();
        if (day == null || day.isEmpty()) {
            activitySet.clear();
            initializeDays();
            return "All your workouts have been cleared from the week";
        }
        Day currentDay = getDay(day);
        String currentDayString = currentDay.getActivityName();

        activitySet.remove(getDay(day));
        Day newDay = new Day(currentDayString);
        newDay.setAssignedWorkout(null);
        activitySet.add(newDay);

        return String.format("Your workout on %s has been cleared", day);
    }


    public JSONObject exportToJSON() {
        JSONObject json = new JSONObject();
        try {
            for (String day : DAYS) {
                Day currentDay = getDay(day);
                Workout assignedWorkout = currentDay.getAssignedWorkout();
                String workoutName = "";

                if (assignedWorkout != null) {
                    workoutName = assignedWorkout.getActivityName();
                }

                json.put(day, workoutName);
            }
        } catch (Exceptions.InvalidInput | Exceptions.ActivityDoesNotExists ignored) {
            // should not get an exception as it is generated
        }

        return json;
    }

    @Override
    public String getActivityType(boolean plural) {
        return plural ? "weekly program" : "weekly programs";
    }

    @Override
    public String getListString() {
        StringBuilder message = new StringBuilder();
        message.append("Your workouts for the week:").append(System.lineSeparator());
        for (String day : DAYS) {
            try {
                Day dayObj = getDay(day);
                String dayString = dayObj.getActivityName();
                Workout dayWorkout = dayObj.getAssignedWorkout();
                message.append(String.format("\t%s: ", dayString));

                if (dayWorkout == null) {
                    message.append("Rest day").append(System.lineSeparator().repeat(2));
                } else {
                    message.append(dayWorkout.toString(1)).append(System.lineSeparator());
                }

            } catch (Exceptions.InvalidInput | Exceptions.ActivityDoesNotExists ignored) {
                // should not get an exception as it is generated
            }

        }
        return message.toString();
    }

}
