/*
 * (c) Copyright 2017 sothawo.com
 */
package com.sothawo.taboo3.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * security configuration.
 *
 * @author P.J. Meisch (pj.meisch@sothawo.com)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Taboo3UserService userService;

    @Autowired
    public SecurityConfig(Taboo3UserService userService) {
        this.userService = userService;
    }

    /**
     * sets up two users in an inmemory repository.
     *
     * @param auth
     *         auth builder to use
     * @throws Exception
     *         on error
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * configure http basic auth with a custom login page.
     *
     * @param http
     *         the security to configure
     * @throws Exception
     *         on error
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .and()
                .rememberMe().key("taboo3")
                .and()
                .httpBasic().realmName("taboo3")
                .and()
                .logout().logoutSuccessUrl("/login?logout")
                .and()
                .csrf().ignoringAntMatchers("/bookmark/loadtitle", "/bookmark/upload", "/bookmark/dump")
                .and()
                .authorizeRequests()
                .regexMatchers("/(images|css|js|fonts)/.*").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/health").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/login")
        ;

    }
}
