package net.safety.alerts.safetynet.exceptions.entities;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class EntityUpdateException extends SafetyException {
    public EntityUpdateException(String message) {
        this(null, message);
    }

    public EntityUpdateException(String entityName, String message) {
        super(
                "Failed to update the entity" + (entityName != null ? " " + entityName : "") + (message != null ? (", reason : " + message) : "")
        );
    }

    public EntityUpdateException() {
        this(null, null);
    }
}
