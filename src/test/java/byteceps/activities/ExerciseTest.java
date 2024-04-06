package byteceps.activities;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.processing.ActivityManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class ExerciseTest {

    private static class TestActivityManager extends ActivityManager {
        @Override
        public String execute(Parser parser) throws Exceptions.InvalidInput, Exceptions.ErrorAddingActivity, Exceptions.ActivityExistsException, Exceptions.ActivityDoesNotExists {
            return null;
        }

        @Override
        public String getActivityType(boolean plural) {
            return "Exercise";
        }
    }


    @Test
    public void toString_activityName_returnsActivityName() {
        String exerciseName = "Bench Press";
        Exercise exercise = new Exercise(exerciseName);

        assertEquals(exerciseName, exercise.toString());
    }


    @Test
    public void editExerciseName_validName_setNewName() {
        String initialName = "Push-ups";
        Exercise exercise = new Exercise(initialName);

        String newName = "Pull-ups";
        ActivityManager activityManager = new TestActivityManager();
        exercise.editExerciseName(newName,activityManager);
        assertEquals(newName, exercise.getActivityName());
    }

    @Test
    public void editExerciseName_emptyName_setEmptyName() {
        String initialName = "Squats";
        Exercise exercise = new Exercise(initialName);

        String newName = "";
        ActivityManager activityManager = new TestActivityManager();
        exercise.editExerciseName(newName,activityManager);
        assertEquals(newName, exercise.getActivityName());
    }

    @Test
    public void editExerciseName_nullName_setNull() {
        String initialName = "Deadlifts";
        Exercise exercise = new Exercise(initialName);

        String newName = null;
        ActivityManager activityManager = new TestActivityManager();
        exercise.editExerciseName(newName,activityManager);
        assertNull(exercise.getActivityName());
    }

    @Test
    public void editExerciseName_sameName_noChange() {
        ActivityManager activityManager = new TestActivityManager();
        Exercise exercise = new Exercise("Walking");
        exercise.editExerciseName("Walking", activityManager);
        assertEquals("Walking", exercise.getActivityName());
    }

}
