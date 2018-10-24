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
                    Class implementation = this.classScanner.findImplementationOf(rootPackage, currentClass);

                    this.registry.put(currentClass, null);
                    this.implementations.put(currentClass, implementation);
                });
    }

    @Override
    public <T> T getClassInstance(Class<T> testAppClass) {
        Object instance = this.registry.get(testAppClass);

        if (instance == null) {
            instance = this.implementations.get(testAppClass);

            if (instance == null) {
                try {
                    instance = testAppClass.newInstance();
                } catch (IllegalAccessException | InstantiationException e) {
                    System.out.println("Cannot create instance of " + testAppClass.getName());
                    e.printStackTrace();
                }
            }
        }



        if (instance != null) {
            final Object instancePassedToLambda = instance;
            this.fieldsScanner.findAllFieldsHavingAnnotation(instance, Injected.class)
                   .forEach(currentField -> {
                       boolean visibilityOfField = currentField.isAccessible();

                       currentField.setAccessible(true);

                       try {
                           currentField.set(instancePassedToLambda, this.getClassInstance(currentField.getType()));
                       } catch (IllegalAccessException e) {
                           e.printStackTrace();
                       }

                       currentField.setAccessible(visibilityOfField);


                   });
        }


        return (T) instance;
    }
}
