package com.ezoqc.blog.di.scanners;

import java.util.List;

public abstract class ClassScanner {
    public static ClassScanner getInstance() {
        return new ClassScannerDemo();
    }

    public abstract <T> List<Class> findAllClassHavingAnnotation(String rootPackage, Class<T> injectableClass);
    public abstract <T> Class<T> findImplementationOf(Class<T> injectableClass);
}
