package byteceps.processing;

import byteceps.activities.Activity;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.ManagerStrings;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Abstract base class for managing activities.
 */
public abstract class ActivityManager {
    protected final String activityType;
    protected final HashSet<Activity> activitySet;

    public ActivityManager() {
        this.activityType = getActivityType(false);
        this.activitySet = new HashSet<>();
    }

    /**
     * Executes the specified command.
     *
     * @param parser Parser containing user input.
     * @return Message to user after executing the command.
     * @throws Exceptions.InvalidInput        if no command action specified.
     * @throws Exceptions.ErrorAddingActivity If there's an error adding the activity.
     * @throws Exceptions.ActivityExistsException If the activity already exists.
     * @throws Exceptions.ActivityDoesNotExists If the activity does not exist.
     */
    public abstract String execute(Parser parser) throws Exceptions.InvalidInput,
            Exceptions.ErrorAddingActivity, Exceptions.ActivityExistsException,
            Exceptions.ActivityDoesNotExists;

    /**
     * Adds an activity to the manager.
     *
     * @param activity The activity to add.
     * @throws Exceptions.ActivityExistsException If the activity already exists.
     */
    public void add(Activity activity) throws Exceptions.ActivityExistsException {
        boolean setReturn = activitySet.add(activity);

        if (!setReturn) {
            String activityName = activity.getActivityName();
            throw new Exceptions.ActivityExistsException(
                    String.format(ManagerStrings.ACTIVITY_EXISTS_EXCEPTION, this.activityType, activityName)
            );
        }
    }

    /**
     * Deletes an activity from the manager.
     *
     * @param activity The activity to delete.
     * @throws Exceptions.ActivityDoesNotExists If the activity does not exist.
     */
    public void delete(Activity activity) throws Exceptions.ActivityDoesNotExists {
        boolean setReturn = activitySet.remove(activity);

        if (!setReturn) {
            String activityName = activity.getActivityName();
            throw new Exceptions.ActivityDoesNotExists(
                    String.format(ManagerStrings.ACTIVITY_DELETE_EXCEPTION,
                            this.activityType, activityName)
            );
        }
    }

    /**
     * Retrieves an activity from the manager by its name.
     *
     * @param activityName The name of the activity to retrieve.
     * @return The retrieved activity.
     * @throws Exceptions.ActivityDoesNotExists if the activity does not exist.
     */
    public Activity retrieve(String activityName) throws Exceptions.ActivityDoesNotExists {
        if (activitySet.isEmpty()) {
            throw new Exceptions.ActivityDoesNotExists(
                    String.format(ManagerStrings.ACTIVITY_EMPTY_LIST_EXCEPTION,
                            this.activityType)
            );
        }

        for (Activity currentActivity : activitySet) {
            if (currentActivity.getActivityName().equals(activityName)) {
                return currentActivity;
            }
        }

        // throw error as activity not found in the set
        throw new Exceptions.ActivityDoesNotExists(
                String.format(ManagerStrings.ACTIVITY_DOES_NOT_EXIST_EXCEPTION,
                        this.activityType, activityName)
        );
    }

    /**
     * Gets a string representation of the list of activities.
     *
     * @return A string representation of the list of activities.
     */
    public String getListString() {
        if (activitySet.isEmpty()) {
            return String.format(ManagerStrings.ACTIVITY_EMPTY_LIST, getActivityType(true));
        }
        StringBuilder result = new StringBuilder();
        result.append(String.format(ManagerStrings.ACTIVITY_LIST, getActivityType(true), System.lineSeparator()));

        int index = 1;
        for (Iterator<Activity> it = activitySet.iterator(); it.hasNext(); index++) {
            Activity currentActivity = it.next();
            result.append(String.format(ManagerStrings.ACTIVITY_LIST_ITEM, index, currentActivity.getActivityName()));
        }

        return result.toString();
    }

    /**
     * Checks if an activity with a given name exists in the manager.
     *
     * @param activityName The name of the activity to check.
     * @return true if the activity does not exist, false otherwise.
     */
    public boolean doesNotHaveActivity(String activityName) {
        if (activitySet.isEmpty()) {
            return true;
        }

        for (Activity currentActivity : activitySet) {
            if (currentActivity.getActivityName().equals(activityName)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets the list of activities.
     *
     * @return The list of activities.
     */
    public ArrayList<Activity> getActivityList() {
        return new ArrayList<>(activitySet);
    }

    /**
     * Gets the type of activity managed by this manager.
     *
     * @param plural true if the plural form of the activity type is requested, false otherwise.
     * @return The type of activity.
     */
    public abstract String getActivityType(boolean plural);


    /**
     * Gets a string representation of the search results.
     *
     * @param searchTerm The search term.
     * @return A string representation of the search results.
     */
    public String getSearchResultsString(String searchTerm){
        ArrayList <Activity> searchResults = searchActivities(searchTerm);
        return stringify(searchResults);
    }

    /**
     * Checks if an activity matches the search term.
     *
     * @param activity   The activity to check.
     * @param searchTerm The search term.
     * @return true if the activity matches the search term, false otherwise.
     */
    private boolean activityMatchesSearchTerm(Activity activity, String searchTerm) {
        String activityName = activity.getActivityName().toLowerCase();
        String searchTermLowerCase = searchTerm.toLowerCase();
        return activityName.contains(searchTermLowerCase);
    }

    /**
     * Searches for activities that match the search term.
     *
     * @param searchTerm The search term.
     * @return A list of activities that match the search term.
     */
    private ArrayList<Activity> searchActivities(String searchTerm){
        ArrayList <Activity> searchResults = new ArrayList<>();
        for (Activity activity : activitySet){
            if (activityMatchesSearchTerm(activity, searchTerm)) {
                searchResults.add(activity);
            }
        }
        return searchResults;
    }

    /**
     * Converts a list of activities to a string representation.
     *
     * @param searchResults The list of activities.
     * @return A string representation of the list of activities.
     */
    private String stringify(ArrayList<Activity> searchResults){
        if (searchResults.isEmpty()) {
            return ManagerStrings.NO_RESULTS;
        }
        StringBuilder result = new StringBuilder();
        result.append(String.format(ManagerStrings.SEARCH_RESULTS, System.lineSeparator()));

        int index = 1;
        for (Activity currentActivity : searchResults) {
            result.append(String.format(ManagerStrings.ACTIVITY_LIST_ITEM, index, currentActivity.getActivityName()));
            index++;
        }

        return result.toString();
    }

}
