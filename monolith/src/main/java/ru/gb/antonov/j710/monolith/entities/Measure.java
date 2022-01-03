package ru.gb.antonov.j710.monolith.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@Table (name="measures")
public class Measure {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column (name="name", nullable=false, unique=true)
    private String name;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp    @Column(name="updated_at")
    private LocalDateTime updatedAt;
//------------------------------------------------------------ конструкторы
    private Measure () {}
//------------------------------------------------------------ геттеры и сеттеры
    private void setId (Long value) { id = value; }
    private void setUpdatedAt (LocalDateTime value) { updatedAt = value; }
    private void setCreatedAt (LocalDateTime value) { createdAt = value; }
//------------------------------------------------------------ @Overrides
    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Measure measure = (Measure) o;
        return name.equals(measure.name);
    }

    @Override
    public int hashCode () {
        return Objects.hash(name);
    }

    @Override public String toString () { return String.format ("Measure[%d, %s]", id, name); }
//------------------------------------------------------------ */

    public static boolean isMeasureValid (Measure value) {
        String name;
        return value != null
               &&  (name = value.getName()) != null
               &&  !name.trim().isEmpty();
    }
}
