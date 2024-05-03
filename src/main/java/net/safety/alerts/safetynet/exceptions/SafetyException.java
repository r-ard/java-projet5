package net.safety.alerts.safetynet.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafetyException extends Exception {
    public SafetyException(String exception) {
        super("SafetyException : " + exception);
        this.getLogger().error(exception);
    }

    public SafetyException() {
        super("SafetyException");
        this.getLogger().error("an error has been thrown");
    }

    private Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
