package org.konurbaev.auth.external;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExternalTokenService {

    private static final Logger logger = LogManager.getLogger(ExternalTokenService.class);

    private final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    public String generateToken(String username){
        String token = UUID.randomUUID().toString();
        logger.info("Generated token " + token + " for user " + username);
        cache.put(token, username);
        return token;
    }

    public boolean doesExistToken(String token){
        return cache.containsKey(token);
    }

    public String getUsernameForToken(String token){
        return cache.get(token);
    }
}
