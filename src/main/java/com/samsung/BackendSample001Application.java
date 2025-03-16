package com.samsung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class BackendSample001Application extends SpringBootServletInitializer {
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        log.info("Configuring Spring Boot application");
        return application.sources(BackendSample001Application.class)
            .web(WebApplicationType.SERVLET);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	log.info("Starting up web application");
        super.onStartup(servletContext);
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendSample001Application.class, args);
    }

}
