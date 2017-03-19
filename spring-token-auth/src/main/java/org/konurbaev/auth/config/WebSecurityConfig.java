package org.konurbaev.auth.config;

import org.konurbaev.auth.security.TokenAuthHeaderFilter;
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

//    @Autowired
//    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public TokenAuthHeaderFilter tokenAuthHeaderFilter(){
        return new TokenAuthHeaderFilter();
    }

//    private CsrfTokenRepository csrfTokenRepository()
//    {
//        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//        repository.setSessionAttributeName("_csrf");
//        return repository;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home","/api/login", "/newlogin", "/js/login.js", "/css/style.css").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/access")
                .permitAll()
                .and()
                .logout()
                .permitAll();

//         http://stackoverflow.com/questions/28138864/expected-csrf-token-not-found-has-your-session-expired-403
//        http.csrf()
//                .csrfTokenRepository(csrfTokenRepository());

        // Custom JWT based security filter
        http
                .addFilterBefore(tokenAuthHeaderFilter(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        http.headers().cacheControl();

        http.csrf().disable();

    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/swagger-ui/**", "/v2/api-docs", "/js/**", "/css/**");
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //.authenticationProvider(customAuthenticationProvider)
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}