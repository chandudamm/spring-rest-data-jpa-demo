package com.spring.demos.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.spring.demos.SpringRestDataJpaDemoApplication;
import com.spring.demos.model.Account;
import com.spring.demos.model.Bookmark;
import com.spring.demos.respository.AccountRepository;
import com.spring.demos.respository.BookmarkRepository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringRestDataJpaDemoApplication.class)
@WebAppConfiguration
public class BookmarkControllerTest {
	private static final String userName = "laxmi";
	private List<Bookmark> bookmarkList = new ArrayList<>();
	private Account account;

	private MockMvc mockMvc;
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), 
					MediaType.APPLICATION_JSON.getSubtype(),
					Charset.forName("utf8"));
	
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BookmarkRepository bookmarkRepository;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
	
	@Before
	public void setup(){	
		//get MockMVC object from WebAppContextSetup
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		//clean all existing data from repository
		bookmarkRepository.deleteAllInBatch();
		accountRepository.deleteAllInBatch();
		//setup test data
		Account account = accountRepository.save(new Account(userName, "password"));
		bookmarkList.add(bookmarkRepository.save(new Bookmark("http://google.com/1/"+userName, "Google Site", account)));
		bookmarkList.add(bookmarkRepository.save(new Bookmark("http://gmail.com/2/"+userName, "Gmail site", account)));
	}
	
	@Test
	public void userNotFound() throws Exception{
		mockMvc.perform(post("/chandu/bookmarks")
				.content(json(new Bookmark()))
				.contentType(contentType))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void readBookmarks() throws Exception{
		mockMvc.perform(get("/"+userName+"/bookmarks"))
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].id", is(bookmarkList.get(0).getId().intValue())))
		.andExpect(jsonPath("$[0].url", is(bookmarkList.get(0).getUrl())))
		.andExpect(jsonPath("$[0].description", is(bookmarkList.get(0).getDescription())))
		.andExpect(jsonPath("$[1].id", is(bookmarkList.get(1).getId().intValue())))
		.andExpect(jsonPath("$[1].url", is(bookmarkList.get(1).getUrl())))
		.andExpect(jsonPath("$[1].description", is(bookmarkList.get(1).getDescription())));
	}
	
	@Test
	public void readSingleBookmark() throws Exception{
		mockMvc.perform(get("/" + userName + "/bookmarks/" + bookmarkList.get(0).getId()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(contentType))
			.andExpect(jsonPath("$.id", is(bookmarkList.get(0).getId().intValue())))
			.andExpect(jsonPath("$.url", is(bookmarkList.get(0).getUrl())))
			.andExpect(jsonPath("$.description", is(bookmarkList.get(0).getDescription())));
	}
	
	@Test
	public void createBookmark() throws Exception{
		String newBookmark = this.json(new Bookmark("http://spring.io/3/"+userName, "Spring IO", account));
		mockMvc.perform(post("/"+userName+"/bookmarks")
				.content(newBookmark)
				.contentType(contentType))
				.andExpect(status().isCreated());
		
	}
	
	
	protected String json(Object obj) throws HttpMessageNotWritableException, IOException{
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(obj, contentType, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
	
}
