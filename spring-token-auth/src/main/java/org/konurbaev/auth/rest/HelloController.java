package org.konurbaev.auth.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class HelloController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(){
        return "Hello, " + SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
