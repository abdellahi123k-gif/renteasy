package com.renteasy.renteasy.service;

import com.renteasy.renteasy.dto.Dashboard.LocataireDashboardDTO;

public interface LocataireDashboardService {

    LocataireDashboardDTO getDashboard(String email);
}
