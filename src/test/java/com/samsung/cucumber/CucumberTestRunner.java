package com.samsung.cucumber;

import org.junit.runner.RunWith;

@Suite
@IncludeEngines("cucumber")
//@SelectClasspathResource("test.feature")  // 정확한 파일 경로로 수정
@SelectClasspathResource("src/test/resources/test.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.samsung.stepdefs")  // 실제 패키지 이름으로 수정
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/cucumber.html")
public class CucumberTestRunner {
    // This class serves as an entry point for Cucumber tests
}

//@RunWith(io.cucumber.junit.Cucumber.class) // 전체 패키지 경로 포함
//@CucumberOptions(features = "src/test/resources/test.feature", glue = "com.samsung.stepdefs")
//public class CucumberTestRunner {
//}