package amorabot.com.passin.domain.attendee;

import amorabot.com.passin.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "attendees")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attendee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    /*
    One event can hold multiple attendees
    A attendee can only attend to one event
        >The reference to a attendee's event goes here
        >It's also necessary to define the event's list of attendees on it's jakarta Entity class
     */
    @ManyToOne()
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;
}
