package net.safety.alerts.safetynet.exceptions.utils;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class EntityUpdateException extends SafetyException {
    public EntityUpdateException(String message) {
        super(
                "Failed to update the entity" + (message != null ? (", reason : " + message) : "")
        );
    }

    public EntityUpdateException() {
        this(null);
    }
}
