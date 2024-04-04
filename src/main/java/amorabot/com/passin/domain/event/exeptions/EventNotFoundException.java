package amorabot.com.passin.domain.event.exeptions;

public class EventNotFoundException extends RuntimeException{

    public EventNotFoundException(String message){
        super(message);
    }
}
