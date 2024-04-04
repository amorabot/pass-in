package amorabot.com.passin.domain.checkin;

import amorabot.com.passin.domain.attendee.Attendee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkins")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckIn {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "attendee_id", nullable = false)
    private Attendee attendee;
}
