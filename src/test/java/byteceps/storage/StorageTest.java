package byteceps.storage;

import byteceps.processing.ExerciseManager;
import byteceps.processing.WorkoutManager;
import byteceps.processing.WeeklyProgramManager;
import byteceps.processing.WorkoutLogsManager;
import byteceps.ui.UserInterface;
import byteceps.ui.strings.UiStrings;
import byteceps.ui.strings.StorageStrings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTest {

    private static final String FILE_PATH = "test.json";
    private static final String RENAMED_PATH = "hidden.json";
    private static Storage storage;

    private static ExerciseManager exerciseManager = null;
    private static WorkoutManager workoutManager = null;
    private static WorkoutLogsManager workoutLogsManager = null;
    private static WeeklyProgramManager weeklyProgramManager = null;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private final UserInterface ui = new UserInterface();

    @BeforeEach
    public void setup() {
        storage = new Storage(FILE_PATH, ui);
        exerciseManager = new ExerciseManager();
        workoutManager = new WorkoutManager(exerciseManager);
        workoutLogsManager = new WorkoutLogsManager();
        weeklyProgramManager = new WeeklyProgramManager(exerciseManager, workoutManager, workoutLogsManager);
    }

    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    public boolean checkFile(String path) throws SecurityException, NullPointerException {
        //Solution below inspired by https://www.geeksforgeeks.org/file-exists-method-in-java-with-examples/
        try {
            File jsonFile = new File(path);
            if (jsonFile.exists()) {
                return true;
            }
            return false;
        } catch (SecurityException | NullPointerException e) {
            throw e;
        }
    }


    public boolean deleteFile(String path) {
        try {
            File jsonFile = new File(path);
            return jsonFile.delete();
        } catch (SecurityException e) {
            return false;
        }

    }

    public boolean renameOriginalFile(String originalPath, String newPath) {
        //Solution below inspired by https://www.geeksforgeeks.org/file-renameto-method-in-java-with-examples/
        File originalFile = new File(originalPath);
        File renamedFile = new File(newPath);

        try {
            if (originalFile.renameTo(renamedFile)) {
                return true;
            }
            return false;
        } catch (SecurityException e) {
            return false;
        }

    }

    public boolean restoreOriginalFile(String originalPath, String renamedPath) {
        File testGenFile = new File(originalPath);
        File originalFileRenamed = new File(renamedPath);

        assertDoesNotThrow(() -> checkFile(originalPath));
        assertDoesNotThrow(() -> checkFile(renamedPath));

        boolean testGenFileExists = checkFile(originalPath);
        boolean originalFileRenamedExists = checkFile(renamedPath);

        try {
            if (testGenFileExists && originalFileRenamedExists) {
                assertTrue(testGenFile.delete());
                File originalFileRestored = new File(originalPath);
                assertTrue(originalFileRenamed.renameTo(originalFileRestored));
                return true;
            }
            return false;
        } catch (SecurityException e) {
            return false;
        }

    }

    public String findFileName (String partialPath) {
        //Solution below adapted by https://www.baeldung.com/java-current-directory
        String directoryPath = new File("").getAbsolutePath();
        File currentDir = new File(directoryPath);

        //Solution below inspired by https://www.geeksforgeeks.org/file-listfiles-method-in-java-with-examples/
        File[] files = currentDir.listFiles();

        for (File f:files) {
            String currentName = f.getName();
            if (currentName.contains(partialPath)) {
                return currentName;
            }
        }

        return "";
    }

    @Test
    public void execute_save_success() {
        setUpStreams();

        assertDoesNotThrow(() -> storage.save(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.WORKOUTS_SAVED,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());
        assertEquals(expectedOutput, outContent.toString());

        restoreStreams();
    }

    @Test
    public void execute_load_newFileSuccess() {
        assertDoesNotThrow(() -> checkFile(FILE_PATH));
        boolean fileExists = checkFile(FILE_PATH);

        if (fileExists) {
            assertTrue(renameOriginalFile(FILE_PATH, RENAMED_PATH));
        }

        setUpStreams();
        assertDoesNotThrow(() -> storage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.NO_SAVE_DATA,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());
        assertEquals(expectedOutput, outContent.toString());

        restoreStreams();

        if (fileExists) {
            assertTrue(restoreOriginalFile(FILE_PATH, RENAMED_PATH));
        }
    }

    @Test
    public void execute_load_existingFileSuccess() {
        assertDoesNotThrow(() -> checkFile(FILE_PATH));
        boolean fileExists = checkFile(FILE_PATH);

        if (!fileExists) {
            assertDoesNotThrow(() -> storage.save(exerciseManager, workoutManager, weeklyProgramManager,
                    workoutLogsManager));
        }

        setUpStreams();
        assertDoesNotThrow(() -> storage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_SUCCESS, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());
        assertEquals(expectedOutput, outContent.toString());

        if (!fileExists) {
            assertTrue(deleteFile(FILE_PATH));
        }
        restoreStreams();
    }

    @Test
    public void execute_load_corruptedJSONFailure() {
        String corruptFile = "corrupted.json";
        String corruptFailureFile = "corrupted.json.old";

        assertDoesNotThrow( () -> checkFile(corruptFile));

        Storage failureStorage = new Storage(corruptFile, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(corruptFile));

        assertNotEquals("",corruptFailureFile = findFileName(corruptFailureFile));

        restoreOriginalFile(corruptFile, corruptFailureFile);

        restoreStreams();

    }


    @Test
    public void execute_load_duplicateExerciseFailure() {
        String duplicateExerciseFile = "duplicateExercise.json";
        String duplicateExerciseFailureFile = "duplicateExercise.json.old";

        assertDoesNotThrow( () -> checkFile(duplicateExerciseFile));

        Storage failureStorage = new Storage(duplicateExerciseFile, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(duplicateExerciseFile));

        assertNotEquals("",duplicateExerciseFailureFile = findFileName(duplicateExerciseFailureFile));

        restoreOriginalFile(duplicateExerciseFile, duplicateExerciseFailureFile);

        restoreStreams();

    }

    @Test
    public void execute_load_duplicateWorkoutFailure() {
        String duplicateWorkoutFile = "duplicateWorkout.json";
        String duplicateWorkoutFailureFile = "duplicateWorkout.json.old";

        assertDoesNotThrow( () -> checkFile(duplicateWorkoutFile));

        Storage failureStorage = new Storage(duplicateWorkoutFile, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(duplicateWorkoutFile));

        assertNotEquals("",duplicateWorkoutFailureFile = findFileName(duplicateWorkoutFailureFile));

        restoreOriginalFile(duplicateWorkoutFile, duplicateWorkoutFailureFile);

        restoreStreams();

    }

    @Test
    public void execute_load_workoutMissingFailure() {
        String workoutMissingFile = "workoutMissing.json";
        String workoutMissingFailureFile = "workoutMissing.json.old";

        assertDoesNotThrow( () -> checkFile(workoutMissingFile));

        Storage failureStorage = new Storage(workoutMissingFile, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(workoutMissingFile));

        assertNotEquals("",workoutMissingFailureFile = findFileName(workoutMissingFailureFile));

        restoreOriginalFile(workoutMissingFile, workoutMissingFailureFile);

        restoreStreams();

    }


    @Test
    public void execute_load_workoutExercisesMissingFailure() {
        String workoutExercisesMissingFile = "workoutExercisesMissing.json";
        String workoutExercisesMissingFailureFile = "workoutExercisesMissing.json.old";

        assertDoesNotThrow( () -> checkFile(workoutExercisesMissingFile));

        Storage failureStorage = new Storage(workoutExercisesMissingFile, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(workoutExercisesMissingFile));

        assertNotEquals("",workoutExercisesMissingFailureFile = findFileName(workoutExercisesMissingFailureFile));

        restoreOriginalFile(workoutExercisesMissingFile, workoutExercisesMissingFailureFile);

        restoreStreams();

    }


    @Test
    public void execute_load_logsExerciseFailFailure() {
        String logsExerciseFailFile = "logsExerciseFail.json";
        String logsExerciseFailFailureFile = "logsExerciseFail.json.old";

        assertDoesNotThrow( () -> checkFile(logsExerciseFailFile));

        Storage failureStorage = new Storage(logsExerciseFailFile, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(logsExerciseFailFile));

        assertNotEquals("",logsExerciseFailFailureFile = findFileName(logsExerciseFailFailureFile));

        restoreOriginalFile(logsExerciseFailFile, logsExerciseFailFailureFile);

        restoreStreams();

    }

    @Test
    public void execute_load_logsWorkoutFailFailure() {
        String logsWorkoutFailFile = "logsWorkoutFail.json";
        String logsWorkoutFailFailureFile = "logsWorkoutFail.json.old";

        assertDoesNotThrow( () -> checkFile(logsWorkoutFailFile));

        Storage failureStorage = new Storage(logsWorkoutFailFile, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(logsWorkoutFailFile));

        assertNotEquals("",logsWorkoutFailFailureFile = findFileName(logsWorkoutFailFailureFile));

        restoreOriginalFile(logsWorkoutFailFile, logsWorkoutFailFailureFile);

        restoreStreams();

    }


}
