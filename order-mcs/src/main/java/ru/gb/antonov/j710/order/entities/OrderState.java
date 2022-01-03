package ru.gb.antonov.j710.order.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table (name="orderstates")
public class OrderState {

    @Id    @Column (name="id")    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name="short_name", nullable=false, unique=true)
    private String shortName;       //NONE, PENDING, SERVING, PAYED, CANCELED;

    @Column (name="friendly_name", nullable=false, unique=true)
    private String friendlyName;

    @CreationTimestamp    @Column(name="created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp    @Column (name="updated_at")
    private LocalDateTime updatedAt;
//---------------------------------------------------------------------
    private void setId (Integer value) { id = value; }
    private void setUpdatedAt (LocalDateTime value) { updatedAt = value; }
    private void setCreatedAt (LocalDateTime value) { createdAt = value; }
//---------------------------------------------------------------------
    @Override public String toString () {
        return String.format("ost:[%d, %s, %s]", id, shortName, friendlyName);
    }
}
