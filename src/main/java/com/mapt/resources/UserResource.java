package com.mapt.resources;

import com.mapt.core.User;
import com.mapt.db.UserDAO;

import io.dropwizard.hibernate.UnitOfWork;

import com.codahale.metrics.annotation.Timed;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource
{
    private UserDAO userDAO;

    public UserResource(UserDAO userDAO)
    {
        this.userDAO = userDAO;
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
    	return (User) context.getUserPrincipal();
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
}
