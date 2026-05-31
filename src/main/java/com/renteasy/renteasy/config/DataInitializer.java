package com.renteasy.renteasy.config;

import com.renteasy.renteasy.entity.*;
import com.renteasy.renteasy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final LogementRepository logementRepository;
    private final AnnonceRepository annonceRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() > 0) return;

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role proprioRole = roleRepository.save(Role.builder().name("PROPRIETAIRE").build());
        Role locataireRole = roleRepository.save(Role.builder().name("LOCATAIRE").build());

        User admin = userRepository.save(User.builder()
                .firstName("Admin").lastName("RentEasy")
                .email("admin@renteasy.com")
                .password(passwordEncoder.encode("admin123"))
                .role(adminRole).build());

        User sophie = userRepository.save(User.builder()
                .firstName("Sophie").lastName("Martin")
                .email("sophie.martin@email.com")
                .password(passwordEncoder.encode("password123"))
                .role(proprioRole).build());

        User pierre = userRepository.save(User.builder()
                .firstName("Pierre").lastName("Dupont")
                .email("pierre.dupont@email.com")
                .password(passwordEncoder.encode("password123"))
                .role(proprioRole).build());

        User lucas = userRepository.save(User.builder()
                .firstName("Lucas").lastName("Bernard")
                .email("lucas.bernard@email.com")
                .password(passwordEncoder.encode("password123"))
                .role(locataireRole).build());

        User emma = userRepository.save(User.builder()
                .firstName("Emma").lastName("Petit")
                .email("emma.petit@email.com")
                .password(passwordEncoder.encode("password123"))
                .role(locataireRole).build());

        User youssef = userRepository.save(User.builder()
                .firstName("Youssef").lastName("Alami")
                .email("youssef.alami@email.com")
                .password(passwordEncoder.encode("password123"))
                .role(locataireRole).build());

        Logement l1 = logementRepository.save(Logement.builder()
                .titre("Appartement Belle Vue").description("Bel appartement lumineux avec vue dégagée sur la tour Eiffel. Cuisine équipée, balcon, parking inclus.")
                .ville("Paris").adresse("15 Rue de Rivoli, 75001").type("Appartement")
                .prix(new BigDecimal("1200")).disponible(true).telephone("+33 6 12 34 56 78")
                .owner(sophie).build());

        Logement l2 = logementRepository.save(Logement.builder()
                .titre("Maison de Campagne").description("Grande maison avec jardin, piscine et cheminée. Idéale pour famille. 4 chambres, 2 salles de bain.")
                .ville("Lyon").adresse("8 Chemin des Vignes, 69005").type("Maison")
                .prix(new BigDecimal("2500")).disponible(true).telephone("+33 6 12 34 56 78")
                .owner(sophie).build());

        Logement l3 = logementRepository.save(Logement.builder()
                .titre("Studio Lumineux").description("Studio meublé proche centre-ville et université. Idéal étudiant. Wifi, lave-linge, chauffage individuel.")
                .ville("Bordeaux").adresse("42 Rue Sainte-Catherine, 33000").type("Studio")
                .prix(new BigDecimal("650")).disponible(true).telephone("+33 6 12 34 56 78")
                .owner(sophie).build());

        Logement l4 = logementRepository.save(Logement.builder()
                .titre("Villa Méditerranée").description("Superbe villa avec piscine à débordement et vue mer panoramique. 5 chambres, terrasse, jardin tropical.")
                .ville("Nice").adresse("3 Boulevard de la Mer, 06000").type("Villa")
                .prix(new BigDecimal("3500")).disponible(true).telephone("+33 6 98 76 54 32")
                .owner(pierre).build());

        Logement l5 = logementRepository.save(Logement.builder()
                .titre("Appartement Centre").description("Appartement rénové en plein cœur de Toulouse. Métro à 2 min, commerces, restaurants à proximité.")
                .ville("Toulouse").adresse("28 Rue d'Alsace-Lorraine, 31000").type("Appartement")
                .prix(new BigDecimal("850")).disponible(false).telephone("+33 6 98 76 54 32")
                .owner(pierre).build());

        annonceRepository.save(Annonce.builder()
                .titre("Appartement Belle Vue").description("Annonce pour appartement à Paris").active(true)
                .datePublication(LocalDateTime.now().minusDays(10)).logement(l1).build());

        annonceRepository.save(Annonce.builder()
                .titre("Maison de Campagne").description("Location maison de campagne à Lyon").active(true)
                .datePublication(LocalDateTime.now().minusDays(7)).logement(l2).build());

        annonceRepository.save(Annonce.builder()
                .titre("Studio Bordeaux").description("Studio étudiant à Bordeaux").active(false)
                .datePublication(LocalDateTime.now().minusDays(5)).logement(l3).build());

        annonceRepository.save(Annonce.builder()
                .titre("Villa de Luxe Nice").description("Villa de standing avec piscine à Nice").active(true)
                .datePublication(LocalDateTime.now().minusDays(3)).logement(l4).build());

        annonceRepository.save(Annonce.builder()
                .titre("Appartement Toulouse").description("Bel appartement au centre de Toulouse").active(true)
                .datePublication(LocalDateTime.now().minusDays(1)).logement(l5).build());

        reservationRepository.save(Reservation.builder()
                .dateDebut(LocalDate.now().plusDays(5)).dateFin(LocalDate.now().plusDays(12))
                .status(ReservationStatus.CONFIRMEE).locataire(lucas).logement(l1).build());

        reservationRepository.save(Reservation.builder()
                .dateDebut(LocalDate.now().plusDays(10)).dateFin(LocalDate.now().plusDays(17))
                .status(ReservationStatus.EN_ATTENTE).locataire(emma).logement(l2).build());

        reservationRepository.save(Reservation.builder()
                .dateDebut(LocalDate.now().plusDays(20)).dateFin(LocalDate.now().plusDays(27))
                .status(ReservationStatus.CONFIRMEE).locataire(youssef).logement(l4).build());

        reservationRepository.save(Reservation.builder()
                .dateDebut(LocalDate.now().minusDays(30)).dateFin(LocalDate.now().minusDays(23))
                .status(ReservationStatus.ANNULEE).locataire(lucas).logement(l5).build());
    }
}
