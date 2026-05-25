package com.renteasy.renteasy.service.impl;

import com.renteasy.renteasy.dto.Dashboard.AdminDashboardDTO;
import com.renteasy.renteasy.repository.*;
import com.renteasy.renteasy.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final LogementRepository logementRepository;
    private final ReservationRepository reservationRepository;
    private final AnnonceRepository annonceRepository;

    @Override
    public AdminDashboardDTO getDashboard() {

        return AdminDashboardDTO.builder()
                .totalUsers(userRepository.count())
                .totalLogements(logementRepository.count())
                .totalReservations(reservationRepository.count())
                .totalAnnonces(annonceRepository.count())
                .build();
    }
}