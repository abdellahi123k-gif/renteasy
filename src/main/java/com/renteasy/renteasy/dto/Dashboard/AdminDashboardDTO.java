package com.renteasy.renteasy.dto.Dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardDTO {

    private long totalUsers;
    private long totalLogements;
    private long totalReservations;
    private long totalAnnonces;
}