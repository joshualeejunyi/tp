package byteceps;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.processing.ExerciseManager;
import byteceps.processing.WorkoutManager;
import byteceps.processing.WeeklyProgramManager;
import byteceps.processing.WorkoutLogsManager;
import byteceps.processing.HelpMenuManager;
import byteceps.processing.CascadingDeletionProcessor;
import byteceps.storage.Storage;
import byteceps.ui.strings.UiStrings;
import byteceps.ui.UserInterface;
import byteceps.ui.strings.CommandStrings;

import byteceps.validators.ExerciseValidator;
import byteceps.validators.WorkoutValidator;
import byteceps.validators.WeeklyProgramValidator;
import byteceps.validators.WorkoutLogsValidator;
import byteceps.validators.HelpValidator;


import java.io.IOException;

public class ByteCeps {
    private static ExerciseManager exerciseManager = null;
    private static WorkoutManager workoutManager = null;
    private static WeeklyProgramManager weeklyProgramManager = null;
    private static WorkoutLogsManager workoutLogsManager = null;
    private static HelpMenuManager helpMenuManager = null;
    private static Parser parser;
    private static Storage storage;

    private static HelpValidator helpValidator = null;
    private static ExerciseValidator exerciseValidator = null;
    private static WorkoutValidator workoutValidator = null;
    private static WeeklyProgramValidator weeklyProgramValidator = null;
    private static WorkoutLogsValidator workoutLogsValidator = null;

    private static final String FILE_PATH = "data.json";
    private final UserInterface ui = UserInterface.getInstance();


    public ByteCeps() {
        helpValidator = new HelpValidator();
        exerciseValidator = new ExerciseValidator();
        workoutValidator = new WorkoutValidator();
        weeklyProgramValidator = new WeeklyProgramValidator();
        workoutLogsValidator = new WorkoutLogsValidator();
        exerciseManager = new ExerciseManager(exerciseValidator);
        workoutManager = new WorkoutManager(exerciseManager, workoutValidator);
        workoutLogsManager = new WorkoutLogsManager(workoutLogsValidator);
        weeklyProgramManager = new WeeklyProgramManager(exerciseManager, workoutManager, workoutLogsManager,
                weeklyProgramValidator);
        parser = new Parser();
        storage = new Storage(FILE_PATH, ui);
        helpMenuManager = new HelpMenuManager(helpValidator);
    }

    public static void main(String[] args) {
        new ByteCeps().run();
    }

    public void runCommandLine() {
        while (true) {
            String userInput = ui.getUserInput();
            parser.parseInput(userInput);

            String messageToUser;
            try {
                switch (parser.getCommand()) {
                case CommandStrings.COMMAND_EXERCISE:
                    messageToUser = exerciseManager.execute(parser);
                    break;
                case CommandStrings.COMMAND_WORKOUT:
                    messageToUser = workoutManager.execute(parser);
                    break;
                case CommandStrings.COMMAND_PROGRAM:
                    messageToUser = weeklyProgramManager.execute(parser);
                    break;
                case CommandStrings.COMMAND_HELP:
                    messageToUser = helpMenuManager.execute(parser);
                    break;
                case CommandStrings.COMMAND_BYE:
                case CommandStrings.COMMAND_EXIT:
                    return;
                default:
                    messageToUser = CommandStrings.UNKNOWN_COMMAND;
                }
                ui.printMessage(messageToUser);
                CascadingDeletionProcessor.checkForCascadingDeletions(parser, workoutManager, weeklyProgramManager);
            } catch (Exceptions.ActivityExistsException | Exceptions.ErrorAddingActivity |
                     Exceptions.InvalidInput | Exceptions.ActivityDoesNotExists | IllegalStateException e) {
                ui.printMessage(String.format(UiStrings.ERROR_STRING, e.getMessage()));
            }
        }
    }

    public void run() {
        ui.printWelcomeMessage();
        try {
            storage.load(exerciseManager, workoutManager, weeklyProgramManager, workoutLogsManager,
                    exerciseValidator, workoutValidator, weeklyProgramValidator, workoutLogsValidator);
            ui.printMessage(helpMenuManager.printHelpGreeting());
            runCommandLine();
            storage.save(exerciseManager, workoutManager, weeklyProgramManager, workoutLogsManager);
        } catch (IOException e) {
            ui.printMessage(String.format(UiStrings.ERROR_STRING, e.getMessage()));
        }
        ui.printGoodbyeMessage();
    }

}
