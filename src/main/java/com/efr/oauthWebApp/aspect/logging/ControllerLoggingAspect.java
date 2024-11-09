package com.efr.oauthWebApp.aspect.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    // Логируем успешную аутентификацию
    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        System.out.println("Успешная аутентификация пользователя: " + username);
    }

    // Логируем выход из системы
    @EventListener
    public void logoutSuccess(LogoutSuccessEvent event){
        String username = event.getAuthentication().getName();
        System.out.println("Успешный выход из пользователя: " + username);
    }

    //Точка среза для перехвата метода revokeToken
    @Pointcut("execution(* com.efr.oauthWebApp.service.OAuth2TokenService.revokeToken(..)) && args(token)")
    public void revokeTokenMethod(String token) {}

    // Логируем перед выполнением метода
    @Around("revokeTokenMethod(token)")
    public Object logRevokeToken(ProceedingJoinPoint joinPoint, String token) throws Throwable {
        logger.info("Attempting to revoke token: {}", token);

        Object result = joinPoint.proceed();

        logger.info("Token revoked successfully: {}", token);
        return result;
    }

}
