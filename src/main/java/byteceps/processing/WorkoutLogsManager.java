package byteceps.processing;

import byteceps.activities.Activity;
import byteceps.activities.Exercise;
import byteceps.activities.ExerciseLog;
import byteceps.activities.WorkoutLog;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.ManagerStrings;
import byteceps.ui.strings.StorageStrings;
import byteceps.validators.WorkoutLogsValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class WorkoutLogsManager extends ActivityManager {
    public WorkoutLogsManager() {
    }

    @Override
    public String execute(Parser parser) throws Exceptions.InvalidInput {
        throw new Exceptions.InvalidInput(ManagerStrings.LOG_INVALID_STATE);
    }

    public void addWorkoutLog(String workoutLogDate, String workoutName) {
        WorkoutLog newWorkoutLog = new WorkoutLog(workoutLogDate, workoutName);
        try {
            add(newWorkoutLog);
        } catch (Exceptions.ActivityExistsException e) {
            // silently fail as duplicates are okay
        }
    }

    public void addExerciseLog(String workoutLogDate, String exerciseName,
                               String weight, String sets, String repetitions)
            throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        try {
            List<Integer> weightsList = Arrays.stream(weight.split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            int setsInt = Integer.parseInt(sets);
            List<Integer> repsList = Arrays.stream(repetitions.split(" "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            WorkoutLogsValidator.hasNegativeInput(weightsList, repsList, setsInt);

            ExerciseLog newExerciseLog = new ExerciseLog(exerciseName, weightsList, setsInt, repsList);
            WorkoutLog workoutLog = (WorkoutLog) retrieve(workoutLogDate);

            workoutLog.addExerciseLog(newExerciseLog);
        } catch (NumberFormatException e) {
            throw new Exceptions.InvalidInput(ManagerStrings.INVALID_REPS_SETS);
        } catch (Exceptions.ActivityDoesNotExists e) {
            throw new Exceptions.ActivityDoesNotExists(ManagerStrings.EXERCISE_NOT_IN_WORKOUT);
        }
    }

    public String getWorkoutLogString(String date, LinkedHashSet<Exercise> workoutLinkedHashSet)
            throws Exceptions.ActivityDoesNotExists {
        WorkoutLog retrievedWorkout = (WorkoutLog) retrieve(date);
        LinkedHashSet<ExerciseLog> exerciseLogs = retrievedWorkout.getExerciseLogs();
        LinkedHashSet<Exercise> tempSet = new LinkedHashSet<>(workoutLinkedHashSet);
        StringBuilder result = new StringBuilder();
        result.append(String.format(
                ManagerStrings.LOG_LIST, date));

        int index = 1;
        for (ExerciseLog currentExerciseLog : exerciseLogs) {
            String exerciseName = currentExerciseLog.getActivityName();
            int setCount = currentExerciseLog.getSets();
            List<Integer> repsList = currentExerciseLog.getRepetitions();
            List<Integer> weightsList = currentExerciseLog.getWeights();


            result.append(String.format("\t\t\t %d. %s\n", index, exerciseName));
            for (int setIndex = 0; setIndex < setCount; setIndex++) {
                int weight = weightsList.get(setIndex);
                int reps = repsList.get(setIndex);
                result.append(String.format(ManagerStrings.LOG_LIST_ITEM, setIndex + 1, weight, reps));
            }

            tempSet.removeIf(p -> p.getActivityName().equals(exerciseName));
            index++;
        }

        for (Exercise currentExercise : tempSet) {
            String exerciseName = currentExercise.getActivityName();
            result.append(String.format(ManagerStrings.ACTIVITY_LIST_ITEM, index, exerciseName));
            index++;
        }

        return result.toString();
    }

    @Override
    public String getActivityType(boolean plural) {
        return plural ? ManagerStrings.WORKOUT_LOGS : ManagerStrings.WORKOUT_LOG;
    }

    public JSONArray exportToJSON() {
        ArrayList<Activity> workoutLogs = getActivityList();
        JSONArray workouts = new JSONArray();
        for (Activity currentActivity : workoutLogs) {
            WorkoutLog currentWorkout = (WorkoutLog) currentActivity;
            String workoutDate = currentWorkout.getWorkoutDate();
            String workoutName = currentWorkout.getWorkoutName();

            LinkedHashSet<ExerciseLog> exercises = currentWorkout.getExerciseLogs();
            JSONObject workoutJson = getWorkoutJson(exercises, workoutName, workoutDate);

            workouts.put(workoutJson);
        }
        return workouts;
    }

    private static JSONObject getWorkoutJson(LinkedHashSet<ExerciseLog> exercises,
                                             String workoutName, String workoutDate) {
        JSONArray workoutExercises = new JSONArray();
        for (ExerciseLog currentExercise : exercises) {
            JSONObject exercise = new JSONObject();
            String exerciseName = currentExercise.getActivityName();

            exercise.put(StorageStrings.EXERCISE_NAME, exerciseName);
            exercise.put(StorageStrings.WEIGHT, currentExercise.getWeights());
            exercise.put(StorageStrings.SETS, currentExercise.getSets());
            exercise.put(StorageStrings.REPS, currentExercise.getRepetitions());

            workoutExercises.put(exercise);
        }

        JSONObject workoutJson = new JSONObject();
        workoutJson.put(StorageStrings.WORKOUT_DATE, workoutDate);
        workoutJson.put(StorageStrings.WORKOUT_NAME, workoutName);
        workoutJson.put(StorageStrings.EXERCISES, workoutExercises);
        return workoutJson;
    }

}
