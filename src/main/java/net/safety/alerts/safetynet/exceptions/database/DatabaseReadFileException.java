package net.safety.alerts.safetynet.exceptions.database;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class DatabaseReadFileException extends SafetyException {
    private String fileName;

    public DatabaseReadFileException(String fileName) {
        super("Failed to read <" + fileName + "> file");

        this.fileName = fileName;
    }

    public String getFileName() { return this.fileName; }
}
