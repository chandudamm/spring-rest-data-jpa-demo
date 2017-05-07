package com.spring.demos.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String userName;
	@JsonIgnore
	private String password;
	@OneToMany(mappedBy = "account")
	private Set<Bookmark> boomarks = new HashSet<>();
	
	public Account() {
		// TODO Auto-generated constructor stub
	}

	public Account(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setBoomarks(Set<Bookmark> boomarks) {
		this.boomarks = boomarks;
	}

	public Set<Bookmark> getBoomarks() {
		return boomarks;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", userName=" + userName + ", password="
				+ password + ", boomarks=" + boomarks + "]";
	}

}
