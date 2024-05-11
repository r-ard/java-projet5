package net.safety.alerts.safetynet.exceptions.entities;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class EntityNotFoundException extends SafetyException {
    public EntityNotFoundException() {
        super("Entity not found");
    }

    public EntityNotFoundException(String entityName) {
        super(entityName + " entity not found");
    }
}
