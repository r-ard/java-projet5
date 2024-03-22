package net.safety.alerts.safetynet.exceptions.database;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class DatabaseParseFileException extends SafetyException {
    private String fileName;

    public DatabaseParseFileException(String fileName) {
        super("Failed to parse <" + fileName + "> file, invalid format");

        this.fileName = fileName;
    }

    public String getFileName() { return this.fileName; }
}
