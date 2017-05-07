package com.spring.demos;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.spring.demos.model.Account;
import com.spring.demos.model.Bookmark;
import com.spring.demos.respository.AccountRepository;
import com.spring.demos.respository.BookmarkRepository;

@SpringBootApplication
//@EnableJpaRepositories(basePackageClasses={AccountRepository.class, BookmarkRepository.class})
public class SpringRestDataJpaDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestDataJpaDemoApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(AccountRepository accountRepository,
			BookmarkRepository bookmarkRepository) {
		return (evt) -> Arrays.asList(
				"jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
				.forEach(
						a -> {
							Account account = accountRepository.save(new Account(a, "password"));
							System.out.println("Account Obj**********"+account);
							bookmarkRepository.save(new Bookmark("http://bookmark.com/1/" + a, "A description", account));
							System.out.println("Bookmark1 Obj saved**********");
							bookmarkRepository.save(new Bookmark("http://bookmark.com/2/" + a, "A description", account));
							System.out.println("Bookmark2 Obj saved**********");
						});
	}
	
}
