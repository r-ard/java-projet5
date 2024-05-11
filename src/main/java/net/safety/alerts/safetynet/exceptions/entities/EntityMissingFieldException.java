package net.safety.alerts.safetynet.exceptions.entities;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class EntityMissingFieldException extends SafetyException {
    public EntityMissingFieldException() {
        this(null, null);
    }
    public EntityMissingFieldException(String fieldName) {
        this(null, fieldName);
    }

    public EntityMissingFieldException(String entityName, String fieldName) {
        super("Missing field " + (fieldName != null ? (fieldName + " ") : "") + "on entity" + (entityName != null ? (" " + entityName) : ""));
    }
}
