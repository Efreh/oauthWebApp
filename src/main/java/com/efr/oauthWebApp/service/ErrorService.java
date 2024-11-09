package com.efr.oauthWebApp.service;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ErrorService {

    public void processError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorCode = (String) request.getAttribute("errorCode");
        String errorTitle = (String) request.getAttribute("errorTitle");
        String errorMessage = (String) request.getAttribute("errorMessage");

        if (status != null) {
            if (status.equals(HttpStatus.NOT_FOUND.value())) {
                model.addAttribute("errorTitle", "Страница не найдена");
                model.addAttribute("errorMessage", "Запрашиваемая страница не существует.");
            } else if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR.value())) {
                model.addAttribute("errorTitle", "Произошла ошибка");
                model.addAttribute("errorMessage", "Внутренняя ошибка сервера.");
            } else if (status.equals(HttpStatus.UNAUTHORIZED.value())) {
                model.addAttribute("errorTitle", "Не авторизован");
                model.addAttribute("errorMessage", "Для доступа к этому ресурсу необходимо войти в систему.");
            } else if (status.equals(HttpStatus.FORBIDDEN.value())) {
                model.addAttribute("errorTitle", errorTitle);
                model.addAttribute("errorMessage", errorMessage);
            } else {
                model.addAttribute("errorTitle", "Неизвестная ошибка");
                model.addAttribute("errorMessage", "Неизвестная ошибка.");
            }
        }

        model.addAttribute("errorCode", errorCode);
    }
}
