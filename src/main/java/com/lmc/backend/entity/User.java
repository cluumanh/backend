package com.lmc.backend.entity;

import com.lmc.backend.constant.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity()
@Table(name = "users",
        indexes = {
                @Index(name = "idx_user_status", columnList = "status"),
                @Index(name = "idx_user_created", columnList = "created"),
                @Index(name = "idx_user_username", columnList = "username"),
                @Index(name = "idx_user_status_created", columnList = "status, created")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_username", columnNames = "username"
                )
        })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity<Long> {
    private String username;
    private String password;
    private String email;
    private String status;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", length = 50)
    private Set<Role> roles = new HashSet<>();
}
