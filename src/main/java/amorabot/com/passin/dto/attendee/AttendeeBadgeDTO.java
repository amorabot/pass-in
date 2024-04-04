package amorabot.com.passin.dto.attendee;

public record AttendeeBadgeDTO(
        String name,
        String email,
        String checkinURL,
        String eventID
) {}
