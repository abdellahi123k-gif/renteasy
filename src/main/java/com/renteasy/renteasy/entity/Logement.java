package com.renteasy.renteasy.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "logements", indexes = {
        @Index(name = "idx_logement_ville", columnList = "ville"),
        @Index(name = "idx_logement_type", columnList = "type"),
        @Index(name = "idx_logement_disponible", columnList = "disponible"),
        @Index(name = "idx_logement_prix", columnList = "prix")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Logement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String ville;

    private String adresse;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private BigDecimal prix;

    private boolean disponible;

    private String imageUrl;

    private String videoUrl;

    private String telephone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "logement", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();
}
