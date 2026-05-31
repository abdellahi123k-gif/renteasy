package com.renteasy.renteasy.service.impl;

import com.renteasy.renteasy.dto.Dashboard.LocataireDashboardDTO;
import com.renteasy.renteasy.entity.ReservationStatus;
import com.renteasy.renteasy.entity.User;
import com.renteasy.renteasy.repository.ReservationRepository;
import com.renteasy.renteasy.repository.UserRepository;
import com.renteasy.renteasy.service.LocataireDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocataireDashboardServiceImpl implements LocataireDashboardService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public LocataireDashboardDTO getDashboard(String email) {
        User locataire = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long myReservations = reservationRepository.countByLocataire_Id(locataire.getId());
        long activeReservations = reservationRepository.countByLocataire_IdAndStatus(
                locataire.getId(), ReservationStatus.CONFIRMEE
        );

        return LocataireDashboardDTO.builder()
                .myReservations(myReservations)
                .activeReservations(activeReservations)
                .build();
    }
}
