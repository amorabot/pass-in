package amorabot.com.passin.dto.event;

import amorabot.com.passin.domain.event.Event;
import lombok.Getter;

@Getter
public class EventResponseDTO {

    private EventDTO eventData;

    public EventResponseDTO(Event event, Integer numOfAttendees){
        this.eventData = new EventDTO(
                event.getId(),
                event.getTitle(),
                event.getDetails(),
                event.getSlug(),
                event.getMaximumAttendees(),
                numOfAttendees
        );
    }
}
