package com.WilsonQdop.Chamadas.controllers;

import com.WilsonQdop.Chamadas.exceptions.*;
import com.WilsonQdop.Chamadas.exceptions.messageException.PatternMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerExceptionController {

    @ExceptionHandler(CalledIsPaidException.class)
    public ResponseEntity<PatternMessageException> calledsIsPaidException (CalledIsPaidException ex) {
        PatternMessageException message = new PatternMessageException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(CalledAssignmentException.class)
    public ResponseEntity<PatternMessageException> calledAssignment (CalledAssignmentException ex) {
        PatternMessageException message = new PatternMessageException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(message);
    }
    @ExceptionHandler(CalledNotFoundException.class)
    public ResponseEntity<PatternMessageException> calledNotFound (CalledNotFoundException ex) {
        PatternMessageException message = new PatternMessageException(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<PatternMessageException> CustomerNotFound (CustomerNotFoundException ex) {
        PatternMessageException message = new PatternMessageException(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
    @ExceptionHandler(TechnicalNotFoundException.class)
    public ResponseEntity<PatternMessageException> TechnicalNotFound (TechnicalNotFoundException ex) {
        PatternMessageException message = new PatternMessageException(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<PatternMessageException> Business (BusinessException ex) {
        PatternMessageException message = new PatternMessageException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
    @ExceptionHandler(TechnicalIsNotOwnerException.class)
    public ResponseEntity<PatternMessageException> technicalIsNotOwner (TechnicalIsNotOwnerException ex) {
        PatternMessageException message = new PatternMessageException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
