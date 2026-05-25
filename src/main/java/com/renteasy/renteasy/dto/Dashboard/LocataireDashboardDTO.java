package com.renteasy.renteasy.dto.Dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocataireDashboardDTO {

    private long myReservations;
    private long activeReservations;
}