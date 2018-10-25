package com.ezoqc.blog.di.bootstrap;

import com.ezoqc.blog.di.annotations.Injected;
import com.ezoqc.blog.di.bootstrap.demo.User;
import com.ezoqc.blog.di.bootstrap.demo.UserService;
import com.ezoqc.blog.di.container.DiContainer;

import java.util.List;

public class TestApp {
public static void main(String... argz) {
    DiContainer container = DiContainer.getInstance("com.ezoqc.blog.di");
    TestApp application = container.getClassInstance(TestApp.class);
    application.doSomething();
}

    public TestApp() {
        System.out.println("Creating application...");
    }

    @Injected
    private UserService userService;

    private void doSomething() {
        System.out.println("Application started - Doing Something...");
        List<User> users = this.userService.findAllUsers();
        users.forEach(x -> System.out.println(x));
        System.out.println("Application ended - Bye!");
    }
}
