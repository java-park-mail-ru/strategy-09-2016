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
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by victor on 25.10.16.
 */

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class RegistrationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void createUser() throws Exception {
		mockMvc.perform(post("/user/")
				.content("{\"login\":\"user2\",\"email\": \"example2@mail.ru\",\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("responce", is("user_created")));
	}

	@Test
	public void createExistingUser() throws Exception {
		mockMvc.perform(post("/user/")
				.content("{\"login\":\"user2\",\"email\": \"example2@mail.ru\",\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("responce", is("user_created")));
		mockMvc.perform(post("/user/")
				.content("{\"login\":\"user2\",\"email\": \"example2@mail.ru\",\"password\":\"123\"}")
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
				.andExpect(status().isOk())
				.andExpect(jsonPath("errorCode", is("no_one_in_database")));
	}

	@Test
	public void ratingOfOne() throws Exception {
		mockMvc.perform(post("/user/")
				.content("{\"login\":\"user1\",\"email\": \"example1@mail.ru\",\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("responce", is("user_created")));
		mockMvc.perform(post("/user/")
				.content("{\"login\":\"user2\",\"email\": \"example2@mail.ru\",\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("responce", is("user_created")));
		mockMvc.perform(get("/rating/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("errorCode",isEmptyOrNullString()));
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
		mockMvc.perform(post("/user/")
				.content("{\"login\":\"user1\",\"email\": \"example1@mail.ru\",\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("responce", is("user_created")));
		mockMvc.perform(post("/session/")
				.content("{\"login\":\"user1\",\"email\": \"example1@mail.ru\",\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("responce", is("successfully_authorized")));
	}

	@Test
	public void sessionOfWithNullField() throws Exception {
		mockMvc.perform(post("/user/")
				.content("{\"login\":\"user1\",\"email\": \"example1@mail.ru\",\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("responce", is("user_created")));
		mockMvc.perform(post("/session/")
				.content("{\"login\":null,\"email\":null,\"password\":\"123\"}")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errorCode", is("null_field")));
	}

}
