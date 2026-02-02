package com.lmc.backend.enity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "refresh_tokens",
        indexes = {
                @Index(name = "idx_token", columnList = "token", unique = true),
                @Index(name = "idx_user_revoked", columnList = "user_id, revoked"),
                @Index(name = "idx_expiry_date", columnList = "expiryDate")
        }
)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseEntity<Long>{
    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    @Builder.Default
    private boolean revoked = false;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;
}
