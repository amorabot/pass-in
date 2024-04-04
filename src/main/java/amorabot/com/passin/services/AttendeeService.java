package amorabot.com.passin.services;

import amorabot.com.passin.domain.attendee.Attendee;
import amorabot.com.passin.domain.attendee.exceptions.AttendeeAlreadyRegisteredException;
import amorabot.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import amorabot.com.passin.domain.checkin.CheckIn;
import amorabot.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import amorabot.com.passin.dto.attendee.AttendeeDetails;
import amorabot.com.passin.dto.attendee.AttendeeListResponseDTO;
import amorabot.com.passin.dto.attendee.AttendeeBadgeDTO;
import amorabot.com.passin.repositories.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository; //MUST BE FINAL -> Signals necessity to Lombok
    private final CheckInService checkInService;

    public Attendee registerAttendee(Attendee newAttendee){
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
        //Attendee validation when attempting to join a event is handled in the EventService class
    }
    public boolean verifyAttendeeSubscription(String email, String eventID){
        Optional<Attendee> registeredAttendee = this.attendeeRepository.findByEventIdAndEmail(eventID, email); //If exists -> Invalid for subsequent registrations
        if (registeredAttendee.isPresent()){throw new AttendeeAlreadyRegisteredException("Invalid Attendee: Already registered on this event.");}
        return false;
    }

    public List<Attendee> getAllAttendeesFromEvent(String eventID){
        return this.attendeeRepository.findByEventId(eventID);
    }

    public AttendeeListResponseDTO getAttendeesAtEvent(String eventID){
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventID);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(currentAttendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(currentAttendee.getId());
            LocalDateTime checkedInAt;
            if (checkIn.isPresent()) {checkedInAt = checkIn.get().getCreatedAt();} else {checkedInAt = null;}
            /*
            Now that we have a value for checkedInAt, lets return the AttendeeDetails so the stream can map
            a Attendee to a AttendeeDetails object that will be appended to the list
             */
            return new AttendeeDetails(currentAttendee.getId(), currentAttendee.getName(), currentAttendee.getEmail(), currentAttendee.getCreatedAt(), checkedInAt);
        }).toList();

        return new AttendeeListResponseDTO(attendeeDetailsList);
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeID, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = this.getAttendee(attendeeID);

        String uriString = uriComponentsBuilder.path("/attendees/{attendeeID}/check-in").buildAndExpand(attendee.getId()).toUri().toString();

        AttendeeBadgeDTO badgeData = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uriString, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(badgeData);
    }

    public void checkInAttendee(String attendeeID){
        /*
        The attendee checkin methods are being implemented here because, before creating the checkin, some validations need to be made, and those
        involve attendee procedures
         */
        Attendee attendee = this.getAttendee(attendeeID);
        //Once its valid, and fetched, we can access the checkin service
        this.checkInService.registerAttendeeCheckIn(attendee);
    }
    private Attendee getAttendee(String attendeeID){
        Attendee attendee = this.attendeeRepository.findById(attendeeID).orElseThrow(()-> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeID));
        return attendee;
    }
}
