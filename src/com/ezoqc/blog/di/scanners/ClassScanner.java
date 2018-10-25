package com.ezoqc.blog.di.scanners;

import java.util.List;

public abstract class ClassScanner {
    public static ClassScanner getInstance(String rootPackage) {
        return new ClassScannerDemo(rootPackage);
    }

    public abstract <T> List<Class> findAllClassHavingAnnotation(Class<T> injectableClass);
    public abstract <T> Class<? extends T> findImplementationOf(Class<T> injectableClass);
}
