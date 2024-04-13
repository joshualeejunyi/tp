package byteceps.activities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkoutLogTest {
    private WorkoutLog workoutLog;

    @BeforeEach
    void setUp() {
        workoutLog = new WorkoutLog("2024-03-28", "Leg Day");
    }

    @Test
    void addExerciseLog_addingExerciseLogs_exerciseLogsUpdated() {
        ExerciseLog exerciseLog1 = new ExerciseLog("Squat", List.of(100, 105), 2, List.of(10, 8));
        ExerciseLog exerciseLog2 = new ExerciseLog("Deadlift", List.of(120, 125), 2, List.of(5, 5));
        workoutLog.addExerciseLog(exerciseLog1);
        workoutLog.addExerciseLog(exerciseLog2);
        assertTrue(workoutLog.getExerciseLogs().contains(exerciseLog1), "Exercise logs should contain the first added exercise log.");
        assertTrue(workoutLog.getExerciseLogs().contains(exerciseLog2), "Exercise logs should contain the second added exercise log.");
        assertEquals(2, workoutLog.getExerciseLogs().size(), "There should be exactly two exercise logs in the set.");
    }

    @Test
    void addExerciseLog_addingDuplicateLogs_onlyUniqueLogsStored() {
        ExerciseLog exerciseLog = new ExerciseLog("Squat", List.of(100, 105), 2, List.of(10, 8));
        workoutLog.addExerciseLog(exerciseLog);
        workoutLog.addExerciseLog(exerciseLog);
        assertEquals(1, workoutLog.getExerciseLogs().size(), "Duplicate logs should not be added to the set.");
    }

    @Test
    void getWorkoutName_workoutName_workoutNameReturned() {
        assertEquals("Leg Day", workoutLog.getWorkoutName(), "Workout name should be 'Leg Day'.");
    }


    @Test
    void getWorkoutDate_whenCalled_returnsCorrectWorkoutDate() {
        assertEquals("2024-03-28", workoutLog.getWorkoutDate(), "Should return the correct workout date.");
    }

    @Test
    void getExerciseLogs_whenCalled_returnsExerciseLogs() {
        ExerciseLog exerciseLog = new ExerciseLog("Squat", List.of(100), 1, List.of(10));
        workoutLog.addExerciseLog(exerciseLog);

        LinkedHashSet<ExerciseLog> expectedLogs = new LinkedHashSet<>();
        expectedLogs.add(exerciseLog);

        assertEquals(expectedLogs, workoutLog.getExerciseLogs(), "Should return the set containing all added exercise logs.");
    }

    @Test
    void getExerciseLogs_noLogsAdded_returnsEmptySet() {
        assertTrue(workoutLog.getExerciseLogs().isEmpty(), "Should return an empty set when no logs are added.");
    }
}