package com.renteasy.renteasy.service;

import com.renteasy.renteasy.dto.Dashboard.ProprietaireDashboardDTO;

public interface ProprietaireDashboardService {

    ProprietaireDashboardDTO getDashboard(String email);
}
