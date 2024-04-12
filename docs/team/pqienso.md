# Lim Yu An - Project Portfolio Page

## Project: ByteCeps
BYTE-CEPS is a CLI-based tool for setting and tracking fitness goals.
The user interacts with the tool using commands entered via the CLI interface. With BYTE-CEPS, they can compile a list of exercises, build custom workouts, assign workouts to a weekly schedule and log details of each exercise completed in each performed workout.

BYTE-CEPS is written in Java, and has approximately 5.6 kLoC.

### Summary of Contributions
All code contributed can be seen on the tP dashboard
 [here](https://nus-cs2113-ay2324s2.github.io/tp-dashboard/?search=&sort=totalCommits%20dsc&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2024-02-23&tabOpen=true&tabType=authorship&tabAuthor=pqienso&tabRepo=AY2324S2-CS2113-F14-3%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false).

#### Functional code
  - Created initial iteration of `Parser` class
    - Created the `Parser` class that takes in user input and outputs the command actions
      arguments in the form of a `HashSet`.
    - Created the corresponding JUnit tests.
  - Created initial iteration of `UserInterface` class
    - Created the `UserInterface` class that is responsible for taking in user input and
      printing to `System.out`.
  - Added the `program` functionality.
    - Created the class `WeeklyProgramManager` that handles all commands starting with
      `program`. These commands allow the user to assign specific workouts to specific
      days of the week, and view their workouts for the day or week.
  - Added the functionality of saving user data to a `.json` file
    - Created the initial iteration of the `Storage` class which loads and saves user data
      to and from a `.json` file using [this JSON-java library](https://github.com/stleary/JSON-java).
  - Added the ability for `ByteCeps` to handle cascading deletions
    - Created the `CascadingDeletionManager` class that checks for cascading deletions
      (eg. when an exercise assigned to a specific workout is deleted from `ByteCeps` by the user)
      , allowing for the .json files to load properly after such deletions.
  - Contributed to `Validator` classes
    - Added validation methods in the `Validator` classes as part of refactoring efforts
      by abstracting out user input validation from `ActivityManager` classes.

#### Release management
  - Managed releases [`v1.0`](https://github.com/AY2324S2-CS2113-F14-3/tp/releases/tag/v1.0),
   [`v2.0`](https://github.com/AY2324S2-CS2113-F14-3/tp/releases/tag/v2.0)

#### User Guide contributions
  - Added precautions of cascading deletions to user guide
  - Contributed to summary table of all commands

#### Developer Guide contributions
  - Created class diagrams of all `Activity` classes and `ActivityManager` classes.
  - Created sequence diagrams for running `WeeklyProgramManager` commands, like `program /assign ...`,
    `program /clear ...` and `program /log...` commands.

#### Contributions to DG: extracts
`Activity` class diagram:\
![](../diagrams/ActivityClassDiagram.png)\
`ActivityManager` class diagram:\
![](../diagrams/ActivityManagerClassDiagram.png)\
`program /assign ...` sequence diagram: \
![](../diagrams/assignWorkoutToProgram.png)\
`program /log ...` sequence diagram:\
![](../diagrams/addExerciseLog.png)\
`program /clear ...` sequence diagram:\
![](../diagrams/clearProgram.png)\