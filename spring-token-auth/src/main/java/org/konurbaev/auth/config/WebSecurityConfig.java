package org.konurbaev.auth.config;

import org.konurbaev.auth.security.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                .antMatchers("/swagger-ui/**",
//                        "/v2/api-docs",
//                        "/js/**",
//                        "/css/**",
//                        "/index",
//                        "/api/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/access")
                .permitAll()
                .and()
                .logout()
                .permitAll();

        // Custom token based security filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        http.headers().cacheControl();

        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
                .ignoring()
                .antMatchers("/swagger-ui/**",
                        "/v2/api-docs",
                        "/js/**",
                        "/css/**",
                        "/index",
                        "/api/login");
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //.authenticationProvider(customAuthenticationProvider)
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}