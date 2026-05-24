package com.renteasy.renteasy.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "logements")

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

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private BigDecimal prix;

    private boolean disponible;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}