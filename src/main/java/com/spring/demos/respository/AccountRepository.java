package com.spring.demos.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.demos.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByUserName(String userName);
}
