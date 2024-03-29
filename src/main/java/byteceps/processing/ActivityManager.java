package byteceps.processing;

import byteceps.activities.Activity;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public abstract class ActivityManager {
    protected final String activityType;
    protected final HashSet<Activity> activitySet;

    public ActivityManager() {
        this.activityType = getActivityType(false);
        this.activitySet = new HashSet<>();
    }

    public abstract void execute(Parser parser) throws Exceptions.InvalidInput,
            Exceptions.ErrorAddingActivity, Exceptions.ActivityExistsException,
            Exceptions.ActivityDoesNotExists;

    public void add(Activity activity) throws Exceptions.ActivityExistsException {
        boolean setReturn = activitySet.add(activity);

        if (!setReturn) {
            String activityName = activity.getActivityName();
            throw new Exceptions.ActivityExistsException(
                    String.format("The %s entry: %s already exists", this.activityType, activityName)
            );
        }
    }

    public void delete(Activity activity) throws Exceptions.ActivityDoesNotExists {
        boolean setReturn = activitySet.remove(activity);

        if (!setReturn) {
            String activityName = activity.getActivityName();
            throw new Exceptions.ActivityDoesNotExists(
                    String.format("The %s entry: %s does not exist and cannot be deleted",
                            this.activityType, activityName)
            );
        }
    }

    public Activity retrieve(String activityName) throws Exceptions.ActivityDoesNotExists {
        if (activitySet.isEmpty()) {
            throw new Exceptions.ActivityDoesNotExists(
                    String.format("The %s List is Empty!",
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
                String.format("The %s entry: %s does not exist",
                        this.activityType, activityName)
        );
    }

    public String executeListAction() {
        if (activitySet.isEmpty()) {
            return String.format("Your List of %s is Empty", getActivityType(true));
        }
        StringBuilder result = new StringBuilder();
        result.append(String.format("Listing %s:%s", getActivityType(true), System.lineSeparator()));

        int index = 1;
        for (Iterator<Activity> it = activitySet.iterator(); it.hasNext(); index++) {
            Activity currentActivity = it.next();
            result.append(String.format("\t\t\t%d. %s\n", index, currentActivity.getActivityName()));
        }

        return result.toString();
    }

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

    public ArrayList<Activity> getActivityList() {
        return new ArrayList<>(activitySet);
    }

    public abstract String getActivityType(boolean plural);

    public String getSearchResultsString(String searchTerm){
        ArrayList <Activity> searchResults = new ArrayList<>();
        for(Activity activity : activitySet){
            if(activity.getActivityName().toLowerCase().contains(searchTerm.toLowerCase())){
                searchResults.add(activity);
            }
        }
        return stringify(searchResults);
    }

    private String stringify(ArrayList<Activity> searchResults){
        if (searchResults.isEmpty()) {
            return "No results found";
        }
        StringBuilder result = new StringBuilder();
        result.append(String.format("Search Results:%s", System.lineSeparator()));

        int index = 1;
        for (Activity currentActivity : searchResults) {
            result.append(String.format("\t\t\t%d. %s\n", index, currentActivity.getActivityName()));
            index++;
        }

        return result.toString();
    }

}
