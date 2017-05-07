package com.spring.demos.controller;

import java.net.URI;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.demos.exception.UserNotFoundException;
import com.spring.demos.model.Bookmark;
import com.spring.demos.respository.AccountRepository;
import com.spring.demos.respository.BookmarkRepository;







@RestController
@RequestMapping("/{userId}/bookmarks")
public class BookmarksRestController {
	
	private final AccountRepository accountRepository;
	private final BookmarkRepository bookmarkRepository;
	
	@Autowired
	public BookmarksRestController(AccountRepository accountRepository,
			BookmarkRepository bookmarkRepository) {
		super();
		this.accountRepository = accountRepository;
		this.bookmarkRepository = bookmarkRepository;
	}


	@RequestMapping(method=RequestMethod.GET)
	public Collection<Bookmark> readBookmarks(@PathVariable String userId){
		this.validateUser(userId);
		return this.bookmarkRepository.findByAccountUserName(userId);
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/{bookmarkId}")
	public Bookmark readBookMark(@PathVariable String userId, @PathVariable Long bookmarkId){
		validateUser(userId);
		return bookmarkRepository.findOne(bookmarkId);
	}
	
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> add(@PathVariable String userId, @RequestBody Bookmark input){
		this.validateUser(userId);
		
		return this.accountRepository.findByUserName(userId).map(account -> {
			Bookmark result = bookmarkRepository.save(new Bookmark(input.getUrl(), input.getDescription(), account));
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
			return ResponseEntity.created(location).build();
			})
			.orElse(ResponseEntity.noContent().build());

	}


	private void validateUser(String userId) {
		accountRepository.findByUserName(userId).orElseThrow(()->new UserNotFoundException(userId));
	}
		
	
}
