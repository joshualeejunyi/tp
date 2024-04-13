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
        assertTrue(workoutLog.getExerciseLogs().contains(exerciseLog1));
        assertTrue(workoutLog.getExerciseLogs().contains(exerciseLog2));
        assertEquals(2, workoutLog.getExerciseLogs().size());
    }

    @Test
    void addExerciseLog_addingDuplicateLogs_onlyUniqueLogsStored() {
        ExerciseLog exerciseLog = new ExerciseLog("Squat", List.of(100, 105), 2, List.of(10, 8));
        workoutLog.addExerciseLog(exerciseLog);
        workoutLog.addExerciseLog(exerciseLog);
        assertEquals(1, workoutLog.getExerciseLogs().size());
    }

    @Test
    void getWorkoutName_workoutName_workoutNameReturned() {
        assertEquals("Leg Day", workoutLog.getWorkoutName());
    }


    @Test
    void getWorkoutDate_whenCalled_returnsCorrectWorkoutDate() {
        assertEquals("2024-03-28", workoutLog.getWorkoutDate());
    }

    @Test
    void getExerciseLogs_whenCalled_returnsExerciseLogs() {
        ExerciseLog exerciseLog = new ExerciseLog("Squat", List.of(100), 1, List.of(10));
        workoutLog.addExerciseLog(exerciseLog);

        LinkedHashSet<ExerciseLog> expectedLogs = new LinkedHashSet<>();
        expectedLogs.add(exerciseLog);

        assertEquals(expectedLogs, workoutLog.getExerciseLogs());
    }

    @Test
    void getExerciseLogs_noLogsAdded_returnsEmptySet() {
        assertTrue(workoutLog.getExerciseLogs().isEmpty());
    }
}
