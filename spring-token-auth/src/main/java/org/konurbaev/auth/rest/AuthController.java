package org.konurbaev.auth.rest;

import org.konurbaev.auth.security.AuthenticationRequest;
import org.konurbaev.auth.security.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<TokenResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {
        System.out.println("In rest controller");
        // Perform the security
        Authentication usernamePasswordAuthentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);

        String uuid = UUID.randomUUID().toString();
        System.out.println("uuid: " + uuid);

        // Return the token
        return ResponseEntity.ok(new TokenResponse(uuid));
    }
}
