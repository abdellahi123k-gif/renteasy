package com.renteasy.renteasy.web;

import com.renteasy.renteasy.service.AdminService;
import com.renteasy.renteasy.service.LocataireDashboardService;
import com.renteasy.renteasy.service.ProprietaireDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardPageController {

    private final AdminService adminService;
    private final ProprietaireDashboardService proprietaireDashboardService;
    private final LocataireDashboardService locataireDashboardService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("dashboard", adminService.getDashboard());
        model.addAttribute("pageTitle", "Dashboard Admin");
        model.addAttribute("viewContent", "pages/dashboard/admin :: content");
        return "layout/base";
    }

    @GetMapping("/proprietaire")
    public String proprietaire(Authentication auth, Model model) {
        model.addAttribute("dashboard",
                proprietaireDashboardService.getDashboard(auth.getName()));
        model.addAttribute("pageTitle", "Dashboard Propriétaire");
        model.addAttribute("viewContent", "pages/dashboard/proprietaire :: content");
        return "layout/base";
    }

    @GetMapping("/locataire")
    public String locataire(Authentication auth, Model model) {
        model.addAttribute("dashboard",
                locataireDashboardService.getDashboard(auth.getName()));
        model.addAttribute("pageTitle", "Dashboard Locataire");
        model.addAttribute("viewContent", "pages/dashboard/locataire :: content");
        return "layout/base";
    }
}
