package byteceps.processing;

import byteceps.activities.Activity;
import byteceps.activities.Day;
import byteceps.activities.Exercise;
import byteceps.activities.Workout;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class CascadingDeletionProcessor {
    /**
     * Checks if any deletion of an exercise/workout causes cascading effects on the container classes.
     * If yes, removes the exercise/workout from the workout/program respectively, silently.
     *
     * @param parser User input in the form of a Parser object.
     * @param workoutManager The activity manager containing all workouts
     * @param weeklyProgramManager The activity manager containing the training program
     */
    public static void checkForCascadingDeletions (Parser parser, WorkoutManager workoutManager,
                                                   WeeklyProgramManager weeklyProgramManager) {
        try {
            String parserAction = parser.getAction();
            if (!parserAction.equals("delete")) {
                return;
            }

            String parserCommand = parser.getCommand();
            if(parserCommand.equals("exercise")) {
                removeDeletedExerciseFromWorkouts(parser.getActionParameter(), workoutManager);
            } else if(parserCommand.equals("workout")) {
                removeDeletedWorkoutsFromProgram(parser.getActionParameter(), weeklyProgramManager);
            }

        } catch (Exceptions.InvalidInput | Exceptions.ActivityDoesNotExists e) {
            return;
        }

    }

    private static void removeDeletedExerciseFromWorkouts (String exerciseName, WorkoutManager workoutManager) {
        ArrayList<Activity> workoutList = workoutManager.getActivityList();
        for (Activity item : workoutList) {
            Workout workout = (Workout) item;
            ArrayList<Exercise> workoutExerciseList = ((Workout) workout).getExerciseList();
            workoutExerciseList.removeIf(exercise -> exercise.getActivityName().equalsIgnoreCase(exerciseName));
        }
    }

    private static void removeDeletedWorkoutsFromProgram (String workoutName,
                                                          WeeklyProgramManager weeklyProgramManager)
            throws Exceptions.ActivityDoesNotExists, Exceptions.InvalidInput {
        LinkedHashSet<Activity> newWorkoutsInProgram = weeklyProgramManager.getDaySet();
        LinkedHashSet<Activity> oldWorkoutsInProgram = new LinkedHashSet<>(newWorkoutsInProgram);

        for (Activity item : oldWorkoutsInProgram) {
            Day currentDay = (Day) item;
            Workout workout = currentDay.getAssignedWorkout();
            if (workout == null) {
                continue;
            } else if (workout.getActivityName().equals(workoutName)) {
                String currentDayString = currentDay.getActivityName();

                newWorkoutsInProgram.remove(weeklyProgramManager.getDay(currentDayString));
                Day newDay = new Day(currentDayString);
                newDay.setAssignedWorkout(null);
                newWorkoutsInProgram.add(newDay);
            }
        }
    }
}
