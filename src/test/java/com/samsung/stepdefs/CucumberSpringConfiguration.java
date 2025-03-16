package com.samsung.stepdefs;

import org.springframework.boot.test.context.SpringBootTest;

import com.samsung.BackendSample001Application;

import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = BackendSample001Application.class)
public class CucumberSpringConfiguration {
    // 이 클래스는 비어있어도 됩니다.
    // Cucumber와 Spring 통합을 위한 구성 클래스입니다.
}