package byteceps.activities;

import byteceps.processing.ActivityManager;

/**
 * Represents an exercise activity in the byteceps application.
 * Each Exercise object corresponds to a specific exercise with a name.
 */
public class Exercise extends Activity {
    /**
     * Constructs a new Exercise object with the specified exercise name.
     *
     * @param exerciseName The name of the exercise.
     */
    public Exercise(String exerciseName) {
        super(exerciseName);
    }

    /**
     * Returns a string representation of the Exercise object.
     *
     * @return The name of the exercise.
     */
    public String toString() {
        return super.getActivityName();
    }

    /**
     * Modifies the name of the exercise.
     *
     * @param newExerciseName The new name for the exercise.
     */
    public void editExerciseName(String newExerciseName, ActivityManager activityManager) {
        if (newExerciseName != null) {
            activityManager.updateActivitySet(this, new Exercise(newExerciseName));
            this.activityName = newExerciseName;
        } else {
            activityName = null;
        }
    }
}
