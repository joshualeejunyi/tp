package byteceps.storage;

import byteceps.processing.ExerciseManager;
import byteceps.processing.WorkoutManager;
import byteceps.processing.WorkoutLogsManager;
import byteceps.processing.WeeklyProgramManager;
import byteceps.ui.strings.UiStrings;
import byteceps.ui.strings.StorageStrings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.io.IOException;

class StorageTest {

    private static final String FILE_PATH = "data.json";
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

    @BeforeEach
    public void setup() {
        storage = new Storage(FILE_PATH);
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

    public boolean createFile(String path) {
        try {
            File jsonFile = new File(path);
            return jsonFile.createNewFile();
        } catch (IOException e) {
            return false;
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
            assertTrue(createFile(FILE_PATH));
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

}
