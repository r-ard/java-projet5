package net.safety.alerts.safetynet.exceptions.controlleradvices;

import java.time.LocalDateTime;

public class ControllerExceptionMessage {
    private final LocalDateTime date;
    private final String request;
    private final String message;
    private final String exceptionName;

    public ControllerExceptionMessage(String request, String message, String exceptionName) {
        this(LocalDateTime.now(), request, message, exceptionName);
    }
    public ControllerExceptionMessage(LocalDateTime date, String request, String message, String exceptionName) {
        this.date = date;
        this.request = request;
        this.message = message;
        this.exceptionName = exceptionName;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public String getRequest() {
        return request;
    }

    public String getMessage() {
        return message;
    }

    public String getExceptionName() {
        return exceptionName;
    }
}
