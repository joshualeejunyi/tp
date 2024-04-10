package byteceps.processing;

import byteceps.activities.Day;
import byteceps.activities.Workout;
import byteceps.activities.Exercise;
import byteceps.activities.WorkoutLog;
import byteceps.activities.Activity;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.DayStrings;
import byteceps.ui.strings.ManagerStrings;
import byteceps.validators.WeeklyProgramValidator;

import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class WeeklyProgramManager extends ActivityManager {
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

    private static String getWorkoutName(Day selectedDay, String workoutDate) throws Exceptions.ActivityDoesNotExists {
        Workout assignedWorkout = selectedDay.getAssignedWorkout();
        if (assignedWorkout == null) {
            throw new Exceptions.ActivityDoesNotExists(
                    String.format(ManagerStrings.NO_WORKOUT_ASSIGNED, workoutDate, selectedDay.getActivityName())
            );
        }
        return assignedWorkout.getActivityName();
    }

    private void initializeDays() {
        for (String day : DayStrings.DAYS) {
            Day newDay = new Day(day);
            newDay.setAssignedWorkout(null);
            activitySet.add(newDay);
        }
    }

    public Day getDay(String day) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        switch (day.toLowerCase()) {
        case DayStrings.DAY_MON:
        case DayStrings.DAY_MONDAY:
            return (Day) retrieve(DayStrings.DAYS[0]);
        case DayStrings.DAY_TUE:
        case DayStrings.DAY_TUES:
        case DayStrings.DAY_TUESDAY:
            return (Day) retrieve(DayStrings.DAYS[1]);
        case DayStrings.DAY_WED:
        case DayStrings.DAY_WEDNESDAY:
            return (Day) retrieve(DayStrings.DAYS[2]);
        case DayStrings.DAY_THU:
        case DayStrings.DAY_THURS:
        case DayStrings.DAY_THURSDAY:
            return (Day) retrieve(DayStrings.DAYS[3]);
        case DayStrings.DAY_FRI:
        case DayStrings.DAY_FRIDAY:
            return (Day) retrieve(DayStrings.DAYS[4]);
        case DayStrings.DAY_SAT:
        case DayStrings.DAY_SATURDAY:
            return (Day) retrieve(DayStrings.DAYS[5]);
        case DayStrings.DAY_SUN:
        case DayStrings.DAY_SUNDAY:
            return (Day) retrieve(DayStrings.DAYS[6]);
        default:
            throw new Exceptions.InvalidInput(DayStrings.INVALID_DAY);
        }
    }

    /**
     * Executes all commands that start with the keyword "program".
     *
     * @param parser Parser containing user input
     * @return Message to user after executing the command
     * @throws Exceptions.InvalidInput            if no command action specified
     * @throws Exceptions.ActivityDoesNotExists   if user inputs name of an activity that does not exist
     * @throws Exceptions.ActivityExistsException if user assigns a workout to an occupied day
     */
    public String execute(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists,
            Exceptions.ActivityExistsException {

        String commandAction = WeeklyProgramValidator.validateCommand(parser);
        String messageToUser;

        switch (commandAction) {
        case CommandStrings.ACTION_ASSIGN:
            messageToUser = executeAssignAction(parser);
            break;
        case CommandStrings.ACTION_CLEAR:
            messageToUser = executeClearAction(parser);
            break;
        case CommandStrings.ACTION_TODAY:
            messageToUser = executeTodayAction();
            break;
        case CommandStrings.ACTION_LOG:
            messageToUser = executeLogAction(parser);
            break;
        case CommandStrings.ACTION_LIST:
            messageToUser = executeListAction();
            break;
        case CommandStrings.ACTION_HISTORY:
            messageToUser = executeHistoryAction(parser);
            break;
        default:
            throw new IllegalStateException(String.format(ManagerStrings.UNEXPECTED_ACTION, parser.getAction()));
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
     * @throws Exceptions.InvalidInput            if user does not specify the day to assign the workout to
     * @throws Exceptions.ActivityDoesNotExists   if user inputs name of a workout that does not exist
     * @throws Exceptions.ActivityExistsException if user assigns a workout to an occupied day
     */
    private String executeAssignAction(Parser parser) throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput,
            Exceptions.ActivityExistsException {
        String day = parser.getAdditionalArguments(CommandStrings.ARG_TO);
        String workoutName = parser.getActionParameter();
        Activity workout = workoutManager.retrieve(workoutName);
        return assignWorkoutToDay(workout, day);
    }

    /**
     * Assigns a workout to a given day.
     *
     * @param workout Workout to be assigned
     * @param day     The day the workout is to be assigned to
     * @return Message to user after executing the command
     * @throws Exceptions.ActivityDoesNotExists   if user inputs name of a workout that does not exist
     * @throws Exceptions.ActivityExistsException if user assigns a workout to an occupied day
     */
    public String assignWorkoutToDay(Activity workout, String day)
            throws Exceptions.ActivityExistsException, Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        Day selectedDay = getDay(day);
        Workout chosenDayWorkout = selectedDay.getAssignedWorkout();

        if (chosenDayWorkout != null) {
            throw new Exceptions.ActivityExistsException(
                    String.format(ManagerStrings.WORKOUT_ALREADY_ASSIGNED,
                            chosenDayWorkout.getActivityName(), selectedDay.getActivityName()
                    )
            );
        }
        selectedDay.setAssignedWorkout((Workout) workout);
        return String.format(ManagerStrings.WORKOUT_ASSIGNED, workout.getActivityName(), day);
    }

    private Day getDayFromDate(LocalDate date) throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        DayOfWeek dayFromDate = date.getDayOfWeek();
        return getDay(dayFromDate.toString());
    }

    private Day getDayFromDate(String dateString)
            throws Exceptions.ActivityDoesNotExists, DateTimeParseException, Exceptions.InvalidInput {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DayStrings.YEAR_FORMAT);
        formatter = formatter.withLocale(formatter.getLocale());
        LocalDate date = LocalDate.parse(dateString, formatter);
        DayOfWeek dayFromDate = date.getDayOfWeek();
        return getDay(dayFromDate.toString());
    }

    private String executeLogAction(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {

        String exerciseName = parser.getActionParameter();
        String sets = parser.getAdditionalArguments(CommandStrings.ARG_SETS);
        String repetition = parser.getAdditionalArguments(CommandStrings.ARG_REPS);
        String weight = parser.getAdditionalArguments(CommandStrings.ARG_WEIGHT);

        String workoutDate = parser.getAdditionalArguments(CommandStrings.ARG_DATE);

        if (exerciseManager.doesNotHaveActivity(exerciseName)) {
            throw new Exceptions.ActivityDoesNotExists(
                    String.format(ManagerStrings.ACTIVITY_DOES_NOT_EXIST_EXCEPTION,
                            CommandStrings.COMMAND_EXERCISE, exerciseName)
            );
        }

        workoutDate = formatDateString(workoutDate);
        Day selectedDay = getDayFromDate(workoutDate);

        String workoutName = getWorkoutName(selectedDay, workoutDate);
        workoutLogsManager.addWorkoutLog(workoutDate, workoutName);
        workoutLogsManager.addExerciseLog(workoutDate, exerciseName, weight, sets, repetition);

        int setsInt = Integer.parseInt(sets);
        List<String> weightList = Arrays.asList(weight.split(" "));
        List<Integer> repetitionList = Arrays.stream(repetition.split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        String formattedWeights = weightList.stream()
                .map(w -> w + "kg")
                .collect(Collectors.joining(", "));

        String formattedReps = repetitionList.size() == 1 ? repetitionList.get(0).toString() :
                repetitionList.stream().map(String::valueOf).collect(Collectors.joining(", "));

        String setWord = setsInt == 1 ? "set" : "sets";
        String weightWord = weightList.size() == 1 ? "weight of" : "weights of";
        String repWord = (repetitionList.size() == 1 && repetitionList.get(0) == 1) ? "rep" : "reps";



        return String.format(ManagerStrings.LOG_SUCCESS,
                exerciseName, weightWord, formattedWeights, formattedReps, repWord, setsInt, setWord, workoutDate);
    }

    private static String formatDateString(String workoutDate) throws Exceptions.ActivityDoesNotExists {
        if (workoutDate == null || workoutDate.isEmpty()) {
            workoutDate = LocalDate.now().toString();
        } else {
            LocalDate currentDate = LocalDate.now();
            LocalDate inputDate = LocalDate.parse(workoutDate);
            if (inputDate.isAfter(currentDate)) {
                throw new Exceptions.ActivityDoesNotExists(DayStrings.FUTURE_DATE);
            }
        }
        return workoutDate;
    }

    private String executeTodayAction() throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        LocalDate currentDate = LocalDate.now();
        Day today = getDayFromDate(currentDate);
        Workout todaysWorkout = today.getAssignedWorkout();
        String todayDate = currentDate.toString();

        return getTodaysWorkoutString(todaysWorkout, todayDate, today);
    }

    private String getTodaysWorkoutString(Workout givenWorkout, String workoutDate, Day workoutDay)
            throws Exceptions.ActivityDoesNotExists {
        String workoutName = workoutDay.getAssignedWorkout().getActivityName();
        LinkedHashSet<Exercise> workoutLinkedHashSet = givenWorkout.getExerciseSet();
        workoutLogsManager.addWorkoutLog(workoutDate, workoutName);
        return workoutLogsManager.getWorkoutLogString(workoutDate, workoutLinkedHashSet);
    }

    private String executeHistoryAction(Parser parser)
            throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        String parameter = parser.getActionParameter();
        if (parameter.isBlank()) {
            return getHistoryString();
        }

        String workoutDate = parser.getActionParameter();
        WorkoutLog retrievedWorkout = (WorkoutLog) workoutLogsManager.retrieve(workoutDate);

        try {
            Day day = getDayFromDate(workoutDate);
            return getTodaysWorkoutString(retrievedWorkout, workoutDate, day);
        } catch (DateTimeParseException e) {
            throw new Exceptions.InvalidInput(ManagerStrings.INVALID_DATE_ENTERED);
        }
    }

    private String getHistoryString() {
        return workoutLogsManager.getListString();
    }

    public LinkedHashSet<Activity> getDaySet() {
        return activitySet;
    }

    private String executeClearAction(Parser parser) throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        String day = parser.getActionParameter();
        if (day == null || day.isEmpty()) {
            activitySet.clear();
            initializeDays();
            return ManagerStrings.PROGRAMS_CLEARED;
        }
        Day currentDay = getDay(day);
        String currentDayString = currentDay.getActivityName();

        activitySet.remove(getDay(day));
        Day newDay = new Day(currentDayString);
        newDay.setAssignedWorkout(null);
        activitySet.add(newDay);

        return String.format(ManagerStrings.WORKOUT_CLEARED, day);
    }


    public JSONObject exportToJSON() {
        JSONObject json = new JSONObject();
        try {
            for (String day : DayStrings.DAYS) {
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
        return ManagerStrings.WEEKLY_PROGRAM;
    }

    @Override
    public String getListString() {
        StringBuilder message = new StringBuilder();
        message.append(ManagerStrings.PROGRAM_LIST).append(System.lineSeparator());
        for (String day : DayStrings.DAYS) {
            try {
                Day dayObj = getDay(day);
                String dayString = dayObj.getActivityName();
                Workout dayWorkout = dayObj.getAssignedWorkout();
                message.append(String.format(ManagerStrings.PROGRAM_LIST_ITEM, dayString));

                if (dayWorkout == null) {
                    message.append(DayStrings.REST_DAY).append(System.lineSeparator().repeat(2));
                } else {
                    message.append(dayWorkout.toString(1)).append(System.lineSeparator());
                }

            } catch (Exceptions.ActivityDoesNotExists | Exceptions.InvalidInput ignored) {
                // should not get an exception as it is generated
            }

        }
        return message.toString();
    }

}
