package com.mapt.auth;

import javax.ws.rs.container.PreMatching;

import com.mapt.core.User;
import com.mapt.core.UserRole;

import io.dropwizard.auth.Authorizer;

@PreMatching
public class MaptAuthorizer implements Authorizer<User>
{
    @Override
    public boolean authorize(User user, String role)
    {
        return user.getRole() == UserRole.fromString(role);
    }
}
