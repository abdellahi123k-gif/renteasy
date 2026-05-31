package com.renteasy.renteasy.service.impl;

import com.renteasy.renteasy.dto.Dashboard.ProprietaireDashboardDTO;
import com.renteasy.renteasy.entity.User;
import com.renteasy.renteasy.repository.AnnonceRepository;
import com.renteasy.renteasy.repository.LogementRepository;
import com.renteasy.renteasy.repository.ReservationRepository;
import com.renteasy.renteasy.repository.UserRepository;
import com.renteasy.renteasy.service.ProprietaireDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProprietaireDashboardServiceImpl implements ProprietaireDashboardService {

    private final UserRepository userRepository;
    private final LogementRepository logementRepository;
    private final AnnonceRepository annonceRepository;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public ProprietaireDashboardDTO getDashboard(String email) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long myLogements = logementRepository.countByOwner_Id(owner.getId());
        long myAnnonces = annonceRepository.countByLogementOwnerId(owner.getId());
        long myReservations = reservationRepository.countByLogement_Owner_Id(owner.getId());

        return ProprietaireDashboardDTO.builder()
                .myLogements(myLogements)
                .myAnnonces(myAnnonces)
                .myReservations(myReservations)
                .build();
    }
}
