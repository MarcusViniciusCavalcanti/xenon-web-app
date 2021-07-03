package br.com.tsi.utfpr.xenon.domain.user.entity;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cars", indexes = @Index(columnList = "plate", name = "Index_car_plate"))
@Data
public class Car {

    @Id
    private Long id;

    @Column(name = "plate", nullable = false, length = 10, unique = true)
    private String plate;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "document")
    private String document;

    @OneToOne(cascade = CascadeType.REMOVE)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "last_access")
    private LocalDateTime lastAccess;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    private void newCar() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void updateCar() {
        updatedAt = LocalDateTime.now();
    }
}
