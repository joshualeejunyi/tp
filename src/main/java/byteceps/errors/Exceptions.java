package byteceps.errors;

public class Exceptions {
    public static class ActivityExistsException extends Exception {
        public ActivityExistsException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class ActivityDoesNotExist extends Exception {
        public ActivityDoesNotExist(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class ErrorAddingActivity extends Exception {
        public ErrorAddingActivity(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidInput extends Exception {
        public InvalidInput(String errorMessage) {
            super(errorMessage);
        }
    }
}
