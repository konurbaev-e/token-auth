package org.konurbaev.auth.security;

import org.konurbaev.auth.external.ExternalTokenService;
import org.springframework.beans.factory.annotation.Autowired;
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

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ExternalTokenService externalTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.info("URI: " + request.getRequestURI());
        String uri = request.getRequestURI();
        if (uri.equals("/api/login") || uri.equals("/index")){
            chain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("token");
        logger.info("checking token " + token);

        if (StringUtils.isEmpty(token)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "no token in request");
            return;
        }

        // validation of token in external service
        if (!externalTokenService.doesExistToken(token)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token does not exist");
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("User with token", null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            logger.info("authenticated user with token " + token + ", setting security context");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}

