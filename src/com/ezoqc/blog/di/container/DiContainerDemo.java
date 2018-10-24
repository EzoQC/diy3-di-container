package com.ezoqc.blog.di.container;

import com.ezoqc.blog.di.annotations.Injectable;
import com.ezoqc.blog.di.annotations.Injected;
import com.ezoqc.blog.di.scanners.FieldsScanner;
import com.ezoqc.blog.di.scanners.ClassScanner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiContainerDemo extends DiContainer {
    private ClassScanner classScanner;
    private FieldsScanner fieldsScanner;

    private Map<Class<Injectable>, Class<Injectable>> implementations;
    private Map<Class<Injectable>, Injectable> registry;

    public DiContainerDemo(String rootPackage) {
        this.fieldsScanner = FieldsScanner.getInstance();
        this.classScanner = ClassScanner.getInstance();

        this.registry = new HashMap<>();
        this.implementations = new HashMap<>();

        this.classScanner.findAllClassHavingAnnotation(rootPackage, Injectable.class)
                .forEach(currentClass -> {
                    Class<Injectable> implementation = this.classScanner.findImplementationOf(Injectable.class);

                    this.registry.put(currentClass, null);
                    this.implementations.put(currentClass, implementation);
                });
    }

    @Override
    public <T> T getClassInstance(Class<T> testAppClass) {
        T instance = null;

        Injectable injectable = this.registry.get(testAppClass);

        if (injectable == null) {
            Class<Injectable> injectableType = this.implementations.get(testAppClass);

            if (injectableType == null) {
                try {
                    instance = testAppClass.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    System.out.println("Cannot create instance of " + testAppClass.getName());
                    e.printStackTrace();
                }
            }
        }

        if (injectable != null) {
           this.fieldsScanner.findAllFieldsHavingAnnotation(injectable, Injected.class)
                   .forEach(currentField -> {
                       boolean visibilityOfField = currentField.isAccessible();

                       currentField.setAccessible(true);
                       try {
                           currentField.set(injectable, this.getClassInstance(currentField.getDeclaringClass()));
                       } catch (IllegalAccessException e) {
                           e.printStackTrace();
                       }
                       currentField.setAccessible(visibilityOfField);


                   });
        }


        return instance;
    }
}
