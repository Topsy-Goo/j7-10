package ru.gb.antonov.j710.users.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity    @Data    @Table (name="ourpermissions")
public class OurPermission
{
    @Id    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column (name="name", nullable=false, unique=true)
    private String name;

    @CreationTimestamp    @Column(name="created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp    @Column(name="updated_at")
    private LocalDateTime updatedAt;
//------------------------------------------------------------
    @Override public boolean equals (Object o)
    {
        if (this == o)   return true;
        if (o == null || getClass () != o.getClass ())   return false;
        OurPermission ourPermission = (OurPermission) o;
        return name.equals (ourPermission.name);
    }

    @Override public int hashCode () { return Objects.hash (name); }
}
