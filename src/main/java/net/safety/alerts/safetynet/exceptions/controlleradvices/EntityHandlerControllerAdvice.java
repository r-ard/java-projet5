package net.safety.alerts.safetynet.exceptions.controlleradvices;

import jakarta.servlet.http.HttpServletRequest;
import net.safety.alerts.safetynet.exceptions.entities.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EntityHandlerControllerAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ControllerExceptionMessage> entityNotFoundExceptionHandler(HttpServletRequest request, EntityNotFoundException exception) {
        ControllerExceptionMessage exceptionMessage = new ControllerExceptionMessage(request.getRequestURI(), exception.getMessage(), exception.getClass().getName());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ControllerExceptionMessage> entityAlreadyExistsExceptionHandler(HttpServletRequest request, EntityAlreadyExistsException exception) {
        ControllerExceptionMessage exceptionMessage = new ControllerExceptionMessage(request.getRequestURI(), exception.getMessage(), exception.getClass().getName());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityUpdateException.class)
    public ResponseEntity<ControllerExceptionMessage> entityUpdateExceptionHandler(HttpServletRequest request, EntityUpdateException exception) {
        ControllerExceptionMessage exceptionMessage = new ControllerExceptionMessage(request.getRequestURI(), exception.getMessage(), exception.getClass().getName());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityInsertException.class)
    public ResponseEntity<ControllerExceptionMessage> entityInsertExceptionHandler(HttpServletRequest request, EntityInsertException exception) {
        ControllerExceptionMessage exceptionMessage = new ControllerExceptionMessage(request.getRequestURI(), exception.getMessage(), exception.getClass().getName());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityMissingFieldException.class)
    public ResponseEntity<ControllerExceptionMessage> entityMissingFieldExceptionHandler(HttpServletRequest request, EntityMissingFieldException exception) {
        ControllerExceptionMessage exceptionMessage = new ControllerExceptionMessage(request.getRequestURI(), exception.getMessage(), exception.getClass().getName());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }
}
