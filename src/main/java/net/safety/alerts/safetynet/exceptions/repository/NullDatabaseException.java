package net.safety.alerts.safetynet.exceptions.repository;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class NullDatabaseException extends SafetyException {
    public NullDatabaseException() {
        super("JSON Database instance is null, it might not be initialized");
    }
}
