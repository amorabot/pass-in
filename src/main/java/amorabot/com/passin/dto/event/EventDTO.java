package amorabot.com.passin.dto.event;

public record EventDTO(
        String id,
        String title,
        String details,
        String slug,
        Integer maxAttendees,
        Integer attendeesAmount
) {
}
