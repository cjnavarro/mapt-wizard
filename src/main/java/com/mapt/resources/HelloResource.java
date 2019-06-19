package com.mapt.resources;

import com.mapt.api.Hello;
import com.mapt.core.User;
import com.mapt.db.UserDAO;

import io.dropwizard.hibernate.UnitOfWork;

import com.google.common.base.Optional;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.concurrent.atomic.AtomicLong;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private UserDAO userDAO;

    public HelloResource(String template, String defaultName, UserDAO userDAO) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
        this.userDAO = userDAO;
    }

    @GET
    @Timed
    @UnitOfWork 
    public User sayHello(@QueryParam("id") Long id) {
        return userDAO.findById(id);
        //return new Hello(counter.incrementAndGet(), value);
    }
}
