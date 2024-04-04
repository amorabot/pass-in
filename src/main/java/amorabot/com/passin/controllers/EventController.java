package amorabot.com.passin.controllers;

import amorabot.com.passin.dto.attendee.AttendeeIdDTO;
import amorabot.com.passin.dto.attendee.AttendeeListResponseDTO;
import amorabot.com.passin.dto.attendee.AttendeeRequestDTO;
import amorabot.com.passin.dto.event.EventDTO;
import amorabot.com.passin.dto.event.EventIdDTO;
import amorabot.com.passin.dto.event.EventRequestDTO;
import amorabot.com.passin.dto.event.EventResponseDTO;
import amorabot.com.passin.services.AttendeeService;
import amorabot.com.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/events")

@RequiredArgsConstructor
public class EventController {
    //Will be responsible for anything to do with Event entries

    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{eventID}")// <<--- Path var. names must match ---VV
    public ResponseEntity<EventDTO> getEvent(@PathVariable String eventID){

        EventResponseDTO eventResponseDTO = this.eventService.getEventData(eventID);
        return ResponseEntity.ok(eventResponseDTO.getEventData());
    }
    @GetMapping("/attendees/{eventID}")
    public ResponseEntity<AttendeeListResponseDTO> getEventAttendees(@PathVariable String eventID){

        AttendeeListResponseDTO attendeeListDTO = this.attendeeService.getAttendeesAtEvent(eventID);
        return ResponseEntity.ok().body(attendeeListDTO);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO createdEventID = this.eventService.createEvent(body);

        URI uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(createdEventID.eventID()).toUri();

        /*
        Created (Cod. 201) means a resource has been successfully created
        Giving the URI to the newly created resource is a good practice/habit being applied
         */
        return ResponseEntity.created(uri).body(createdEventID);
    }
    @PostMapping("/{eventID}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventID, @RequestBody AttendeeRequestDTO attendeeData, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO newlyAddedAttendeeID = this.eventService.registerAttendeeOnEvent(eventID, attendeeData);

        URI uri = uriComponentsBuilder.path("/attendees/{attendeeID}/badge").buildAndExpand(newlyAddedAttendeeID.attendeeID()).toUri();

        return ResponseEntity.created(uri).body(newlyAddedAttendeeID);
    }

}
