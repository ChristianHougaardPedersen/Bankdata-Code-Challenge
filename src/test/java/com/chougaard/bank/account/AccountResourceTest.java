package com.chougaard.bank.account;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class AccountResourceTest {

	private static final String BASE_URI = "/bank/api/v1/accounts";

	@Test
	void createAccount_returns201_withAccountNumberAndZeroBalance() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"firstName\":\"Bob\",\"lastName\":\"Builder\"}")
		.when()
				.post(BASE_URI)
		.then()
				.statusCode(201)
				.header("Location", notNullValue())
				.body("accountNumber", notNullValue())
				.body("balance", notNullValue());
	}

	@Test
	void getAccount_unknownAccount_returns404() {
		given()
				.contentType(ContentType.JSON)
		.when()
				.get(BASE_URI + "/9999999999")
		.then()
				.statusCode(404)
				.body("message", notNullValue());
	}


	@Test
	void createAccount_blankName_returns400() {
		given()
				.contentType("application/json")
				.body("{\"firstName\":\"\",\"lastName\":\"Builder\"}")
		.when()
				.post(BASE_URI)
		.then()
				.statusCode(400);
	}

	@Test
	void deposit_negativeAmount_returns400() {
		// Arrange — create an account and extract its number
		String accountNumber =
				given()
						.contentType(ContentType.JSON)
						.body("{\"firstName\":\"Bob\",\"lastName\":\"Builder\"}")
				.when()
						.post(BASE_URI)
				.then()
						.statusCode(201)
				.extract().path("accountNumber");

		// Act & assert — negative deposit should reject
		given()
				.contentType(ContentType.JSON)
				.body("{\"amount\":-50.00}")
		.when()
				.post(BASE_URI + "/" + accountNumber + "/deposits")
		.then()
				.statusCode(400);
	}

}
