package com.mapt.resources;

import com.mapt.core.User;
import com.mapt.db.UserDAO;

import io.dropwizard.hibernate.UnitOfWork;

import com.codahale.metrics.annotation.Timed;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.List;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {
    private UserDAO userDAO;

    public HelloResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @Timed
    @UnitOfWork 
    @PermitAll
    public User sayHello(@QueryParam("id") Long id) {
        return userDAO.findById(id);
    }
    
    @GET
    @Timed
    @UnitOfWork 
    @Path("/all")
    @PermitAll
    public List<User> sayHello() {
        return userDAO.findAll();
    }
}
