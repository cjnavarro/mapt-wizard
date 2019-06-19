package com.mapt.db;

import java.util.List;

import org.hibernate.SessionFactory;

import com.mapt.core.User;

import io.dropwizard.hibernate.AbstractDAO;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO(SessionFactory factory) {
        super(factory);
    }

    public User findById(Long id) {
        return get(id);
    }

    public long create(User person) {
        return persist(person).getId();
    }

    public List<User> findAll() {
        return list(namedQuery("com.mapt.core.User_findById"));
    }
}