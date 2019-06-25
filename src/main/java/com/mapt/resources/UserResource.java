package com.mapt.resources;

import com.mapt.core.User;
import com.mapt.core.UserRole;
import com.mapt.db.UserDAO;

import io.dropwizard.hibernate.UnitOfWork;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource
{
    private UserDAO userDAO;
    private AmazonSNS snsClient;
    private String topicArn;

    @Inject
    public UserResource(@Named("dao") UserDAO dao,
    		@Named("awsSNS") AmazonSNS snsClient,
    		@Named("topicArn") String topicArn)
    {
        this.userDAO = dao;
        this.snsClient = snsClient;
        this.topicArn = topicArn;
    }

    @GET
    @Timed
    @UnitOfWork 
    @RolesAllowed("ADMIN")
    public User getUserById(@QueryParam("id") Long id)
    {
        return userDAO.findById(id);
    }
    
    @GET
    @Timed
    @UnitOfWork 
    @Path("/all")
    @RolesAllowed("ADMIN")
    public List<User> getAllUsers()
    {
        return userDAO.findAll();
    }
    
    @GET
    @Timed
    @Path("/me")
    @PermitAll
    public User getMe(@Context SecurityContext context)
    {
    	User user = (User) context.getUserPrincipal();
    	
    	if(user.getRole() == UserRole.VIP)
    	{
    		publishToMe(user.getUsername());
    	}
    	
    	return user;
    }
    
    @GET
    @Timed
    @Path("/auth")
    @PermitAll
    public void checkAuth()
    {
    	// NOOP
    	return;
    }
    
	private void publishToMe(String username)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String message = simpleDateFormat.format(new Date()) + username;
		
		PublishRequest request = new PublishRequest();
		request.setMessage(message);
		request.setTopicArn(topicArn);
		
		snsClient.publish(request);
	}
}
