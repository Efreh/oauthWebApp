package com.efr.oauthWebApp.config.security;

import com.efr.oauthWebApp.service.OAuth2TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class OAuth2LogoutHandler implements LogoutHandler {

    private final OAuth2TokenService tokenService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public OAuth2LogoutHandler(OAuth2TokenService tokenService, OAuth2AuthorizedClientService authorizedClientService) {
        this.tokenService = tokenService;
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            // Получаем авторизованный клиент
            OAuth2AuthorizedClient authorizedClient =
                    authorizedClientService.loadAuthorizedClient(
                            oauthToken.getAuthorizedClientRegistrationId(),
                            oauthToken.getName());

            if (authorizedClient != null) {
                // Извлекаем токен
                String accessToken = authorizedClient.getAccessToken().getTokenValue();
                System.out.println("Токен: " + accessToken);
                // Отзываем токен
                if (accessToken != null) {
                    tokenService.revokeToken(accessToken);
                }
            }
        }
        SecurityContextHolder.clearContext();
    }
}
