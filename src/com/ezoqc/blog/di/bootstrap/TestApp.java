package com.ezoqc.blog.di.bootstrap;

import com.ezoqc.blog.di.annotations.Injected;
import com.ezoqc.blog.di.bootstrap.demo.User;
import com.ezoqc.blog.di.bootstrap.demo.UserService;
import com.ezoqc.blog.di.container.DiContainer;

import java.util.List;

public class TestApp {
    public static void main(String... argz) {
        DiContainer container = DiContainer.getInstance("");
        TestApp application = container.getClassInstance(TestApp.class);
        application.doSomething();
    }

    public TestApp() {
        System.out.println("Creating application...");
    }

    @Injected
    private UserService userService;

    private void doSomething() {
        List<User> users = this.userService.findAllUsers();
        users.forEach(x -> System.out.println(x));
    }

}
