package byteceps.activities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActivityTest {
    @Test
    public void getActivityName_validName_setNewName() {
        String activityName = "Running";
        Activity activity = new Activity(activityName);
        assertEquals(activityName, activity.getActivityName());
    }

    @Test
    public void getActivityName_nullName_setNull() {
        String activityName = null;
        Activity activity = new Activity(activityName);
        assertNull(activity.getActivityName());
    }

    @Test
    public void getActivityName_emptyName_setEmptyName() {
        String activityName = "";
        Activity activity = new Activity(activityName);
        assertEquals(activityName, activity.getActivityName());
    }

    @Test
    public void setActivityName_validName_setNewName() {
        Activity activity = new Activity("Initial Name");
        activity.setActivityName("Updated Name");
        assertEquals("Updated Name", activity.getActivityName());
    }

    @Test
    public void setActivityName_nullName_setNull() {
        Activity activity = new Activity("Initial Name");
        activity.setActivityName(null);
        assertNull(activity.getActivityName());
    }

    @Test
    public void setActivityName_emptyName_setEmptyName() {
        Activity activity = new Activity("Initial Name");
        activity.setActivityName("");
        assertEquals("", activity.getActivityName());
    }

    @Test
    public void hashCode_sameName_sameHashCode() {
        Activity activity1 = new Activity("Running");
        Activity activity2 = new Activity("Running");
        assertEquals(activity1.hashCode(), activity2.hashCode());
    }

    @Test
    public void equals_sameName_true() {
        Activity activity1 = new Activity("Running");
        Activity activity2 = new Activity("Running");
        assertTrue(activity1.equals(activity2));
    }

    @Test
    public void equals_differentName_false() {
        Activity activity1 = new Activity("Running");
        Activity activity2 = new Activity("Swimming");
        assertTrue(!activity1.equals(activity2));
    }

    @Test
    public void equals_null_false() {
        Activity activity1 = new Activity("Running");
        assertTrue(!activity1.equals(null));
    }

    @Test
    public void equals_sameObject_true() {
        Activity activity1 = new Activity("Running");
        assertTrue(activity1.equals(activity1));
    }

    @Test
    public void equals_differentClass_false() {
        Activity activity1 = new Activity("Running");
        assertTrue(!activity1.equals(new Object()));
    }



}
