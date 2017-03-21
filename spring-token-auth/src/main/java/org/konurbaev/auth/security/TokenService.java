package org.konurbaev.auth.security;

import org.konurbaev.auth.external.ExternalTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    @Autowired
    private ExternalTokenService externalTokenService;

    public TokenResponse generateToken(String username){
        return new TokenResponse(externalTokenService.generateToken(username));
    }

    public boolean doesExistToken(String token){
        return externalTokenService.doesExistToken(token);
    }

    public String getUsernameForToken(String token) {
        return externalTokenService.getUsernameForToken(token);
    }
}
