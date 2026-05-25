package com.renteasy.renteasy.dto.Dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProprietaireDashboardDTO {

    private long myLogements;
    private long myAnnonces;
    private long myReservations;
}