package byteceps.commands;

import byteceps.errors.Exceptions;
import byteceps.ui.strings.ManagerStrings;
import byteceps.ui.strings.UiStrings;

import java.util.HashMap;

public class Parser {
    private String command;
    private InputArguments commandAction;
    private HashMap<String, String> additionalArguments;

    public Parser() {
        flush();
    }

    private void flush() {
        command = "";
        commandAction = null;
        additionalArguments = new HashMap<>();
    }

    //@@author pqienso
    public void parseInput(String line) throws Exceptions.InvalidInput {
        // flush the old input
        flush();

        //@@author joshualeejunyi
        assert command.isEmpty() : "Command should be empty after flush";
        assert commandAction == null : "CommandAction should be null after flush";
        assert additionalArguments.isEmpty() : "AdditionalArguments should be empty after flush";

        //@@author pqienso
        int indexOfFirstSlash = line.indexOf('/');

        // input does not have parameters
        if (indexOfFirstSlash == -1) {
            command = line.trim().toLowerCase();
            return;
        }

        command = line.substring(0, indexOfFirstSlash).trim().toLowerCase();
        String[] argumentKeyValuePairs = line.substring(indexOfFirstSlash + 1).split("/");

        long numberOfSlashes = line.chars().filter(ch -> ch == '/').count();

        if (numberOfSlashes != argumentKeyValuePairs.length) {
            throw new Exceptions.InvalidInput(UiStrings.MORE_SLASHES_THAN_ARGS);
        }

        for (String keyValuePair : argumentKeyValuePairs) {
            String[] currentKV = keyValuePair.split( " ", 2);
            String flag = currentKV[0].trim();

            String parameter;
            if (currentKV.length > 1) {
                parameter = currentKV[1].trim();
            } else {
                parameter = "";
            }

            if (commandAction == null) {
                commandAction = new InputArguments(flag, parameter);
            } else {
                additionalArguments.put(flag, parameter);
            }
        }
    }

    public String getCommand() {
        return command;
    }

    public String getAction() throws Exceptions.InvalidInput {
        if (commandAction == null) {
            throw new Exceptions.InvalidInput(ManagerStrings.NO_ACTION_EXCEPTION);
        }
        return commandAction.getFlag();
    }

    public String getActionParameter() {
        return commandAction.getParameter();
    }

    public int getAdditionalArgumentsLength() {
        return additionalArguments.size();
    }

    public boolean hasAdditionalArguments() {
        return !additionalArguments.isEmpty();
    }

    public String getAdditionalArguments(String key) {
        String value = additionalArguments.get(key);
        if (value == null) {
            return "";
        }

        return value;
    }

    public int getNumAdditionalArguments() {
        return additionalArguments.size();
    }

    //@@author pqienso
    @Override
    public String toString() {
        return "COMMAND: " + System.lineSeparator() + command + System.lineSeparator() +
                "ARGUMENTS: " + System.lineSeparator() + additionalArguments.toString();
    }
}
