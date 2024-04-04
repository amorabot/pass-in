package amorabot.com.passin.repositories;

import amorabot.com.passin.domain.checkin.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {

    Optional<CheckIn> findByAttendeeId(String attendeeID);
}
