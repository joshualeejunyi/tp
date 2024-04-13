package byteceps.processing;

import byteceps.activities.Exercise;
import byteceps.activities.WorkoutLog;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.StorageStrings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;



class WorkoutLogsManagerTest {
    private WorkoutLogsManager workoutLogsManager;


    @BeforeEach
    void setUp() {
        workoutLogsManager = new WorkoutLogsManager();
    }


    @Test
    public void addWorkoutLog_nonDuplicate_success() throws Exceptions.ActivityDoesNotExists {
        assertDoesNotThrow(() -> workoutLogsManager.addWorkoutLog("2023-04-01",
                "Morning Fitness"));
        WorkoutLog log = (WorkoutLog) workoutLogsManager.retrieve("2023-04-01");
        assertNotNull(log);
        assertEquals("Morning Fitness", log.getWorkoutName());
    }

    @Test
    public void addWorkoutLog_duplicate_success() throws Exceptions.ActivityDoesNotExists {
        assertDoesNotThrow(() -> workoutLogsManager.addWorkoutLog("2023-04-01",
                "Morning Fitness"));
        assertDoesNotThrow(() -> workoutLogsManager.addWorkoutLog("2023-04-01",
                "Morning Fitness"));
        WorkoutLog log = (WorkoutLog) workoutLogsManager.retrieve("2023-04-01");
        assertNotNull(log);
        assertEquals("Morning Fitness", log.getWorkoutName());
    }

    @Test
    public void addExerciseLog_validInput_success() throws Exceptions.ActivityDoesNotExists {
        workoutLogsManager.addWorkoutLog("2023-04-01", "Leg Day");
        assertDoesNotThrow(() ->
                workoutLogsManager.addExerciseLog("2023-04-01", "Squats", "100 200",
                        "2", "10 10")
        );
        WorkoutLog log = (WorkoutLog) workoutLogsManager.retrieve("2023-04-01");
        assertFalse(log.getExerciseLogs().isEmpty());
    }

    @Test
    public void addExerciseLog_negativeWeight_exceptionThrown() {
        workoutLogsManager.addWorkoutLog("2023-04-01", "Leg Day");
        assertThrows(Exceptions.InvalidInput.class, () ->
                workoutLogsManager.addExerciseLog("2023-04-01", "Squats", "-100 200",
                        "2", "10 10")
        );
    }

    @Test
    public void addExerciseLog_negativeReps_exceptionThrown() {
        workoutLogsManager.addWorkoutLog("2023-04-01", "Leg Day");
        assertThrows(Exceptions.InvalidInput.class, () ->
                workoutLogsManager.addExerciseLog("2023-04-01", "Squats", "100 200",
                        "2", "-10 10")
        );
    }

    @Test
    public void addExerciseLog_negativeSets_exceptionThrown() {
        workoutLogsManager.addWorkoutLog("2023-04-01", "Leg Day");
        assertThrows(Exceptions.InvalidInput.class, () ->
                workoutLogsManager.addExerciseLog("2023-04-01", "Squats", "100 200",
                        "-2", "10 10")
        );
    }

    @Test
    public void addExerciseLog_nonExistentWorkoutLog_throwsActivityDoesNotExists() {
        assertThrows(Exceptions.ActivityDoesNotExists.class, () ->
                workoutLogsManager.addExerciseLog("2023-04-01", "Squats", "100 200",
                        "2", "10 10")
        );
    }

    @Test
    public void getWorkoutLog_nonExistentWorkoutLog_throwsActivityDoesNotExists() {
        assertThrows(Exceptions.ActivityDoesNotExists.class, () ->
                workoutLogsManager.getWorkoutLogString("2023-04-01", new LinkedHashSet<>())
        );
    }

    @Test
    public void getWorkoutLog_validInput_success() throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        workoutLogsManager.addWorkoutLog("2023-04-01", "Leg Day");
        workoutLogsManager.addExerciseLog("2023-04-01", "Squats", "100 200",
                "2", "10 10");
        String workoutLogString = workoutLogsManager.getWorkoutLogString("2023-04-01", new LinkedHashSet<>());
        assertTrue(workoutLogString.contains("Squats"));
    }

    @Test
    public void getWorkoutLogString_unloggedExercises_returnsCorrectlyAppendedUnloggedExercises() throws
            Exceptions.ActivityDoesNotExists {
        workoutLogsManager.addWorkoutLog("2023-04-01", "Leg Day");
        LinkedHashSet<Exercise> exercises = new LinkedHashSet<>();
        exercises.add(new Exercise("Squats"));
        exercises.add(new Exercise("Deadlifts"));
        String workoutLogString = workoutLogsManager.getWorkoutLogString("2023-04-01", exercises);
        assertTrue(workoutLogString.contains("Squats"));
        assertTrue(workoutLogString.contains("Deadlifts"));
    }

    @Test
    public void execute_invalidInput_throwsInvalidInput() {
        assertThrows(Exceptions.InvalidInput.class, () -> workoutLogsManager.execute(null));
    }


    @Test
    public void exportToJSON_validInput_success() throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        workoutLogsManager.addWorkoutLog("2023-04-01", "Leg Day");
        workoutLogsManager.addExerciseLog("2023-04-01", "Squats", "100 200", "2", "10 10");
        JSONArray jsonOutput = workoutLogsManager.exportToJSON();
        assertFalse(jsonOutput.isEmpty());
        JSONObject workout = jsonOutput.getJSONObject(0);
        assertEquals("2023-04-01", workout.getString(StorageStrings.WORKOUT_DATE));
        JSONArray exercises = workout.getJSONArray(StorageStrings.EXERCISES);
        assertEquals(1, exercises.length());
    }

}
