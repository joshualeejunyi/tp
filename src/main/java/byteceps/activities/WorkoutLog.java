package byteceps.activities;

import byteceps.errors.Exceptions;
import byteceps.ui.strings.ManagerStrings;

import java.util.Iterator;
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

    public boolean hasExerciseName(String exerciseName) {
        for (Iterator<ExerciseLog> iterator = exerciseLogs.iterator(); iterator.hasNext(); ) {
            ExerciseLog currentExerciseLog = iterator.next();
            if (currentExerciseLog.activityName.equalsIgnoreCase(exerciseName)) {
                return true;
            }
        }
        return false;
    }

    public void removeExistingLogEntry(String exerciseName) throws Exceptions.ActivityDoesNotExist {
        ExerciseLog exerciseLogToFind = null;
        for (Iterator<ExerciseLog> iterator = exerciseLogs.iterator(); iterator.hasNext(); ) {
            ExerciseLog currentExerciseLog = iterator.next();
            if (currentExerciseLog.activityName.equalsIgnoreCase(exerciseName)) {
                exerciseLogToFind = currentExerciseLog;
                break;
            }
        }

        if (exerciseLogToFind == null) {
            throw new Exceptions.ActivityDoesNotExist(ManagerStrings.LOG_ENTRY_EXERCISE_DOES_NOT_EXIST);
        }

        exerciseLogs.remove(exerciseLogToFind);
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
