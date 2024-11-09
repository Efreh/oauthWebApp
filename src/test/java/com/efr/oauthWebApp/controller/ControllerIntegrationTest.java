package com.efr.oauthWebApp.controller;

import com.efr.oauthWebApp.config.security.SecurityConfig;
import com.efr.oauthWebApp.service.OAuth2TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OAuth2TokenService tokenService;

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testAdminPageAccess() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testLogoutSuccess() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testRevokeTokenAspect() throws Exception {
        // Пример вызова метода, чтобы проверить, срабатывает ли аспект
        String testToken = "sample-token";
        tokenService.revokeToken(testToken);

        // Проверка логирования можно добавить через проверки логов (с помощью LogCaptor, например)
    }
}