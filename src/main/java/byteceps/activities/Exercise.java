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
     * Modifies the name of the exercise. If the new name is not null, it updates the activity name
     * and updates the activity set in the provided activity manager with the new exercise name.
     * If the new name is null, it sets the activity name to null.
     *
     * @param activityManager The activity manager responsible for managing activities and updating activity sets.
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
