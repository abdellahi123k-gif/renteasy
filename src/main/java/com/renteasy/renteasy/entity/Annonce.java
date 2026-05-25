package com.renteasy.renteasy.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "annonces")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Column(length = 1000)
    private String description;

    private boolean active;

    private LocalDateTime datePublication;

    @ManyToOne
    @JoinColumn(name = "logement_id")
    private Logement logement;
}