package com.efr.oauthWebApp.service;

import static org.mockito.Mockito.*;

import com.efr.oauthWebApp.config.security.OAuth2LogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;


@SpringBootTest
public class OAuth2LogoutHandlerTest {

    @InjectMocks
    private OAuth2LogoutHandler logoutHandler;

    @Mock
    private OAuth2AuthorizedClientService authorizedClientService;

    @Mock
    private OAuth2TokenService tokenService;

    @Mock
    private OAuth2AuthenticationToken authenticationToken;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private OAuth2AuthorizedClient authorizedClient;

    @Mock
    private OAuth2AccessToken accessToken;

    @BeforeEach
    public void setUp() {
        // Настройка мока авторизованного клиента
        when(authenticationToken.getAuthorizedClientRegistrationId()).thenReturn("google");
        when(authenticationToken.getName()).thenReturn("testUser");
        when(authorizedClientService.loadAuthorizedClient("google", "testUser")).thenReturn(authorizedClient);
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);
        when(accessToken.getTokenValue()).thenReturn("sampleToken");
    }

    @Test
    public void testLogout() {
        logoutHandler.logout(request, response, authenticationToken);

        // Проверка, что токен был отозван
        verify(tokenService, times(1)).revokeToken("sampleToken");
    }
}