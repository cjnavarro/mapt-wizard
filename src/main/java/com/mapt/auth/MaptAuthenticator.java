package com.mapt.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mapt.core.User;
import com.mapt.core.UserRole;
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
	        	if(user.getRole() == UserRole.VIP)
	        	{
	        		publishToMe(user.getUsername());
	        	}
	        	
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
	
	private void publishToMe(String username)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String message = simpleDateFormat.format(new Date()) + username;
		
		PublishRequest request = new PublishRequest();
		request.setMessage(message);
		request.setTopicArn(topicArn);
		
		awsSNS.publish(request);
	}
}