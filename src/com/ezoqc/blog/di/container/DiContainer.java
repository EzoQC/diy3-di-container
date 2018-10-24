package com.ezoqc.blog.di.container;

import com.ezoqc.blog.di.bootstrap.TestApp;

public abstract class DiContainer {
    public static DiContainer getInstance(String rootPackage) {
        return new DiContainerDemo(rootPackage);
    }

    public abstract <T> T getClassInstance(Class<T> testAppClass);
}
