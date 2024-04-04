package amorabot.com.passin.config;


import amorabot.com.passin.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import amorabot.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import amorabot.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import amorabot.com.passin.domain.event.exeptions.EventFullException;
import amorabot.com.passin.domain.event.exeptions.EventNotFoundException;
import amorabot.com.passin.dto.general.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //Indicates to Spring that this class is responsible for handling exceptions at the controller layer
public class ExceptionEntityHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity handleEventNotFound(EventNotFoundException exception){
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException exception){
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(AttendeeAlreadyRegisteredException.class)
    public ResponseEntity handleAttendeeAlreadyRegistered(AttendeeAlreadyRegisteredException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }


    @ExceptionHandler(EventFullException.class)
    public ResponseEntity handleEventFull(EventFullException exception){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(exception.getMessage()));
    }


    @ExceptionHandler(CheckInAlreadyExistsException.class)
    public ResponseEntity handleEventNotFound(CheckInAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
