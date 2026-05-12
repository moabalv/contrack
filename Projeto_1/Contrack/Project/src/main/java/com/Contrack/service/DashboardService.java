package com.Contrack.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.Contrack.dto.Dashboard.DashboardResponseDTO;

public interface DashboardService {
    public DashboardResponseDTO getDashboardInfo(UserDetails userDetails);
}
