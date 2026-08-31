package com.chougaard.bank.transfer;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class TransferResourceTest {

	private static final String BASE_URI = "/bank/api/v1/transfers";
	private static final String ACCOUNT_ANDERS = "1000000001";
	private static final String ACCOUNT_JOAKIM = "1000000003";

	@Test
	void transfer_betweenTwoAccounts_returns201WithRecord() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"fromAccountNumber\":\""
						+ ACCOUNT_JOAKIM
						+ "\",\"toAccountNumber\":\""
						+ ACCOUNT_ANDERS
						+ "\",\"amount\":100.00}")
		.when()
				.post(BASE_URI)
		.then()
				.statusCode(201)
				.header("Location", notNullValue())
				.body("fromAccountNumber", equalTo(ACCOUNT_JOAKIM))
				.body("toAccountNumber", equalTo(ACCOUNT_ANDERS))
				.body("amount", comparesEqualTo(100.00f));
	}

	@Test
	void transfer_insufficientFunds_returns409() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"fromAccountNumber\":\""
						+ ACCOUNT_ANDERS
						+ "\",\"toAccountNumber\":\""
						+ ACCOUNT_JOAKIM
						+ "\",\"amount\":999999999.00}")
		.when()
				.post(BASE_URI)
		.then()
				.statusCode(409);
	}

	@Test
	void transfer_sameAccount_returns400() {
		given()
				.contentType(ContentType.JSON)
				.body("{\"fromAccountNumber\":\""
						+ ACCOUNT_ANDERS
						+ "\",\"toAccountNumber\":\""
						+ ACCOUNT_ANDERS
						+ "\",\"amount\":100.00}")
		.when()
				.post(BASE_URI)
		.then()
				.statusCode(400);
	}

	@Test
	void getTransfer_existingId_returns200() {
		Integer transferId =
			given()
					.contentType(ContentType.JSON)
					.body("{\"fromAccountNumber\":\""
							+ ACCOUNT_JOAKIM
							+ "\",\"toAccountNumber\":\""
							+ ACCOUNT_ANDERS
							+ "\",\"amount\":100.00}")
			.when()
					.post(BASE_URI)
			.then()
					.statusCode(201)
					.extract().path("transferId");

		given()
				.contentType(ContentType.JSON)
		.when()
				.get(BASE_URI + "/" + transferId)
		.then()
				.statusCode(200)
				.body("fromAccountNumber", equalTo(ACCOUNT_JOAKIM))
				.body("toAccountNumber", equalTo(ACCOUNT_ANDERS))
				.body("amount", comparesEqualTo(100.00f));
	}

	@Test
	void getTransfer_unknownId_returns404() {
		given()
				.contentType(ContentType.JSON)
		.when()
				.get(BASE_URI + "/" + 99999999)
		.then()
				.statusCode(404);
	}
}
