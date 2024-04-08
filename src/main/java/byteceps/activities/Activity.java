package byteceps.activities;

/**
 * Represents an activity in the byteceps application.
 * Each Activity object has a name.
 */
public class Activity {
    protected String activityName;

    /**
     * Constructs a new Activity object with the specified activity name.
     *
     * @param activityName The name of the activity.
     */
    public Activity(String activityName) {
        this.activityName = activityName;
    }

    /**
     * Returns the name of the activity.
     *
     * @return The name of the activity.
     */
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }


    /**
     * Computes a hash code for the Activity object based on its name.
     *
     * @return The hash code value for this Activity.
     */
    @Override
    public int hashCode() {
        return activityName.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Two Activity objects are considered equal if they have the same name.
     *
     * @param obj The reference object with which to compare.
     * @return true if this Activity is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Activity other = (Activity) obj;
        return (activityName.equalsIgnoreCase(other.getActivityName()));
    }
}
