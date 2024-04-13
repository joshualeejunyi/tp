package byteceps.activities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ExerciseLogTest {

    private ExerciseLog exerciseLog;
    private List<Integer> weights;
    private List<Integer> repetitions;
    private final int sets = 3;
    private final String exerciseName = "Deadlift";

    @BeforeEach
    void setUp() {
        weights = Arrays.asList(100, 105, 110);
        repetitions = Arrays.asList(10, 8, 6);
        exerciseLog = new ExerciseLog(exerciseName, weights, sets, repetitions);
    }

    @Test
    public void getSets_whenCalled_returnsCorrectNumberOfSets() {
        ExerciseLog exerciseLog = new ExerciseLog("Bench Press", List.of(100, 100, 100), 3, List.of(10, 10, 10));
        assertEquals(3, exerciseLog.getSets());
    }

    @Test
    public void getWeights_whenCalled_returnsCorrectWeightsList() {
        assertEquals(weights, exerciseLog.getWeights());
    }

    @Test
    public void getRepetitions_whenCalled_returnsCorrectRepetitionsList() {
        assertEquals(repetitions, exerciseLog.getRepetitions());
    }


    @Test
    public void equals_withSameExerciseLog_returnsTrue() {
        ExerciseLog otherLog = new ExerciseLog(exerciseName, weights, sets, repetitions);
        assertEquals(otherLog, otherLog, "ExerciseLogs should be equal");
    }

    @Test
    public void equals_withDifferentWeights_returnsFalse() {
        List<Integer> differentWeights = Arrays.asList(90, 95, 100);
        ExerciseLog otherLog = new ExerciseLog(exerciseName, differentWeights, sets, repetitions);
        assertNotEquals(exerciseLog, otherLog);
    }

    @Test
    void equals_withDifferentRepetitions_returnsFalse() {
        List<Integer> differentRepetitions = Arrays.asList(12, 10, 8);
        ExerciseLog otherLog = new ExerciseLog(exerciseName, weights, sets, differentRepetitions);
        assertNotEquals(exerciseLog, otherLog);
    }

    @Test
    void equals_withDifferentSets_returnsFalse() {
        ExerciseLog otherLog = new ExerciseLog(exerciseName, weights, 4, repetitions);
        assertNotEquals(exerciseLog, otherLog);
    }

    @Test
    void equals_withDifferentExerciseName_returnsFalse() {
        ExerciseLog otherLog = new ExerciseLog("Squat", weights, sets, repetitions);
        assertNotEquals(exerciseLog, otherLog);
    }

    @Test
    void equals_withDifferentObject_returnsFalse() {
        assertNotEquals(exerciseLog, new Object());
    }

    @Test
    void equals_withNull_returnsFalse() {
        assertNotEquals(exerciseLog, null);
    }
}
