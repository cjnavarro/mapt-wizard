package com.mapt.core;

import java.security.Principal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
@NamedQueries({
@NamedQuery(name = "com.mapt.core.User_findAll", query = "from User"),
@NamedQuery(name = "com.mapt.core.User_findByName", query = "from User where firstname = :firstname and lastname = :lastname"),
})
public class User implements Principal
{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String firstname;
	
	private String lastname;
	
	@JsonIgnore
	private String password;

	public User() {
	}

	public User(String firstname, String lastname)
	{
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getLastname()
	{
		return lastname;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	@Override
	public String getName()
	{
		return firstname + " " + lastname;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}