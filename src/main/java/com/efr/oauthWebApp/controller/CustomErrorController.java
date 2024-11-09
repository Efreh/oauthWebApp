package com.efr.oauthWebApp.controller;

import com.efr.oauthWebApp.service.ErrorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class CustomErrorController implements ErrorController {

    @Autowired
    private ErrorService errorService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        errorService.processError(request, model);

        return "error";
    }
}
