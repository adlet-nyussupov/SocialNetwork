package com.adlet.SocialNetwork;

import com.adlet.SocialNetwork.controller.SocialNetworkPostController;
import com.adlet.SocialNetwork.model.response.SocialNetworkPostResponseModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adlet.SocialNetwork.exception.SocialNetworkPostServiceException;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SocialNetworkApplicationTests {

	@Autowired
	private SocialNetworkPostController controller;

	@LocalServerPort
	private int port;

	@Autowired
	private MockMvc mockMvc;


	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void createPostsAndIncreaseViewCounterAndRequestHighestTest() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		RandomString content = new RandomString(500, ThreadLocalRandom.current());
		RandomString author = new RandomString(100, ThreadLocalRandom.current());
		HashMap<String, String> body = new HashMap<>();
		List<SocialNetworkPostResponseModel> posts = new ArrayList<>();

		// Create posts and add results into list
		for(int i = 0; i < 50; ++i){
			String randContent = content.nextString();
			String randAuthor = author.nextString();
			body.put("author", randAuthor);
			body.put("content", randContent);

			MvcResult mvcResult = this.mockMvc.perform(post("http://localhost:" + port +"/posts").contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(body))).andDo(print()).andExpect(status().isOk()).andReturn();

			SocialNetworkPostResponseModel response = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), SocialNetworkPostResponseModel.class);
			Assert.assertEquals(response.getAuthor(), randAuthor);
			Assert.assertEquals(response.getContent(), randContent);
			posts.add(response);
		}

		// Increment posts view by random number and update posts list
		for(int i = 0; i < posts.size(); ++i){
			int randomNum = ThreadLocalRandom.current().nextInt(0, 50);
			posts.get(i).setViewCount(randomNum);
			for(int j = randomNum; j > 0; --j){
				MockHttpServletRequestBuilder increaseCounterRequest = MockMvcRequestBuilders.patch("http://localhost:" + port +"/posts/view-count/"+posts.get(i).getPostId())
						.contentType(MediaType.APPLICATION_JSON);
				this.mockMvc.perform(increaseCounterRequest).andExpect(status().isOk());
			}
		}

		// Get the highest view posts and compare sorted posts list with response
		MvcResult mvcResult = this.mockMvc.perform(get("http://localhost:" + port +"/posts/highest-view-count").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsByteArray());
		List<SocialNetworkPostResponseModel> responses = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<List<SocialNetworkPostResponseModel>>(){});
		Collections.sort(posts, Comparator.comparing(p -> -p.getViewCount()));

		for(int i = 0; i < responses.size(); ++i){
			if(posts.get(i).getViewCount() != responses.get(i).getViewCount()) Assert.fail("Wrong view count order");
		}

	}


	@Test
	void createAndGetPost() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		RandomString content = new RandomString(500, ThreadLocalRandom.current());
		RandomString author = new RandomString(100, ThreadLocalRandom.current());
		String randContent = content.nextString();
		String randAuthor = author.nextString();
		HashMap<String, String> body = new HashMap<>();
		body.put("author", randAuthor);
		body.put("content", randContent);

		// Create post
		MvcResult mvcResult = this.mockMvc.perform(post("http://localhost:" + port +"/posts").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(body))).andDo(print()).andExpect(status().isOk()).andReturn();
		SocialNetworkPostResponseModel response = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), SocialNetworkPostResponseModel.class);

		// Get post
		mvcResult = this.mockMvc.perform(get("http://localhost:" + port +"/posts/"+response.getPostId()).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();


	}

	@Test
	void deletePost() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		RandomString content = new RandomString(500, ThreadLocalRandom.current());
		RandomString author = new RandomString(100, ThreadLocalRandom.current());
		String randContent = content.nextString();
		String randAuthor = author.nextString();
		HashMap<String, String> body = new HashMap<>();
		body.put("author", randAuthor);
		body.put("content", randContent);

		// Create post
		MvcResult mvcResult = this.mockMvc.perform(post("http://localhost:" + port +"/posts").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(body))).andDo(print()).andExpect(status().isOk()).andReturn();
		SocialNetworkPostResponseModel response = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), SocialNetworkPostResponseModel.class);

		// Delete post
		mvcResult = this.mockMvc.perform(delete("http://localhost:" + port +"/posts/"+response.getPostId()).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

	}


	@Test
	void getInvalidPost() throws Exception{
		this.mockMvc.perform(get("http://localhost:" + port +"/posts/dnfsdnfoiewnvpgomds165156").contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof SocialNetworkPostServiceException))
				.andExpect(result -> assertEquals("Post was not found", result.getResolvedException().getMessage()));
	}

	@Test
	void deleteInvalidPost() throws Exception{
		this.mockMvc.perform(delete("http://localhost:" + port +"/posts/dnfsdnfoiewnvpgomds165156").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent());
	}

	@Test
	void createEmptyPost() throws Exception{

		ObjectMapper objectMapper = new ObjectMapper();
		this.mockMvc.perform(post("http://localhost:" + port +"/posts").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(""))).andDo(print()).andExpect(status().isBadRequest());;

	}

	@Test
	void createInvalidPost() throws Exception{

		HashMap<String, String> body = new HashMap<>();
		body.put("content", "test");

		ObjectMapper objectMapper = new ObjectMapper();
		this.mockMvc.perform(post("http://localhost:" + port +"/posts").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(body)))
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof SocialNetworkPostServiceException))
				.andExpect(result -> assertEquals("Author or content are not specified", result.getResolvedException().getMessage()));
	}

}
