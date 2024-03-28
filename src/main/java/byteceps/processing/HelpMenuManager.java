package byteceps.processing;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.UserInterface;

public class HelpMenuManager {

  UserInterface ui;

  private final String helpManagerGreeting = "To access the help menu for command guidance, please type:\n"
          + "help /COMMAND_TYPE_FLAG\n"
          + "Available command types (type exactly as shown):\n"
          + "exercise\n"
          + "workout\n"
          + "program";

  private final String[] exerciseFlagFunctions = {
          "(1) add an exercise", "(2) delete an existing exercise",
          "(3) edit an existing exercise's name", "(4) list all existing exercises"
  };

  private final String[] exerciseFlagFormats = {
          "exercise /add <EXERCISE_NAME [string]>",
          "exercise /delete <EXERCISE_NAME [string]>",
          "exercise /edit <OLD_EXERCISE_NAME [string]> /to <NEW_EXERCISE_NAME [string]>", "exercise /list"
  };


  private final String[] workoutFlagFunctions = {
          "(1) create a workout plan", "(2) delete an existing workout plan",
          "(3) list all existing workout plans", "(4) assign an exercise to a specified workout plan",
          "(5) remove an exercise from a specified workout plan", "(6) list all exercises in a given workout plan"
  };

  private final String[] workoutFlagFormats = {
          "workout /create <WORKOUT_PLAN_NAME [string]>",
          "workout /delete <WORKOUT_PLAN_NAME [string]>",
          "workout /list", "workout /assign <EXERCISE_NAME [string]> /to <WORKOUT_PLAN_NAME [string]>",
          "workout /unassign <EXERCISE_NAME [string]> /from <WORKOUT_PLAN_NAME [string]>",
          "workout /info <WORKOUT_PLAN_NAME [string]>"
  };

  private final String[] programFlagFunctions = {
          "(1) assign a workout plan to a specific day of the week",
          "(2) view today's workout plan",
          "(3) see all workout plans assigned to each day of the week",
          "(4) remove a workout plan from a given day of the week",
          "(5) create a log for the amount of weight,sets & reps completed for an exercise on a given day which already"
                  + " has an assigned workout plan",
          "(6) create a log for a specified date", "(7) see all the dates that you have entered at least 1 log entry",
          "(8) view the logs that you have added on a specific date"
  };

  private final String[] programFlagFormats = {
          "program /assign <WORKOUT_PLAN_NAME [string]> /to <DAY [string]>", "program /today", "program /list",
          "program /clear <DAY [string]>",
          "program /log <EXERCISE_NAME [string]> /weight <WEIGHT [integer]> /sets <NUMBER_OF_SETS [integer]> "
                  + "/reps <NUMBER_OF_REPS [integer]>",
          "program /log <EXERCISE_NAME [string]> /weight <WEIGHT [integer]> /sets <NUMBER_OF_SETS [integer]> "
                 + "/reps <NUMBER_OF_REPS [integer]> /date <DATE [yyyy-mm-dd]>", "program /history",
          "program /history <DATE [yyyy-mm-dd]>"
  };

    public HelpMenuManager(UserInterface ui) {
      this.ui=ui;
    }

    public void printHelpGreeting () {
      UserInterface.printMessage(helpManagerGreeting);
    }

  public void execute(Parser parser) throws Exceptions.InvalidInput {
    assert parser != null : "Parser must not be null";
    assert parser.getAction() != null : "Command action must not be null";

    if (parser.getAction().isEmpty()) {
      throw new Exceptions.InvalidInput("No command type specified");
    }

    if (!parser.getActionParameter().isEmpty()) {
      throw new Exceptions.InvalidInput("Help menu does not accept additional values after command type"
              + "flag");
    }

    if (parser.hasAdditionalArguments()) {
      throw new Exceptions.InvalidInput("Help menu does not accept additional arguments");
    }

    switch (parser.getAction()) {
    case "exercise":
      showExerciseCommand();
      break;
    case "workout":
      showWorkoutCommand();
      break;
    case "program":
      showProgramCommand();
      break;
    default:
      throw new Exceptions.InvalidInput("Command type is not recognised");
    }

  }


  public void showExerciseCommand() throws Exceptions.InvalidInput {

      boolean incorrectSelection = true;

      while (incorrectSelection) {
        String exerciseMessage = "Please enter the number corresponding to the exercise command format you want to see";
        UserInterface.printMessage(exerciseMessage);

        for (String flagFunction: exerciseFlagFunctions) {
          ui.printListItem(flagFunction);
        }

        incorrectSelection = getFlagFormat("exercise") ;

      }

  }

  public void showWorkoutCommand() throws Exceptions.InvalidInput {

    boolean incorrectSelection = true;

    while (incorrectSelection) {
      String workoutMessage = "Please enter the number corresponding to the workout command format you want to see";
      UserInterface.printMessage(workoutMessage);

      for (String flagFunction: workoutFlagFunctions) {
        System.out.printf("%s%s", flagFunction, System.lineSeparator());
      }

      incorrectSelection = getFlagFormat("workout") ;

    }

  }

  public void showProgramCommand() {

    boolean incorrectSelection = true;

    while (incorrectSelection) {
      String programMessage = "Please enter the number corresponding to the program command format you want to see";
      UserInterface.printMessage(programMessage);


      for (String flagFunction: programFlagFunctions) {
        System.out.printf("%s%s", flagFunction, System.lineSeparator());
      }


      incorrectSelection = getFlagFormat("program") ;

    }

  }

  public boolean getFlagFormat (String commandType) {
    try{
      int flagChoice = Integer.parseInt(ui.getUserInput());
      int flagIndex = flagChoice - 1;

      switch (commandType) {
        case "exercise":
          UserInterface.printMessage(getExerciseFlagFormats(flagIndex));
          break;
        case "workout":
          UserInterface.printMessage(getWorkoutFlagFormats(flagIndex));
          break;
        case "program":
          UserInterface.printMessage(getProgramFlagFormats(flagIndex));
          break;
        default:
          break;

      }

      return false;

    } catch (NumberFormatException | IndexOutOfBoundsException e) {
      System.out.println("Invalid choice entered: please enter a number from the list");
      return true;
    }

  }

  public String getExerciseFlagFormats(int index) {
    return exerciseFlagFormats[index];
  }

  public String getWorkoutFlagFormats(int index) {
    return workoutFlagFormats[index];
  }

  public String getProgramFlagFormats(int index) {
    return programFlagFormats[index];
  }

}
