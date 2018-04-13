package com.example.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(1)
public class WebSecurityInitializer extends AbstractSecurityWebApplicationInitializer {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityInitializer.class);

    public WebSecurityInitializer() {
        logger.info("Instantiated.");
    }
}