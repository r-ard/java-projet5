package net.safety.alerts.safetynet.exceptions.application;

import net.safety.alerts.safetynet.exceptions.SafetyException;

public class MissingArgumentException extends SafetyException {
    public MissingArgumentException() {
        super("Missing JSON file first argument");
    }
}
