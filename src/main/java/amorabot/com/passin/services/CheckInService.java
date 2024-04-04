package amorabot.com.passin.services;

import amorabot.com.passin.domain.attendee.Attendee;
import amorabot.com.passin.domain.checkin.CheckIn;
import amorabot.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import amorabot.com.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;

    public void registerAttendeeCheckIn(Attendee attendee){
        this.validateCheckIn(attendee.getId());

        //If it's valid:
        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());

        this.checkInRepository.save(newCheckIn);
    }
    public Optional<CheckIn> getCheckIn(String attendeeID){
        return this.checkInRepository.findByAttendeeId(attendeeID);
    }

    private void validateCheckIn(String attendeeID){
        Optional<CheckIn> fetchedCheckin = this.getCheckIn(attendeeID);
        if (fetchedCheckin.isPresent()){
            throw new CheckInAlreadyExistsException("Invalid checkin. Attendee( " + attendeeID + " ) already checked in.");
        }
    }
}
