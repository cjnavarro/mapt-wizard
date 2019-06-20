package com.mapt.auth;

import com.mapt.core.User;

import io.dropwizard.auth.Authorizer;

public class MaptAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        return user.getName().equals("Chris Navarro") && role.equals("ADMIN");
    }
}