package net.safety.alerts.safetynet.exceptions.entities;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class EntityAlreadyExistsException extends SafetyException {
    public EntityAlreadyExistsException() {
        super("Entity already exists");
    }

    public EntityAlreadyExistsException(String entityName) {
        super(entityName + " already exists");
    }
}
