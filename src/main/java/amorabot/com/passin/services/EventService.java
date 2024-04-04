package amorabot.com.passin.services;

/*
The service layers serves purpose because it still gives
the View layer access to data while shielding the data-access layer
from direct access

Typically, Services, Repository implementations and Controllers build the Controller layer
 */

import amorabot.com.passin.domain.attendee.Attendee;
import amorabot.com.passin.domain.event.Event;
import amorabot.com.passin.domain.event.exeptions.EventFullException;
import amorabot.com.passin.domain.event.exeptions.EventNotFoundException;
import amorabot.com.passin.dto.attendee.AttendeeIdDTO;
import amorabot.com.passin.dto.attendee.AttendeeRequestDTO;
import amorabot.com.passin.dto.event.EventIdDTO;
import amorabot.com.passin.dto.event.EventRequestDTO;
import amorabot.com.passin.dto.event.EventResponseDTO;
import amorabot.com.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor //Gera o construtor com os parametros que necessitam inicialização (Repositories, por exemplo)
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    private Event getEventByID(String eventID){
        Event fetchedEvent = this.eventRepository.findById(eventID).orElseThrow( () ->
                new EventNotFoundException("No event found with ID: " + eventID)
        );
        return fetchedEvent;
    }

    public EventResponseDTO getEventData(String eventID){
        Event fetchedEvent = this.getEventByID(eventID);
        List<Attendee> registeredAttendees = this.attendeeService.getAllAttendeesFromEvent(eventID);
        return new EventResponseDTO(fetchedEvent, registeredAttendees.size());
    }

    public EventIdDTO createEvent(EventRequestDTO newEventData){ //Title details maxAtten.
        Event newEvent = new Event();
        newEvent.setTitle(newEventData.title());
        newEvent.setDetails(newEventData.details());
        newEvent.setMaximumAttendees(newEventData.maxAttendees());
        newEvent.setSlug(generateEventSlug(newEventData.title()));

        this.eventRepository.save(newEvent);
        /*
        Once the repository successfully saved that object as a entity, auto-generated data (in this case, ID)
        is filled in and can be accessed right away, with no need to query the updated entry for that entity

        So, for clear signaling of whats happening, we want to return SPECIFICALLY a Event ID. Thus the need for
        a specific DTO. (If we returned, loosely, a String object, it could cause problems/ambiguity
         */
        return new EventIdDTO(newEvent.getId());
    }
    private String generateEventSlug(String text){
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalizedText
                .replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]","")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("[\\s+]", "-")
                .toLowerCase();
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventID, AttendeeRequestDTO newAttendeeData){
        /*
        Check if the that user's email is already registered on a particular event
        If the check fails, a error is thrown inside #verifyAttendeeSubscription()
        and the register method stops
         */

        this.attendeeService.verifyAttendeeSubscription(newAttendeeData.email(), eventID);

        //Same logic as getEventData(), but getting the event data here will server another purpose
        Event fetchedEvent = this.getEventByID(eventID);
        List<Attendee> registeredAttendees = this.attendeeService.getAllAttendeesFromEvent(eventID);

        if (registeredAttendees.size() < fetchedEvent.getMaximumAttendees()){
            //There is room for a new attendee
            Attendee newAttendee = new Attendee();
            newAttendee.setName(newAttendeeData.name());
            newAttendee.setEmail(newAttendeeData.email());
            newAttendee.setEvent(fetchedEvent);
            newAttendee.setCreatedAt(LocalDateTime.now());

            this.attendeeService.registerAttendee(newAttendee);
            //Once the register method has gone through, newAttendee is a persistant entity and it's ID can be accessed
            return new AttendeeIdDTO(newAttendee.getId());

        } else { //Not using a early return for clarity's sake
            //Throw a EventFullException
            throw new EventFullException("This event is full. The attendee could not be registered");
        }
    }
}
