package byteceps.storage;

import byteceps.activities.Day;
import byteceps.activities.Exercise;
import byteceps.activities.Workout;
import byteceps.errors.Exceptions;
import byteceps.processing.ExerciseManager;
import byteceps.processing.WorkoutLogsManager;
import byteceps.processing.WeeklyProgramManager;
import byteceps.processing.WorkoutManager;
import byteceps.ui.strings.DayStrings;
import byteceps.ui.strings.StorageStrings;
import byteceps.ui.UserInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Storage {
    private final Path filePath;
    private final UserInterface ui;
    public Storage(String filePath, UserInterface ui) {
        this.filePath = Path.of(filePath);
        this.ui = ui;
    }

    public void save(ExerciseManager allExercises, WorkoutManager allWorkouts,
                     WeeklyProgramManager weeklyProgram, WorkoutLogsManager workoutLogsManager)
            throws IOException {
        JSONObject jsonArchive = new JSONObject().put(
                StorageStrings.EXERCISE_MANAGER, allExercises.getActivityList().toArray());
        jsonArchive.put(StorageStrings.WORKOUT_MANAGER, allWorkouts.getActivityList().toArray());
        jsonArchive.put(StorageStrings.WEEKLY_PROGRAM, weeklyProgram.exportToJSON());
        jsonArchive.put(StorageStrings.WORKOUT_LOG_MANAGER, workoutLogsManager.exportToJSON());

        FileWriter fileWriter = new FileWriter(filePath.toFile());
        fileWriter.write(jsonArchive.toString());
        fileWriter.close();

        ui.printMessage(StorageStrings.WORKOUTS_SAVED);
    }

    public void load(ExerciseManager allExercises, WorkoutManager allWorkouts,
                     WeeklyProgramManager weeklyProgram, WorkoutLogsManager workoutLogsManager)
            throws IOException {
        boolean exerciseManagerIsEmpty = allExercises.getActivityList().isEmpty();
        boolean workoutManagerIsEmpty =  allWorkouts.getActivityList().isEmpty();
        boolean weeklyProgramIsEmpty = weeklyProgram.getActivityList().stream().
                allMatch(day-> ((Day) day).getAssignedWorkout() == null);
        assert exerciseManagerIsEmpty && workoutManagerIsEmpty && weeklyProgramIsEmpty
            : "Must load from a clean state";
        File jsonFile = filePath.toFile();

        if (jsonFile.createNewFile()) {
            ui.printMessage(StorageStrings.NO_SAVE_DATA);
            return;
        }

        ui.printMessage(StorageStrings.LOADING);

        try (Scanner jsonScanner = new Scanner(jsonFile)) {
            JSONObject jsonArchive = new JSONObject(jsonScanner.nextLine());
            loadExercises(allExercises, jsonArchive);
            loadWorkouts(allExercises, allWorkouts, jsonArchive);
            loadWeeklyProgram(allWorkouts, weeklyProgram, jsonArchive);
            loadWorkoutLogs(allExercises, allWorkouts, jsonArchive, workoutLogsManager);
            ui.printMessage(StorageStrings.LOAD_SUCCESS);
        } catch (Exceptions.ActivityExistsException | Exceptions.ErrorAddingActivity |
             Exceptions.ActivityDoesNotExists | Exceptions.InvalidInput | JSONException | NoSuchElementException e) {
            ui.printMessage(StorageStrings.LOAD_ERROR);
            try {
                String timestamp = new SimpleDateFormat(StorageStrings.BACKUP_DATE_FORMAT)
                        .format(new Date());
                File oldFile = new File(jsonFile.getParent(),
                        jsonFile.getName() + StorageStrings.OLD_SUFFIX + timestamp);
                jsonFile.renameTo(oldFile);
                jsonFile.createNewFile();
            } catch (IOException ex) {
                ui.printMessage(StorageStrings.NEW_JSON_ERROR);
            }

            allExercises = new ExerciseManager();
            allWorkouts = new WorkoutManager(allExercises);
            weeklyProgram = new WeeklyProgramManager(allExercises, allWorkouts, workoutLogsManager);
            workoutLogsManager = new WorkoutLogsManager();
        }

    }

    private static void loadWeeklyProgram(WorkoutManager allWorkouts, WeeklyProgramManager weeklyProgram,
        JSONObject jsonArchive)
            throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput, Exceptions.ActivityExistsException {
        JSONObject jsonWeeklyProgram = jsonArchive.getJSONObject(StorageStrings.WEEKLY_PROGRAM);

        assert jsonWeeklyProgram.length() == 7 : "Weekly program array must be length 7";
        for (Iterator<String> it = jsonWeeklyProgram.keys(); it.hasNext(); ) {
            String day = it.next();
            String workout = (String) jsonWeeklyProgram.get(day);
            if (!workout.isBlank()) {
                Workout dayWorkout = (Workout) allWorkouts.retrieve(workout);
                weeklyProgram.assignWorkoutToDay(dayWorkout, day);
            }
        }
    }

    private static void loadWorkouts(ExerciseManager allExercises, WorkoutManager allWorkouts, JSONObject jsonArchive)
            throws Exceptions.ActivityExistsException, Exceptions.ErrorAddingActivity,
            Exceptions.ActivityDoesNotExists {
        JSONArray jsonWorkoutArray = jsonArchive.getJSONArray(StorageStrings.WORKOUT_MANAGER);
        for (int i = 0; i < jsonWorkoutArray.length(); i++) {
            JSONObject jsonWorkout = jsonWorkoutArray.getJSONObject(i);
            String workoutName = jsonWorkout.getString(StorageStrings.ACTIVITY_NAME);
            Workout workout = new Workout(workoutName);
            allWorkouts.add(workout);
            JSONArray jsonExercisesInWorkout = jsonWorkout.getJSONArray(
                    StorageStrings.EXERCISE_LIST);
            for (int j = 0; j < jsonExercisesInWorkout.length(); j++) {
                String exerciseInWorkout = jsonExercisesInWorkout.getJSONObject(j)
                        .getString(StorageStrings.ACTIVITY_NAME);
                workout.addExercise((Exercise) allExercises.retrieve(exerciseInWorkout));
            }
        }
    }

    private static void loadExercises(ExerciseManager allExercises, JSONObject jsonArchive)
            throws Exceptions.ActivityExistsException, Exceptions.ErrorAddingActivity {
        JSONArray jsonExerciseArray = jsonArchive.getJSONArray(StorageStrings.EXERCISE_MANAGER);
        for (int i = 0; i < jsonExerciseArray.length(); i++) {
            String exerciseName = jsonExerciseArray.getJSONObject(i).getString(StorageStrings.ACTIVITY_NAME);
            allExercises.add(new Exercise(exerciseName));
        }
    }

    private void loadWorkoutLogs(ExerciseManager allExercises, WorkoutManager allWorkouts,
                                 JSONObject jsonArchive, WorkoutLogsManager workoutLogsManager)
            throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        JSONArray jsonWorkoutLogs = jsonArchive.getJSONArray(StorageStrings.WORKOUT_LOG_MANAGER);
        for (int i = 0; i < jsonWorkoutLogs.length(); i++) {
            JSONObject currentWorkout = jsonWorkoutLogs.getJSONObject(i);
            JSONArray exercisesArray = currentWorkout.getJSONArray(StorageStrings.EXERCISES);
            String workoutDate = currentWorkout.getString(StorageStrings.WORKOUT_DATE);
            String workoutName = currentWorkout.getString(StorageStrings.WORKOUT_NAME);
            //validate that workoutDate is a valid date string
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DayStrings.YEAR_FORMAT);
                formatter = formatter.withLocale(formatter.getLocale());
                LocalDate.parse(workoutDate, formatter); //ignore result, just catch exception
            } catch (DateTimeParseException e) {
                throw new Exceptions.InvalidInput(""); //no need for error message, LOAD_ERROR will be printed
            }
            workoutLogsManager.addWorkoutLog(workoutDate, workoutName);

            for (int j = 0; j < exercisesArray.length(); j++) {
                JSONObject currentExercise = exercisesArray.getJSONObject(j);
                String exerciseName = currentExercise.getString(StorageStrings.EXERCISE_NAME);
                String weight = String.valueOf(currentExercise.getInt(StorageStrings.WEIGHT));
                String sets = String.valueOf(currentExercise.getInt((StorageStrings.SETS)));
                String reps = String.valueOf(currentExercise.getInt((StorageStrings.REPS)));
                workoutLogsManager.addExerciseLog(workoutDate, exerciseName,
                        weight, sets, reps);
            }
        }

    }


}
