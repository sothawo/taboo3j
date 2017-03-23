/*
 * (c) Copyright 2017 HLX Touristik GmbH
 */
package com.sothawo.taboo3.mvc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDateTime;

/**
 * session scoped object to store the currently selections and bookmarks.
 *
 * @author P.J. Meisch (Peter.Meisch@hlx.com)
 */
@Component
@SessionScope
public class SessionStore {
    private static final Logger logger = LoggerFactory.getLogger(SessionStore.class);
    /** time when the store was created. */
    private final LocalDateTime creationTime = LocalDateTime.now();

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        return "SessionStore{" +
                "creationTime=" + creationTime +
                '}';
    }
}
