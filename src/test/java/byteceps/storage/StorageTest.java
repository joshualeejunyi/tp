package byteceps.storage;

import byteceps.processing.ExerciseManager;
import byteceps.processing.WorkoutManager;
import byteceps.processing.WeeklyProgramManager;
import byteceps.processing.WorkoutLogsManager;
import byteceps.ui.UserInterface;
import byteceps.ui.strings.UiStrings;
import byteceps.ui.strings.StorageStrings;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.File;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTest {

    private static final String FOLDER_PATH = "./jsons/";
    private static final String FILE_PATH = "./jsons/test.json";
    private static final String RENAMED_PATH = "./jsons/hidden.json";
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
        String jsonsFolder = "/jsons/";
        File currentDir = new File(directoryPath + jsonsFolder);

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

    public boolean isEmptyFile(String path) throws SecurityException {
        try {
            File jsonFile = new File(path);

            if(jsonFile.length() == 0) {
                return true;
            }
            return false;
        } catch (SecurityException e) {
            throw e;
        }
    }

    public boolean isCorruptedFile(String path) throws SecurityException, FileNotFoundException {

        File jsonFile = new File(path);
        try (Scanner jsonScanner = new Scanner(jsonFile)) {
            JSONObject jsonArchive = new JSONObject(jsonScanner.nextLine());
        } catch (FileNotFoundException|SecurityException e) {
            throw e;
        } catch (JSONException e) {
            return true;
        }
        return false;
    }


    @Test
    public void save_expectedManagers_success() {
        setUpStreams();

        assertDoesNotThrow(() -> storage.save(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.WORKOUTS_SAVED,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());
        assertEquals(expectedOutput, outContent.toString());

        restoreStreams();
    }

    @Test
    public void load_noPreExistingJsonFile_success() {
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
    public void load_preExistingJsonFile_success() {
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
    public void load_preExistingJsonFileBlankBug_success() {
        assertDoesNotThrow(() -> checkFile(FILE_PATH));
        boolean fileExists = checkFile(FILE_PATH);

        if (!fileExists) {
            assertDoesNotThrow(() -> storage.save(exerciseManager, workoutManager, weeklyProgramManager,
                    workoutLogsManager));
        }

        if (assertDoesNotThrow(() -> isEmptyFile(FILE_PATH))){
            assertTrue(deleteFile(FILE_PATH));
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
    public void load_preExistingJsonFileCorruptBug_success() {
        assertDoesNotThrow(() -> checkFile(FILE_PATH));
        boolean fileExists = checkFile(FILE_PATH);

        if (!fileExists) {
            assertDoesNotThrow(() -> storage.save(exerciseManager, workoutManager, weeklyProgramManager,
                    workoutLogsManager));
        }


        if (assertDoesNotThrow(() -> isCorruptedFile(FILE_PATH))){
            assertTrue(deleteFile(FILE_PATH));
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
    public void load_corruptedJSON_failure() {
        String corruptFileName = "corrupted.json";
        String corruptFailureFileName = "corrupted.json.old";
        String corruptFilePath = "./jsons/corrupted.json";
        String corruptFailureFilePath = "./jsons/corrupted.json.old";

        assertDoesNotThrow( () -> checkFile(corruptFilePath));

        Storage failureStorage = new Storage(corruptFilePath, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(corruptFilePath));

        assertNotEquals("", corruptFailureFileName = findFileName(corruptFailureFileName));

        corruptFailureFilePath = FOLDER_PATH + corruptFailureFileName;

        assertTrue(restoreOriginalFile(corruptFilePath, corruptFailureFilePath));

        restoreStreams();

    }


    @Test
    public void load_duplicateExercise_failure() {
        String duplicateExerciseFileName = "duplicateExercise.json";
        String duplicateExerciseFailureFileName = "duplicateExercise.json.old";
        String duplicateExerciseFilePath = "./jsons/duplicateExercise.json";
        String duplicateExerciseFailureFilePath = "./jsons/duplicateExercise.json.old";

        assertDoesNotThrow( () -> checkFile(duplicateExerciseFilePath));

        Storage failureStorage = new Storage(duplicateExerciseFilePath, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(duplicateExerciseFilePath));

        assertNotEquals("",duplicateExerciseFailureFileName = findFileName(duplicateExerciseFailureFileName));

        duplicateExerciseFailureFilePath = FOLDER_PATH + duplicateExerciseFailureFileName;

        assertTrue(restoreOriginalFile(duplicateExerciseFilePath, duplicateExerciseFailureFilePath));

        restoreStreams();

    }

    @Test
    public void load_duplicateWorkout_failure() {
        String duplicateWorkoutFileName = "duplicateWorkout.json";
        String duplicateWorkoutFailureFileName = "duplicateWorkout.json.old";
        String duplicateWorkoutFilePath = "./jsons/duplicateWorkout.json";
        String duplicateWorkoutFailureFilePath = "./jsons/duplicateWorkout.json.old";

        assertDoesNotThrow( () -> checkFile(duplicateWorkoutFilePath));

        Storage failureStorage = new Storage(duplicateWorkoutFilePath, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(duplicateWorkoutFilePath));

        assertNotEquals("",duplicateWorkoutFailureFileName = findFileName(duplicateWorkoutFailureFileName));

        duplicateWorkoutFailureFilePath = FOLDER_PATH + duplicateWorkoutFailureFileName;

        assertTrue(restoreOriginalFile(duplicateWorkoutFilePath, duplicateWorkoutFailureFilePath));

        restoreStreams();

    }

    @Test
    public void load_workoutMissing_failure() {
        String workoutMissingFileName = "workoutMissing.json";
        String workoutMissingFailureFileName = "workoutMissing.json.old";
        String workoutMissingFilePath = "./jsons/workoutMissing.json";
        String workoutMissingFailureFilePath = "./jsons/workoutMissing.json.old";

        assertDoesNotThrow( () -> checkFile(workoutMissingFilePath));

        Storage failureStorage = new Storage(workoutMissingFilePath, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(workoutMissingFilePath));

        assertNotEquals("",workoutMissingFailureFileName = findFileName(workoutMissingFailureFileName));

        workoutMissingFailureFilePath = FOLDER_PATH + workoutMissingFailureFileName;

        assertTrue(restoreOriginalFile(workoutMissingFilePath, workoutMissingFailureFilePath));

        restoreStreams();

    }


    @Test
    public void load_workoutExercisesMissing_failure() {
        String workoutExercisesMissingFileName = "workoutExercisesMissing.json";
        String workoutExercisesMissingFailureFileName = "workoutExercisesMissing.json.old";
        String workoutExercisesMissingFilePath = "./jsons/workoutExercisesMissing.json";
        String workoutExercisesMissingFailureFilePath = "./jsons/workoutExercisesMissing.json.old";

        assertDoesNotThrow( () -> checkFile(workoutExercisesMissingFilePath));

        Storage failureStorage = new Storage(workoutExercisesMissingFilePath, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(workoutExercisesMissingFilePath));

        assertNotEquals("",workoutExercisesMissingFailureFileName = findFileName(workoutExercisesMissingFailureFileName));

        workoutExercisesMissingFailureFilePath = FOLDER_PATH + workoutExercisesMissingFailureFileName;

        assertTrue(restoreOriginalFile(workoutExercisesMissingFilePath, workoutExercisesMissingFailureFilePath));

        restoreStreams();

    }


    @Test
    public void load_nonExistingExercisesInLogs_failure() {
        String logsExerciseFailFileName = "logsExerciseFail.json";
        String logsExerciseFailFailureFileName = "logsExerciseFail.json.old";
        String logsExerciseFailFilePath = "./jsons/logsExerciseFail.json";
        String logsExerciseFailFailureFilePath = "./jsons/logsExerciseFail.json.old";

        assertDoesNotThrow( () -> checkFile(logsExerciseFailFilePath));

        Storage failureStorage = new Storage(logsExerciseFailFilePath, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(logsExerciseFailFilePath));

        assertNotEquals("",logsExerciseFailFailureFileName = findFileName(logsExerciseFailFailureFileName));

        logsExerciseFailFailureFilePath = FOLDER_PATH + logsExerciseFailFailureFileName;

        assertTrue(restoreOriginalFile(logsExerciseFailFilePath, logsExerciseFailFailureFilePath));

        restoreStreams();

    }

    @Test
    public void load_nonExistingWorkoutInLogs_failure() {
        String logsWorkoutFailFileName = "logsWorkoutFail.json";
        String logsWorkoutFailFailureFileName = "logsWorkoutFail.json.old";
        String logsWorkoutFailFilePath = "./jsons/logsWorkoutFail.json";
        String logsWorkoutFailFailureFilePath = "./jsons/logsWorkoutFail.json.old";

        assertDoesNotThrow( () -> checkFile(logsWorkoutFailFilePath));

        Storage failureStorage = new Storage(logsWorkoutFailFilePath, new UserInterface());

        setUpStreams();

        assertDoesNotThrow( () -> failureStorage.load(exerciseManager, workoutManager, weeklyProgramManager,
                workoutLogsManager));

        String expectedOutput = String.format("%s%s%s%s%s%s%s%s%s%s", UiStrings.BYTECEP_PROMPT, StorageStrings.LOADING,
                System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator(), UiStrings.BYTECEP_PROMPT,
                StorageStrings.LOAD_ERROR, System.lineSeparator(), UiStrings.SEPARATOR, System.lineSeparator());

        assertEquals(expectedOutput, outContent.toString());

        assertDoesNotThrow( () -> checkFile(logsWorkoutFailFilePath));

        assertNotEquals("",logsWorkoutFailFailureFileName = findFileName(logsWorkoutFailFailureFileName));

        logsWorkoutFailFailureFilePath = FOLDER_PATH + logsWorkoutFailFailureFileName;

        assertTrue(restoreOriginalFile(logsWorkoutFailFilePath, logsWorkoutFailFailureFilePath));

        restoreStreams();

    }


}
