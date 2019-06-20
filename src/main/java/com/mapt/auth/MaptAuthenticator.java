package com.mapt.auth;

import java.util.Optional;

import com.mapt.core.User;
import com.mapt.db.UserDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.hibernate.UnitOfWork;

public class MaptAuthenticator implements Authenticator<BasicCredentials, User>
{
	private UserDAO dao;
	
	public MaptAuthenticator(UserDAO dao)
	{
		this.dao = dao;
	}

	@Override
	@UnitOfWork
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException
	{
		String[] name = credentials.getUsername().split(" ");
		
		System.out.println(name[0] + " " + name[1]);
		
		User user = dao.findByName(name[0], name[1]);
		
		if(user == null)
		{
			return Optional.empty();
		}
		
		String pw = user.getPassword();
		
        if (pw.equals(credentials.getPassword()))
        {
            return Optional.of(dao.findById(1L));
        }
        
        return Optional.empty();
    }
}