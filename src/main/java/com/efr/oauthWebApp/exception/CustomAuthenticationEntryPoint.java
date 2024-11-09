package com.efr.oauthWebApp.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = authException.getMessage();

        if (message.contains("expired")) { // Проверяем, если в сообщении содержится информация об истечении срока действия
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.FORBIDDEN.value());
            request.setAttribute("errorTitle", "Ошибка истечения токена");
            request.setAttribute("errorMessage", "Ваш токен истек. Пожалуйста, войдите снова.");
            request.setAttribute("errorCode", "401"); // Можно использовать 401 для случаев авторизации
        } else {
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.UNAUTHORIZED.value());
            request.setAttribute("errorTitle", "Не авторизован");
            request.setAttribute("errorMessage", "Для доступа к этому ресурсу необходимо войти в систему.");
            request.setAttribute("errorCode", "401");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/error");
        dispatcher.forward(request, response);
    }
}