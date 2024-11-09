package com.efr.oauthWebApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginController {
    @GetMapping
    public String loginPage(){
        return "login";
    }

    @GetMapping("/logout")
    public RedirectView logout() {
        return new RedirectView("/logout");
    }
}
