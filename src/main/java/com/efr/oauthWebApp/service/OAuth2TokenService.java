package com.efr.oauthWebApp.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuth2TokenService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void revokeToken(String token) {
        String TOKEN_REVOCATION_URL = "https://oauth2.googleapis.com/revoke";
        String revokeUrl = TOKEN_REVOCATION_URL + "?token=" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(revokeUrl, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Token revoked successfully");
            } else {
                System.out.println("Failed to revoke token. Response: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Error during token revocation: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
    }

}
