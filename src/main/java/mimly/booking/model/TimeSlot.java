package mimly.booking.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = TimeSlot.TABLE_NAME)
public class TimeSlot implements Serializable {

    public static final String TABLE_NAME = "TimeSlots";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String time;
    @Column(columnDefinition = "boolean default false")
    private Boolean reserved = false;
    private String confirmedBy;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "assistantId", nullable = false)
    private Assistant assistant;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) object;
        return id.equals(timeSlot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
