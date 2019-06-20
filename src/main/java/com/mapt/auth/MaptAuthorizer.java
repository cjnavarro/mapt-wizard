package com.mapt.auth;

import javax.ws.rs.container.PreMatching;

import com.mapt.core.User;

import io.dropwizard.auth.Authorizer;

@PreMatching
public class MaptAuthorizer implements Authorizer<User>
{
    @Override
    public boolean authorize(User user, String role)
    {
        return (user.getName().equals("Chris Navarro") && role.equals("ADMIN")) 
        		|| (user !=null && !role.equals("ADMIN"));
    }
}
