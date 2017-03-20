package org.konurbaev.auth.security;

import org.konurbaev.auth.external.ExternalTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    @Autowired
    private ExternalTokenService externalTokenService;

    public TokenResponse generateToken(){
        return new TokenResponse(externalTokenService.generateToken());
    }
}
