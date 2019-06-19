package com.mapt.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.mapt.core.User;
import com.mapt.core.mapper.UserMapper;

@RegisterMapper(UserMapper.class)
public interface UserDAO {

    @SqlQuery("select * from USER")
    List<User> getAll();
}
