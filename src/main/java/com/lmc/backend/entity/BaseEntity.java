package com.lmc.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

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
        updated = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = Instant.now();
    }
}
