package amorabot.com.passin.controllers;

import amorabot.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import amorabot.com.passin.services.AttendeeService;
import amorabot.com.passin.services.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    private final AttendeeService attendeeService;
//    private final CheckInService checkInService;

    @GetMapping("/{attendeeID}/badge")
    public ResponseEntity<AttendeeBadgeResponseDTO> getAttendeeBadge(@PathVariable String attendeeID, UriComponentsBuilder uriComponentsBuilder){
        AttendeeBadgeResponseDTO attendeeBadgeDTO = this.attendeeService.getAttendeeBadge(attendeeID, uriComponentsBuilder);
         return ResponseEntity.ok(attendeeBadgeDTO);
    }

    @PostMapping("/{attendeeID}/check-in")
    public ResponseEntity createCheckIn(@PathVariable String attendeeID, UriComponentsBuilder uriComponentsBuilder){
        this.attendeeService.checkInAttendee(attendeeID);

        URI uri = uriComponentsBuilder.path("/attendees/{attendeeID}/badge").buildAndExpand(attendeeID).toUri();

        return ResponseEntity.created(uri).build();
    }
}
