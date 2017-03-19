package org.konurbaev.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.UUID;

//@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        if (authentication.isAuthenticated()){
//            return authentication;
//        }
        // TODO: check if the authentication is instance of TokenAuthentication
        if (authentication instanceof TokenAuthentication){
            TokenAuthentication tokenAuthentication = (TokenAuthentication)authentication;
            System.out.println("token: " + tokenAuthentication.getToken());
            return authentication;
        }

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        if (StringUtils.isEmpty(username)){
            throw new AuthenticationServiceException("Username should not be empty");
        }
        if (StringUtils.isEmpty(password)){
            throw new AuthenticationServiceException("Password should not be empty");
        }

        String uuid = UUID.randomUUID().toString();
        System.out.println("uuid: " + uuid);
        return new TokenAuthentication(authentication.getPrincipal(), authentication.getCredentials(), new ArrayList<>(), uuid);
//        return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
//        UsernamePasswordAuthenticationResponse usernamePasswordAuthenticationResponse = unidataAuthenticationService.authenticateInUnidata(username, password);
//        return new TokenAuthentication(authentication.getPrincipal(), authentication.getCredentials(), new ArrayList<>(), usernamePasswordAuthenticationResponse.getToken());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class) || authentication.equals(TokenAuthentication.class);
    }

}
