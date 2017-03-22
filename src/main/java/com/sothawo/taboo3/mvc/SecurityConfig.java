/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * security configuration.
 *
 * @author P.J. Meisch (Peter.Meisch@hlx.com)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
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
        auth.inMemoryAuthentication()
                .withUser("peter").password("retep").roles("USER").and()
                .withUser("admin").password("nimda").roles("USER", "ADMIN");
    }
}
