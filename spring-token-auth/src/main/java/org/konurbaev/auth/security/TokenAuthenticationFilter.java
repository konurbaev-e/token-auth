package org.konurbaev.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.konurbaev.auth.config.Constants.publicResources;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.info("URI: " + request.getRequestURI());
        String uri = request.getRequestURI();
        if (publicResources.stream().anyMatch(uri::startsWith)){
            logger.info("Skipping this filter for uri " + uri);
        } else {
            String token = request.getHeader("token");
            if (StringUtils.isEmpty(token)) {
                logger.info("no or empty header token " + token + " for uri " + uri);
                if (request.getCookies() != null) {
                    Optional<Cookie> optionalCookie = Arrays.stream(request.getCookies()).filter(cookie -> "token".equals(cookie.getName())).findFirst();
                    if (optionalCookie.isPresent()){
                        token = optionalCookie.get().getValue();
                        logger.info("cookie token " + token + " for uri " + uri);
                    }
                    else {
                        logger.info("no or empty cookie token " + token + " for uri " + uri);
                    }
                } else {
                    logger.info("no cookies for uri " + uri);
                }
            } else {
                logger.info("header token " + token + " for uri " + uri);
            }
            if (StringUtils.isEmpty(token)){
                response.setStatus(302);
                response.sendRedirect("/relogin");
                return;
            }

            // validation of token in external service
            if (!tokenService.doesExistToken(token)){
                logger.info("token " + token + " does not exist in token service");
                response.setStatus(302);
                response.sendRedirect("/relogin");
                return;
            }

            setTokenAuthentication(request, token);
        }
        chain.doFilter(request, response);
    }

    private void setTokenAuthentication(HttpServletRequest request, String token){
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(tokenService.getUsernameForToken(token), null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            logger.info("authenticated user for request session " + request.getRequestedSessionId() + ", setting security context");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

}

