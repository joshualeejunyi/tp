package byteceps.activities;

import java.util.LinkedHashSet;


public class WorkoutLog extends Workout {
    protected final String workoutName;
    LinkedHashSet<ExerciseLog> exerciseLogs;
    public WorkoutLog(String workoutDate, String workoutName) {
        super(workoutDate);
        this.workoutName = workoutName;
        this.exerciseLogs = new LinkedHashSet<>();
    }

    public void addExerciseLog(ExerciseLog exerciseLog) {
        exerciseLogs.remove(exerciseLog);
        exerciseLogs.add(exerciseLog);
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public String getWorkoutDate() {
        return activityName;
    }

    public LinkedHashSet<ExerciseLog> getExerciseLogs() {
        return exerciseLogs;
    }
}
