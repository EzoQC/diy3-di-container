package com.ezoqc.blog.di.bootstrap.demo;

import com.ezoqc.blog.di.annotations.Injected;

import java.util.List;

public class UserServiceDemo implements UserService {
    @Injected
    private UserRepository userRepository;

    @Override
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }
}
