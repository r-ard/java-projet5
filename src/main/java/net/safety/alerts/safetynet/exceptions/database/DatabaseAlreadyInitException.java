package net.safety.alerts.safetynet.exceptions.database;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class DatabaseAlreadyInitException extends SafetyException {
    public DatabaseAlreadyInitException() {
        super("Database already initialized");
    }
}
