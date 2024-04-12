# Developer Guide

## Acknowledgements

[//]: # ({list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well})

1. [AB-3 Developer Guide](https://se-education.org/addressbook-level3/DeveloperGuide.html)
2. [PlantUML for sequence diagrams](https://plantuml.com/)

## Setting Up and Getting Started

First, fork [this repo](https://github.com/AY2324S2-CS2113-F14-3/tp), and clone the fork into your computer.

If you plan to use IntelliJ IDEA (highly recommended):

1. **Configure the JDK**: Follow the guide [se-edu/guides IDEA: Configuring the JDK](https://se-education.org/guides/tutorials/intellijJdk.html) to ensure IntelliJ is configured to use JDK 11.
2. **Import the project as a Gradle project**: Follow the guide
[se-edu/guides IDEA: Importing a Gradle project](https://se-education.org/guides/tutorials/intellijImportGradleProject.html)
to import the project into IDEA.

   :exclamation: **Note:** Importing a Gradle project is slightly different from importing a normal Java project.
3. **Verify the setup**:
   * Run `ByteCeps.java` and try a few commands.
   * Run the tests using `./gradlew check` and ensure they all pass.

---
## Design

This section provides a high-level explanation of the design and implementation of ByteCeps, 
supported by UML diagrams and short code snippets to illustrate the flow of data and interactions between the 
components.

---

### Architecture

Given below is a quick overview of main components and how they interact with each other.





## Design
### Architecture
Given below is a quick overview of the main components of ByteCeps and how they interact with each other.

![architectureDiagram.png](diagrams/architectureDiagram.png)

**Main components of the architecture**

[ByteCeps](../src/main/java/byteceps/ByteCeps.java) is the entrypoint for the application to launch and shut down.

The bulk of ByteCep's work is done by the following components:
- [UserInterface](../src/main/java/byteceps/ui/UserInterface.java): Interacts with the user via the command line.
- [Parser](../src/main/java/byteceps/commands/Parser.java): Parses the user's input as saves their inputs in the class.
- [ExerciseManager](../src/main/java/byteceps/processing/ExerciseManager.java): Manages all the exercises stored in memory.
- [WorkoutManager](../src/main/java/byteceps/processing/WorkoutManager.java): Manages all the workouts in memory.
- [HelpMenuManager](../src/main/java/byteceps/processing/HelpMenuManager.java): Displays the help messages to the user.
- [WeeklyProgramManager](../src/main/java/byteceps/processing/WeeklyProgramManager.java): Manages the weekly program for the user in memory. 
- [WorkoutLogsManager](../src/main/java/byteceps/processing/WorkoutLogsManager.java): Managing the logging of workouts.
- [Storage](../src/main/java/byteceps/storage/Storage.java): Reads data from, and writes data to, the hard disk.

**Other notable components**:
- [Exercise](../src/main/java/byteceps/activities/Exercise.java): Stores an exercise entry by the user in memory.
- [Workout](../src/main/java/byteceps/activities/Workout.java): Stores a workout (collection of exercises) entry by the user in memory. 
- [ExerciseLog](../src/main/java/byteceps/activities/ExerciseLog.java): Stores an exercise log entry by the user in memory.
- [Exceptions](../src/main/java/byteceps/errors/Exceptions.java): Represents exceptions used by the components in the application.


## Classes: overview
### `Activity` and child classes
The `Activity` class serves as a parent class to `Exercise`, `ExerciseLog`, `Workout`, `WorkoutLog` and `Day` classes for the ease of usage of `ActivityManager` classes (see below).

**Note:** The `Day` class acts as a container class for `Workout`, for use in `WeeklyProgramManager` 
![ActivityClassDiagram](diagrams/ActivityClassDiagram.png)

### <code>ActivityManager</code> and child classes
The <code>ActivityManager</code> and inheritors are responsible for managing an <code>ArrayList</code> of activities. The basic functions of an <code>ActivityManager</code> include:
1. <code>add()</code>: Adding an activity to the <code>ArrayList</code>
2. <code>delete()</code>: Deleting an activity from the <code>ArrayList</code>
3. <code>retrieve()</code>: Retrieving an activity from the <code>ArrayList</code> by name
4. <code>getListString()</code>: Get the string containing all the activities contained in the <code>ActivityManager</code>.
5. `execute()`: Execute all commands related to the `ActivityManager` and return the required user input.

![ActivityManagerClassDiagram](diagrams/ActivityManagerClassDiagram.png)

#### The `ExerciseManager` class
`ExerciseManager` is responsible for tracking and manipulating all exercises added to `ByteCeps` by the user.

#### The `WorkoutManager` class
`WorkoutManager` is responsible for tracking and manipulating all workouts created by the user.

#### The `WeeklyProgramManager` class
`WeeklyProgram` is responsible for tracking and manipulating the weekly training program set by the user.

## Implementation
### Exercise Management
#### [Implemented] Add, Edit, Delete, List, and Search Exercises
ByteCeps streamlines the management of exercise-related tasks by following a general multi-step pattern. Here’s how these operations are carried out:

**Step 1 - Input Processing:** 
The user’s input is received and processed by ByteCeps, which involves parsing the command through the `Parser` class. User input examples include:
- `exercise /add Pushups` for adding an exercise.
- `exercise /edit Pushups /to Pullups` for editing an exercise name from Pushups to Pullups.
- `exercise /delete Pushups` for deleting the Pushups exercise.
- `exercise /list` for listing all exercises.
- `exercise /search Pushups` for finding all instances of the Pushups exercise.

**Step 2 - Command Identification:** 
The `Parser` class determines the type of exercise operation and extracts any necessary parameters. For instance, the `exercise /add` command will be recognized, and the exercise name `Pushups` will be parsed as the parameter.

**Step 3 - Command Validation**: The input is then validated using `ExerciseValidator` class to ensure that the command and parameters provided meet the expected format and criteria for processing.

**Step 4 - Command Execution**: The appropriate action is taken by the `ExerciseManager` class. 
- Adding: If the user wants to add a new exercise, `ExerciseManager` creates a new `Exercise` instance and adds it to the `ExerciseManager's activitySet`.
- Editing: When editing an exercise, `ExerciseManager` locates the existing `Exercise`, updates its details, and then updates the `activitySet` accordingly.
- Deleting: To delete an exercise, `ExerciseManager` finds the targeted `Exercise` in the `activitySet` and removes it.
- Listing: For listing exercises, `ExerciseManager` retrieves all the exercises from the `activitySet` and formats them into a list for display.
- Searching: Searching is handled by querying the `activitySet` for exercises that match the search criteria provided by the user, and presenting the results.

**Step 5 - Result Display**: After the command is executed, a message indicating the success or failure of the operation is generated and displayed to the user. This feedback is crucial for confirming the effect of the user's command on the system.

Here is the sequence diagram for the `exercise /add pushups` command to illustrate the five-step process:

![AddExercise](diagrams/addExercise.png)

### Workout Management
#### [Implemented] Add, Edit, Delete, List, and Search Workout plan.
ByteCeps streamlines the management of exercise-related tasks by following a general multi-step pattern. Here’s how these operations are carried out:

**Step 1 - Input Processing:** 
The user’s input is received and processed by ByteCeps, which involves parsing the command through the `Parser` class. User input examples include:
- `workout /create LegDay` for creating a workout plan.
- `workout /edit LegDay /to CardioBlast` for editing a workout plan name from LegDay to CardioBlast.
- `workout /delete LegDay` for deleting the LegDay workout plan.
- `workout /list` for listing all workout plans.
- `workout /search HighIntensity` for finding all workout plans containing HighIntensity.

**Step 2 - Command Identification:** 
The `Parser` class determines the type of workout operation and extracts any necessary parameters. For instance, the `workout /create` command will be recognized, and the workout plan name `LegDay` will be parsed as the parameter.

**Step 3 - Command Validation**: The input is then validated using `WorkoutValidator` class to ensure that the command and parameters provided meet the expected format and criteria for processing.

**Step 4 - Command Execution**: The appropriate action is taken by the `WorkoutManager` class. 
- Creating: If the user wants to create a new workout plan, `WorkoutManager` creates a new `Workout` instance and adds it to the `WorkoutManager's activitySet`.
- Editing: When editing a workout plan, `WorkoutManager` locates the existing `Workout`, updates its details, and then updates the `activitySet` accordingly.
- Deleting: To delete a workout plan, `WorkoutManager` finds the targeted `Workout` in the `activitySet` and removes it.
- Listing: For listing workout plans, `WorkoutManager` retrieves all the workouts from the `activitySet` and formats them into a list for display.
- Searching: Searching is handled by querying the `activitySet` for workouts that match the search criteria provided by the user, and presenting the results.

**Step 5 - Result Display**: After the command is executed, a message indicating the success or failure of the operation is generated and displayed to the user. This feedback is crucial for confirming the effect of the user's command on the system.

Here is the sequence diagram for the `workout /delete LegDay` command to illustrate the five-step process:

![deleteWorkout](diagrams/deleteWorkout.png)

#### [Implemented] Assign and Unassign Workout plan.
The ByteCeps application facilitates workout management, including the assignment and unassignment of exercises to workout plans. The process is outlined in the sequence diagram provided and follows a standard operational pattern as described below:

ByteCeps streamlines the management of exercise-related tasks by following a general multi-step pattern. Here’s how these operations are carried out:

**Step 1 - Input Processing:** 
The user’s input is received and processed by ByteCeps, which involves parsing the command through the `Parser` class. User input examples include:
- `workout /assign Pushups /to LegDay`  to assign the exercise `Pushups` to the workout plan `LegDay`.
- `workout /unassign Pushups /from LegDay` to unassign the exercise `Pushups` to the workout plan `LegDay`.

**Step 2 - Command Identification:** 
The `Parser` class determines the type of workout operation and extracts any necessary parameters. For instance, the `workout /assign` command will be recognized, workout plan name `LegDay` and exercise name `Pushups` will be parsed as the parameter.

**Step 3 - Command Validation**: The input is then validated using `WorkoutValidator` class to ensure that the command and parameters provided meet the expected format and criteria for processing.

**Step 4 - Command Execution**: The appropriate action is taken by the `WorkoutManager` class. 
- Assigning: The `WorkoutManager` calls `executeAssignAction` which initiates the process to assign an exercise to a workout plan. It communicates with the `ExerciseManager` to retrieve the specified `Exercise` object. Simultaneously, it retrieves the specified `Workout` object to which the exercise will be added. The `Workout` object’s `addExercise` method is called to include the exercise within the workout plan.
- Unassigning: The `WorkoutManager` calls `executeUnassignAction` which initiates the process to unassign an exercise to a workout plan. It first retrieves the `Workout` object corresponding to `LegDay` by calling the retrieve method on the `WorkoutManager`. With the `Workout` object obtained, it attempts to find and remove the `Exercise` object representing `Pushups`. If the `Exercise` is present in the `Workout`, it is removed from the workout's exercise list.

**Step 5 - Result Display**: After the command is executed, a message indicating the success or failure of the operation is generated and displayed to the user. This feedback is crucial for confirming the effect of the user's command on the system.

Here is the sequence diagram for the `workout /assign Pushups /to LegDay` command to illustrate the five-step process:

![assignExercise](diagrams/assignExercise.png)

#### [Implemented] List all exercises in a workout plan.

The feature to list all exercises within a specific workout plan is crucial for users to review their workout regimen. This section outlines the sequence of operations triggered by the `workout /info workoutplan` command, culminating in the display of all associated exercises to the user.

**Step 1 - Input Processing:** 
The user’s input is received and processed by ByteCeps, which involves parsing the command through the `Parser` class. The user initiates the process by inputting the command `workout /info workoutplan`.

**Step 2 - Command Identification:** 
The `Parser` class determines the type of workout operation and extracts any necessary parameters. For instance, the `workout /info` command will be recognized, workout plan name `workoutplan` will be parsed as the parameter.

**Step 3 - Command Validation**: The input is then validated using `WorkoutValidator` class to ensure that the command and parameters provided meet the expected format and criteria for processing.

**Step 4 - Command Execution**: The appropriate action is taken by the `WorkoutManager` class. 
- Execute Info Action: The `WorkoutManager` proceeds to execute the `executeInfoAction`, specifically tailored for fetching details about the workout plan named `workoutplan`.
- Retrieve Workout Plan: The `WorkoutManager`retrieves the `Workout` object corresponding to `workoutplan`. The `WorkoutManager` then searches its records and returns the `Workout` object to the `WorkoutManager`.
- Fetch Exercise List: The `WorkoutManager` then invokes the `getExerciseList` method on the retrieved `Workout` object to obtain a list of all exercises included in the workout plan.
- Compile Exercise Information: For each `Exercise` in the list, the `WorkoutManager` calls the `getName` method to retrieve the name of the exercise. These names are compiled into a comprehensive message detailing all exercises within the workout plan.

**Step 5 - Result Display**: After the command is executed, a message indicating the success or failure of the operation is generated and displayed to the user. This feedback is crucial for confirming the effect of the user's command on the system.
- Success Path: The compiled list of exercises is presented to the user, providing a clear overview of the workout plan's contents.
- Validation Failure: If the initial validation fails, the user is informed of the invalid command format without proceeding further into the sequence.

Here is the sequence diagram for the `workout /info workoutplan` command to illustrate the five-step process:

![listExerciseInWorkoutPlan](diagrams/listExerciseInWorkoutPlan.png)


### Program management
#### Assigning a workout to a program
Below is the sequence diagram of the command `program /assign <workout> /to <day>` being run:
![](./diagrams/assignWorkoutToProgram.png)

#### Clearing a day in the program
This is the sequence diagram of the command `program /clear <day [optional]>` being run.
The validation of user input has been omitted for purposes of brevity.
![](./diagrams/clearProgram.png)

### Logging workout management

#### Logging an exercise
Below is the sequence diagram of the command `program /log <EXERCISE_NAME [string]> /weight
 <WEIGHT [integer]> /sets <NUMBER_OF_SETS [integer]> /reps <NUMBER_OF_REPS [integer]> /date <DATE [yyyy-mm-dd]> ` being run: 
![](./diagrams/addExerciseLog.png)



### Help Menu
To implement a help menu for the user, where they can view the formatting of any command corresponding to any specific BYTE-CEPS functionality, 3 classes work together:
- [HelpMenuManager](../src/main/java/byteceps/processing/HelpMenuManager.java) : Returns help menus to be shown to the user or, if requested, a specific functionality's command format.
- [HelpStrings](../src/main/java/byteceps/ui/strings/HelpStrings.java): Stores all Strings including numbered help menu items, command formats and help menu error messages.
- [HelpValidator](../src/main/java/byteceps/validators/HelpValidator.java): Parses the input to HelpMenuManager's execute() method to ensure input validity before the rest of the method executes.


## Product scope
### Target user profile

BYTE-CEPS, a CLI-based all-in-one tool for setting and tracking fitness goals. Whether you're a tech-savvy fitness enthusiast or just starting your fitness journey, BYTE-CEPS offers the simplicity and efficiency of a CLI interface to help you maintain or improve your fitness through self-managed routines.

### Value proposition

ByteCeps offers a streamlined and comprehensive platform to manage exercise routines, track workout progress, and design personalized fitness programs with ease and efficiency for fitness enthusiasts and professionals. 

1. Streamlined Exercise Management: ByteCeps simplifies the organization of exercise routines by providing a user-friendly interface to add, edit, delete, list and search exercises effortlessly.
2. Effortless Workout Planning: Create personalized workout plans by assigning exercises to specific days with intuitive CLI commands, ensuring organized and effective training sessions tailored to your needs.
3. Flexible Program Adaptation: Seamlessly adjust workout plans and schedules as needed with the ability to add, remove, or modify exercises on the fly, providing flexibility and adaptability to your evolving fitness journey.
4. Comprehensive Progress Tracking: Log and monitor workout performance, including weights, sets, and reps, with detailed exercise logs and historical data, enabling you to track progress, identify trends, and stay motivated.

With ByteCeps, achieve your fitness objectives efficiently, effectively, and enjoyably, unlocking your full potential for a healthier, fitter lifestyle.


## User Stories

| Version | As a ... | I want to ...             | So that I can ...                                           |
|---------|----------|---------------------------|-------------------------------------------------------------|
| v1.0    | user     | create an exercise entry             | begin tracking my exercises                      |
| v1.0    | user     | create edit an exercise entry        | modify an exercise to suit my needs              |
| v1.0    | user     | delete an exercise entry             | remove unwanted exercises that I will not do     |
| v1.0    | user     | add an exercise to a workout plan    | customise my workout plan                        |
| v1.0    | user     | edit an exercise in a workout plan   | modify the workout plan to suit my needs         |
| v1.0    | user     | delete an exercise from workout plan | remove unwanted exercises from a workout plan    |
| v1.0    | user     | list all exercises in a workout plan | see the details of my planned exercises          |
| v1.0    | user     | choose the workout plan for a day    | organise and structure my daily workout routine  |
| v1.0    | user     | display my workout for the day       | know what exercises I should be doing today      |
| v1.0    | user     | display my workout for the week      | have a weekly overview of what I should do       |
| v2.0    | user     | export my workout plan to Json       | share with other fitness enthusiasts             |
| v2.0    | user     | import my workout plan to Json       | bring my progress across devices                 |
| v2.0    | user     | search for exercises                 | build my workout plan faster                     |
| v2.0    | user     | search for workout plans             | identify which is the suitable workout for me    |
| v2.0    | fitness enthusiast   | record the amount of weight lifted                             | track my progress over time                                       |
| v2.0    | fitness enthusiast   | track the number of sets performed for each exercise session   | follow my workout plan effectively                                |
| v2.0    | fitness professional | monitor the repetitions completed for each exercise            | evaluate my clients' performance and provide tailored feedback    |
| v2.0    | fitness professional | log my exercise data for a specific date                       | accurately track my progress over time                            |
| v2.0    | fitness professional | view a list of dates on which I have logged exercise entries   | track my consistency and adherence to my workout routine          |
| v2.0    | fitness professional | review specific exercise logs for a particular date            | analyze my workout details and progress on that specific day      |
| v2.1    | fitness professional |  log multiple sets of an exercise, including different weights and reps for each set            |  have a comprehensive log of my exercise sessions to monitor variations in my performance and strength training progress     |
| v2.1    | fitness professional |  access and review historical workout data with detailed breakdowns by exercise, set, weight, and repetition            |  analyze trends in my performance and identify areas for improvement or adjustment in my training regime     |

## Non-Functional Requirements

1. BYTE-CEPS should work on Windows, macOS and Linux that has Java 11 installed.
2. BYTE-CEPS should be able to store data locally.
3. BYTE-CEPS should be able to work offline.
4. BYTE-CEPS should be easy to use.


## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
