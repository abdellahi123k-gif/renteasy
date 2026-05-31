package com.renteasy.renteasy.web;

import com.renteasy.renteasy.entity.User;
import com.renteasy.renteasy.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.renteasy.renteasy.web")
@RequiredArgsConstructor
public class WebControllerAdvice {

    private final SecurityUtils securityUtils;

    @ModelAttribute
    public void addCurrentUserInfo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            model.addAttribute("currentUserFirstName", "");
            model.addAttribute("currentUserLastName", "");
            model.addAttribute("currentUserInitials", "");
            return;
        }
        try {
            User user = securityUtils.getCurrentUser();
            String firstInitial = user.getFirstName().isEmpty() ? "" : String.valueOf(user.getFirstName().charAt(0));
            String lastInitial = user.getLastName().isEmpty() ? "" : String.valueOf(user.getLastName().charAt(0));
            model.addAttribute("currentUserFirstName", user.getFirstName());
            model.addAttribute("currentUserLastName", user.getLastName());
            model.addAttribute("currentUserInitials", (firstInitial + lastInitial).toUpperCase());
        } catch (Exception e) {
            model.addAttribute("currentUserFirstName", "");
            model.addAttribute("currentUserLastName", "");
            model.addAttribute("currentUserInitials", "");
        }
    }
}
