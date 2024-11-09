package com.efr.oauthWebApp.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        Collection<GrantedAuthority> authorities = new ArrayList<>(oAuth2User.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        String email = oAuth2User.getAttribute("email");
        if (email != null && email.endsWith("raam@gmail.com")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "name");
    }
}
