package com.efr.oauthWebApp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminPage(@AuthenticationPrincipal OAuth2User principal, Model model){

        model.addAttribute("name", principal.getAttribute("given_name"));
        model.addAttribute("surname", principal.getAttribute("family_name"));
        model.addAttribute("email", principal.getAttribute("email"));
        model.addAttribute("avatar", principal.getAttribute("picture")); // Используется картинка из атрибута picture

        model.addAttribute("id", principal.getAttribute("sub"));

        return "admin";
    }
}
