package com.ezoqc.blog.di.bootstrap.demo;

import java.util.Arrays;
import java.util.List;

public class UserRepositoryDemo implements UserRepository {

    @Override
    public List<User> findAll() {
        return Arrays.asList(
                new User(0),
                new User(1),
                new User(2),
                new User(3),
                new User(4));
    }
}
