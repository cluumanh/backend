package com.lmc.backend.enity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity<ID> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @Column(updatable = false)
    private Instant created;

    private Instant updated;

    @PrePersist
    protected void onCreate() {
        created = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = Instant.now();
    }
}
