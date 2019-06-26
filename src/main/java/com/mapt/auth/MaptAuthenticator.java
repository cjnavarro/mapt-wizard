package com.mapt.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
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
	        		long thirtyMinutes = 1000 * 60 * 20; // 30 minutes
	        		Date current = new Date();
	        		
	        		if(user.getSns() == null || ((current.getTime() - thirtyMinutes) > user.getSns().getTime()))
	        		{
	        			publishToMe(user.getUsername());
	        			user.setSns(current);
	        			
	        			dao.create(user);
	        		}
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
		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			
			TimeZone zone = TimeZone.getTimeZone("EST");
			zone.useDaylightTime();
			
			simpleDateFormat.setTimeZone(zone);
			
			String message = simpleDateFormat.format(new Date()) + ": " + username;

			PublishRequest request = new PublishRequest();
			request.setMessage(message);
			request.setPhoneNumber("+1 603 393 9047");

			AmazonSNS client = AmazonSNSClient.builder()
					.withRegion("us-east-1")
					.build();

			client.publish(request);
		}
		catch(Exception e) {}
	}
}