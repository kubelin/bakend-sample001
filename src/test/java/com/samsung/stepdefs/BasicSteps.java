package com.samsung.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BasicSteps {
	@Given("a test step")
	public void aTestStep() {
		System.out.println("Given step executed");
	}

	@When("I run the test")
	public void iRunTheTest() {
		System.out.println("When step executed");
	}

	@Then("it should pass")
	public void itShouldPass() {
		System.out.println("Then step executed");
	}
}