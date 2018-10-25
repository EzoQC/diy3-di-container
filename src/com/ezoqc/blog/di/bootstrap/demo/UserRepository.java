package com.ezoqc.blog.di.bootstrap.demo;

import com.ezoqc.blog.di.annotations.Injectable;

import java.util.List;

@Injectable
public interface UserRepository {
    List<User> findAll();
}
