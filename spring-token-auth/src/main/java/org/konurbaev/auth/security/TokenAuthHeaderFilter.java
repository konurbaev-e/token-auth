package org.konurbaev.auth.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthHeaderFilter /*extends GenericFilterBean*/ extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader("token");

        // here validation of token should be
        logger.info("checking authentication for token " + authToken);

        chain.doFilter(request, response);
    }

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        final String token = httpServletRequest.getHeader("");
//
//        if (StringUtils.isEmpty(token)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            TokenAuthentication tokenAuthentication = new AuthenticationToken(token);
//            SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
//        } catch (AuthenticationException failed) {
//            SecurityContextHolder.clearContext();
//            return;
//    }
//
//    chain.doFilter(request, response); // continue down the chain
//
//    }
}

