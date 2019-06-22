package com.mapt.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

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
		
		try
		{
			String password = toSHA1(credentials.getPassword().getBytes());
			
	        if (user.getPassword().equals(password))
	        {
	            return Optional.of(user);
	        }
		}
		catch(Exception e)
		{
			//TODO logging
			e.printStackTrace();
		}
        
        return Optional.empty();
    }
	
	private static String toSHA1(byte[] convertme)
	{
	    MessageDigest md = null;
	    try
	    {
	        md = MessageDigest.getInstance("MD5");
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    } 
	    
	    return (new HexBinaryAdapter()).marshal(md.digest(convertme));
	}
}