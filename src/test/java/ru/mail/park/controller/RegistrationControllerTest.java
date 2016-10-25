package ru.mail.park.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.mail.park.services.AccountService;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by victor on 25.10.16.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class RegistrationControllerTest {
    private AccountService accountService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testMe() {
        HttpEntity requestEntity = new HttpEntity("{\"login\":\"user2\",\"email\": \"example2@mail.ru\"," +
                "\"password\":\"123\"}");
        String test = null;
        ResponseEntity<RegistrationResponse> meResp = restTemplate.postForEntity("/user",requestEntity,RegistrationResponse.class,test);
        assertEquals(HttpStatus.OK, meResp.getStatusCode());
        RegistrationResponse response = meResp.getBody();
        assertEquals("user_created", response.getResponse());
    }

    private static final class RegistrationResponse {
        private String response;

        @JsonCreator
        private RegistrationResponse(@JsonProperty("response") String response) {
            this.response = response;
        }

        public String getResponse() {
            return response;
        }
    }

}
