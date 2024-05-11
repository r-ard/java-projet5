package net.safety.alerts.safetynet.exceptions.entities;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class EntityInsertException extends SafetyException {
    public EntityInsertException() {
        super("Failed to insert entity");
    }

    public EntityInsertException(String entityName) {
        super("Failed to insert " + entityName + " entity");
    }
}
