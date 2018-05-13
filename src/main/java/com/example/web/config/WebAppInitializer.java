package com.example.web.config;

import com.example.security.config.SecurityConfig;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Order(2)
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        // Place MvcConfig and SecurityConfig to same ApplicationContext
        // see https://docs.spring.io/spring-security/site/docs/5.0.5.RELEASE/reference/htmlsingle/#mvc-requestmatcher
        return new Class[] {MvcConfig.class, SecurityConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
