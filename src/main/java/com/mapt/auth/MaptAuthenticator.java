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
		User user = dao.findByName(credentials.getUsername().toLowerCase());
		
		if(user == null)
		{
			return Optional.empty();
		}
		
		String pw = user.getPassword();
		
        if (pw.equals(credentials.getPassword()))
        {
            return Optional.of(user);
        }
        
        return Optional.empty();
    }
}