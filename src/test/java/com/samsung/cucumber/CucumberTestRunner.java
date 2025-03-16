package com.samsung.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

//이 부분이 잘못되었을 수 있습니다
//올바른 임포트는 다음과 같습니다
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("member-service.feature")  // 정확한 파일 경로로 수정
//@SelectClasspathResource("src/test/resources/test.feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.samsung.stepdefs")  // 실제 패키지 이름으로 수정
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/cucumber.html")
public class CucumberTestRunner {
    // This class serves as an entry point for Cucumber tests
}

//@RunWith(io.cucumber.junit.Cucumber.class) // 전체 패키지 경로 포함
//@CucumberOptions(features = "src/test/resources/test.feature", glue = "com.samsung.stepdefs")
//public class CucumberTestRunner {
//}