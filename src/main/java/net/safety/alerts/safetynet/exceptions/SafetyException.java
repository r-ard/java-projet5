package net.safety.alerts.safetynet.exceptions;

public class SafetyException extends Exception {
    public SafetyException(String exception) {
        super("SafetyException : " + exception);
    }

    public SafetyException() {
        super("SafetyException");
    }
}
