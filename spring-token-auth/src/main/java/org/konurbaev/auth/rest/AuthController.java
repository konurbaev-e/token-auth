package org.konurbaev.auth.rest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.konurbaev.auth.security.AuthenticationRequest;
import org.konurbaev.auth.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("In /api/login rest controller");
        // Perform the security
        Authentication usernamePasswordAuthentication;
        try {
            usernamePasswordAuthentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e){
            // catch AuthenticationException if incorrect username or password
            logger.error("Authentication error", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);

        // Return the token
        return ResponseEntity.ok(tokenService.generateToken(authenticationRequest.getUsername()));
    }
}
