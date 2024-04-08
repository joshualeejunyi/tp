//@@author V4vern
package byteceps.activities;

import byteceps.processing.ActivityManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.ListIterator;

/**
 * Represents a workout plan in the byteceps application.
 * It extends the Activity class and contains a list of exercises.
 */
public class Workout extends Activity {
    /**
     * The list of exercises in this workout plan.
     */
    ArrayList<Exercise> exerciseList;

    /**
     * Constructs a new Workout object with the specified workout name.
     *
     * @param workoutName The name of the workout plan.
     */
    public Workout(String workoutName) {
        super(workoutName);
        exerciseList = new ArrayList<>();
    }

    /**
     * Returns the list of exercises in this workout plan.
     *
     * @return The list of exercises.
     */
    public ArrayList<Exercise> getExerciseList() {
        return exerciseList;
    }

    /**
     * Returns a set of exercises in this workout plan.
     *
     * @return A set of exercises.
     */
    public LinkedHashSet<Exercise> getExerciseSet() {
        return new LinkedHashSet<>(exerciseList);
    }

    /**
     * Adds an exercise to this workout plan.
     *
     * @param exercise The exercise to add.
     */
    public void addExercise(Exercise exercise) {
        exerciseList.add(exercise);
    }

    /**
     * Returns a string representation of this workout plan.
     * The representation includes the workout name and the list of exercises.
     *
     * @param numTabs The number of tabs for indentation.
     * @return A string representation of this workout plan.
     */
    public String toString(int numTabs) {
        assert numTabs > 0 : "numTabs cannot be negative";

        StringBuilder result = new StringBuilder();
        result.append("\t".repeat(numTabs)).append(activityName).append(System.lineSeparator());
        for (ListIterator<Exercise> it = exerciseList.listIterator(); it.hasNext(); ) {
            Activity currentExercise = it.next();
            result.append("\t".repeat(numTabs + 1));
            result.append(String.format("%d. %s%s",
                    it.nextIndex(), currentExercise.toString(), System.lineSeparator()));
        }
        return result.toString();
    }

    /**
     * Edits the name of the workout.
     *
     * @param newWorkoutName  The new name for the workout. If null, the workout name will be set to null.
     * @param activityManager The ActivityManager responsible for managing activities.
     * @throws NullPointerException If the activityManager is null.
     */
    public void editWorkoutName(String newWorkoutName, ActivityManager activityManager) {
        if (newWorkoutName != null) {
            activityManager.updateActivitySet(this, new Workout(newWorkoutName));
            this.activityName = newWorkoutName;
        } else {
            activityName = null;
        }
    }
}
