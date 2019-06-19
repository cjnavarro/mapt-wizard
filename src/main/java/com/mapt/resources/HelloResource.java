package com.mapt.resources;

import com.mapt.api.Hello;
import com.google.common.base.Optional;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jdbi.v3.core.Jdbi;

import java.util.concurrent.atomic.AtomicLong;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private final Jdbi jdbi;

    public HelloResource(String template, String defaultName, Jdbi jdbi) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
        this.jdbi = jdbi;
    }

    @GET
    @Timed
    public Hello sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.or(defaultName));
        return new Hello(counter.incrementAndGet(), value);
    }
}
