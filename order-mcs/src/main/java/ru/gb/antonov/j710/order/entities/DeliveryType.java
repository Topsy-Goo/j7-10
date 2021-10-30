package ru.gb.antonov.j710.order.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity  @Data
public class DeliveryType
{
    @Id  @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column (name="friendly_name", nullable=false, unique = true)
    private String friendlyName;

    //@Column (name="short_name", nullable=false, unique = true)
    //private String shortName;

    @Column (name="cost", nullable=false)
    private BigDecimal cost = BigDecimal.ZERO;

    @CreationTimestamp  @Column(name="created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp  @Column(name="updated_at")
    private LocalDateTime updatedAt;
}
