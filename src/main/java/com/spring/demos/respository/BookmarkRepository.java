package com.spring.demos.respository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.demos.model.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	Collection<Bookmark> findByAccountUserName(String userName);
}
