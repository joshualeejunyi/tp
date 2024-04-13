package byteceps.activities;

import byteceps.processing.ActivityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkoutTest {

    private Workout workout;
    private TestActivityManager testManager;

    @BeforeEach
    void setUp() {
        workout = new Workout("InitialName");
        testManager = new TestActivityManager();
    }

    @Nested
    class WorkoutToStringTests {
        @Test
        void testToStringWithNoExercises() {
            Workout emptyWorkout = new Workout("Empty Routine");
            String expected = "\tEmpty Routine" + System.lineSeparator();  // Using system-dependent newline
            assertEquals(expected, emptyWorkout.toString(1));
        }

        @Test
        void testToStringWithExercises() {
            workout.addExercise(new Exercise("Push-ups"));
            workout.addExercise(new Exercise("Sit-ups"));
            String expected = "\tInitialName" + System.lineSeparator() +
                    "\t\t1. Push-ups" + System.lineSeparator() +
                    "\t\t2. Sit-ups" + System.lineSeparator();
            assertEquals(expected, workout.toString(1));
        }

        @Test
        void testToStringWithMultipleTabs() {
            workout.addExercise(new Exercise("Push-ups"));
            workout.addExercise(new Exercise("Sit-ups"));
            String expected = "\t\t\tInitialName" + System.lineSeparator() +
                    "\t\t\t\t1. Push-ups" + System.lineSeparator() +
                    "\t\t\t\t2. Sit-ups" + System.lineSeparator();
            assertEquals(expected, workout.toString(3));
        }
    }

    @Nested
    public class TestActivityManager extends ActivityManager {
        public LinkedHashSet<Activity> updates = new LinkedHashSet<>();

        @Override
        public String execute(byteceps.commands.Parser parser) {
            return null;
        }

        @Override
        public String getActivityType(boolean plural) {
            return plural ? "workouts" : "workout";
        }

        @Override
        public void updateActivitySet(Activity activityToRemove, Activity activityToAdd) {
            super.updateActivitySet(activityToRemove, activityToAdd);
            updates.add(activityToAdd);
        }

        @BeforeEach
        void setUp() {
            workout = new Workout("InitialName");
            testManager = new TestActivityManager();
        }

        @Test
        public void editWorkoutName_workoutWithInitialName_success() {
            workout.editWorkoutName("NewName", testManager);
            assertEquals("NewName", workout.getActivityName());
        }

        @Test
        public void editWorkoutName_emptyStringName_success() {
            workout.editWorkoutName("", testManager);
            assertEquals("", workout.getActivityName());
            assertTrue(testManager.updates.contains(new Workout("")));
        }

        @Test
        public void editWorkoutName_multipleValidChanges_success() {
            String[] names = {"Cardio Blast", "Strength Training", "Endurance"};
            for (String name : names) {
                workout.editWorkoutName(name, testManager);
                assertEquals(name, workout.getActivityName());
                assertTrue(testManager.updates.contains(new Workout(name)));
            }
        }

        @Test
        public void editWorkoutName_nullName_setsNameToNull() {
            workout.editWorkoutName(null, testManager);
            assertNull(workout.getActivityName());
        }


        @Test
        public void addExercise_addSingleExerciseToWorkout_success() {
            Workout workout = new Workout("Push Day");
            Exercise exercise = new Exercise("Bench Press");

            workout.addExercise(exercise);

            ArrayList<Exercise> expectedList = new ArrayList<>();
            expectedList.add(exercise);
            assertEquals(expectedList, workout.getExerciseList());
        }

        @Test
        public void addExercise_addMultipleExercisesToWorkout_success() {
            Workout workout = new Workout("Push Day");

            Exercise exercise1 = new Exercise("Bench Press");
            Exercise exercise2 = new Exercise("Overhead Press");
            Exercise exercise3 = new Exercise("Chest Fly");


            workout.addExercise(exercise1);
            workout.addExercise(exercise2);
            workout.addExercise(exercise3);


            ArrayList<Exercise> expectedList = new ArrayList<>();
            expectedList.add(exercise1);
            expectedList.add(exercise2);
            expectedList.add(exercise3);
            assertEquals(expectedList, workout.getExerciseList());
        }

        @Test
        public void getWorkoutList_workoutWithNoExercises_returnsEmptyList() {
            Workout workout = new Workout("Push Day");
            assertTrue(workout.getExerciseList().isEmpty());
        }

        @Test
        public void getWorkoutList_workoutWithExercises_returnsListOfExercises() {
            Workout workout = new Workout("Push Day");
            Exercise exercise1 = new Exercise("Bench Press");
            Exercise exercise2 = new Exercise("Overhead Press");
            workout.addExercise(exercise1);
            workout.addExercise(exercise2);

            ArrayList<Exercise> expectedList = new ArrayList<>();
            expectedList.add(exercise1);
            expectedList.add(exercise2);
            assertEquals(expectedList, workout.getExerciseList());
        }

    }
}

