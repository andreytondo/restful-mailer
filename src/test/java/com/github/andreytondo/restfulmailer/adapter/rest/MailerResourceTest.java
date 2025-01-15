package com.github.andreytondo.restfulmailer.adapter.rest;

import com.github.andreytondo.restfulmailer.MailScenario;
import com.github.andreytondo.restfulmailer.MailServer;
import com.github.andreytondo.restfulmailer.adapter.rest.dto.MailRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static com.github.andreytondo.restfulmailer.MailScenario.*;

@QuarkusTest
class MailerResourceTest {

    @BeforeAll
    static void setup() {
        MailServer.start();
    }

    @BeforeEach
    void clearInboxes() {
        MailServer.clearInboxes();
    }

    @Test
    void testSendMail() {
        given()
                .contentType(ContentType.JSON)
                .body(createDefaultRequest())
                .when()
                .post("/mail")
                .then()
                .statusCode(200);
        Assertions.assertTrue(MailServer.awaitIncomingEmail(1));
    }

    @Test
    void testSendMailWrongCredentials() {
        MailRequest mailRequest = createMailRequest(
                createConnection(MailServer.HOST, MailServer.PORT, "wrong", "wrong"),
                List.of(createDefaultMail("Test", "Test")));

        given()
                .contentType(ContentType.JSON)
                .body(mailRequest)
                .when()
                .post("/mail")
                .then()
                .statusCode(200); // The response is 200 because the mail is sent to the server, but it is not delivered
        Assertions.assertTrue(MailServer.awaitIncomingEmail(0));
    }

    @AfterAll
    static void tearDown() {
        MailServer.stop();
    }
}