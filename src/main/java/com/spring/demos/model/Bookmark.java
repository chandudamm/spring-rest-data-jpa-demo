package com.spring.demos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Bookmark {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String url;
	private String description;
	@JsonIgnore
	@ManyToOne
	private Account account;
	
	public Bookmark() {
		// TODO Auto-generated constructor stub
	}
	
	public Bookmark(String url, String description, Account account) {
		this.url = url;
		this.description = description;
		this.account = account;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

	@Override
	public String toString() {
		return "Bookmark [id=" + id + ", url=" + url + ", description="
				+ description + ", account=" + account + "]";
	}

}
