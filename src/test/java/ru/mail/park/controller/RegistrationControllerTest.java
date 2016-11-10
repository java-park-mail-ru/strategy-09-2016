package ru.mail.park.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class RegistrationControllerTest {

	@Autowired
	private MockMvc mockMvc;

    private void createUserTest(String login, String email) throws Exception {
        mockMvc.perform(post("/user/")
                .content("{\"login\":\""+login+"\",\"email\": \""+email+"\",\"password\":\"123\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("response", is("user_created")));
    }

	@Test
	public void createUser() throws Exception {
        createUserTest("user","email@mail.ru");
	}

	@Test
	public void createExistingUser() throws Exception {
        createUserTest("user","email@mail.ru");
        mockMvc.perform(post("/user/")
				.content("{\"login\":\"user\",\"email\": \"email@mail.ru\",\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errorCode", is("user_already_exist")));
	}

	@Test
	public void createUserWithNullField() throws Exception {
		mockMvc.perform(post("/user/")
				.content("{\"login\":null,\"email\":null,\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errorCode", is("null_field")));
	}

	@Test
	public void ratingOfNoone() throws Exception {
		mockMvc.perform(get("/rating/"))
				.andExpect(status().isOk());
	}

	@Test
	public void ratingOfOne() throws Exception {
        createUserTest("user","email@mail.ru");
        createUserTest("user1","email1@mail.ru");
		mockMvc.perform(get("/rating/"))
				.andExpect(status().isOk());
	}

	@Test
	public void sessionOfNonexistUser() throws Exception {
		mockMvc.perform(post("/session/")
				.content("{\"login\":\"user1\",\"email\": \"example1@mail.ru\",\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errorCode", is("wrong_login_password")));
	}

	@Test
	public void sessionOfExistUser() throws Exception {
        createUserTest("user1","email1@mail.ru");
		mockMvc.perform(post("/session/")
				.content("{\"login\":\"user1\",\"email\": \"email1@mail.ru\",\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("response", is("successfully_authorized")));
	}

	@Test
	public void sessionOfWithNullField() throws Exception {
        createUserTest("user","email1@mail.ru");
		mockMvc.perform(post("/session/")
				.content("{\"login\":null,\"email\":null,\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errorCode", is("null_field")));
	}

}
