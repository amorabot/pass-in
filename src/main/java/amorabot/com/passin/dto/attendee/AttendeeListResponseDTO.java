package amorabot.com.passin.dto.attendee;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public record AttendeeListResponseDTO (
        List<AttendeeDetails> attendees
) {
}
