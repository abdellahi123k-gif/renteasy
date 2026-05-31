package com.renteasy.renteasy.web;

import com.renteasy.renteasy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserPageController {

    private final UserService userService;

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model) {
        var user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Profil");
        model.addAttribute("viewContent", "pages/user/profile :: content");
        return "layout/base";
    }
}
