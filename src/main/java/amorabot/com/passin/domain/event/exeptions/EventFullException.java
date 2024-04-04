package amorabot.com.passin.domain.event.exeptions;

public class EventFullException extends RuntimeException{

    public EventFullException(String message){
        super(message);
    }
}
