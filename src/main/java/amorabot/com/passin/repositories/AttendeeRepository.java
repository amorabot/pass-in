package amorabot.com.passin.repositories;

import amorabot.com.passin.domain.attendee.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {

    List<Attendee> findByEventId(String eventID); //JPA infers the method functionality due to naming conventions
    Optional<Attendee> findByEventIdAndEmail(String eventID, String email);
}
