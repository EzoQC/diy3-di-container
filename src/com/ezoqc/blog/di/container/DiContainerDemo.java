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
    System.out.println("Creating DI container...");
    this.classScanner = ClassScanner.getInstance(rootPackage);
    this.fieldsScanner = FieldsScanner.getInstance();

    this.registry = new HashMap<>();
    this.implementations = new HashMap<>();

    System.out.println("Looking for classes annotated to be managed by the DI container...");
    this.classScanner.findAllClassHavingAnnotation(Injectable.class)
            .forEach(currentClass -> {
                Class implementation = this.classScanner.findImplementationOf(currentClass);

                System.out.println("Interface " + currentClass.getSimpleName() + " has " + implementation.getSimpleName() + " as implementation. Saving in registry.");
                this.registry.put(currentClass, null);
                this.implementations.put(currentClass, implementation);
            });
}

    @Override
    public <T> T getClassInstance(Class<T> testAppClass) {
        Object instance = this.getInstanceOf(testAppClass);
        this.injectFields(instance);
        return (T) instance;
    }

private <T> Object getInstanceOf(Class<T> classToGetInstanceOf) {
    System.out.println("Looking for instance of " + classToGetInstanceOf.getSimpleName() + " in DI container cache");
    Object instance = this.registry.get(classToGetInstanceOf);

    if (instance == null) {
        System.out.println("Instance not found. Creating.");
        try {
            instance = classToGetInstanceOf.newInstance();
            System.out.println(instance + " created.");
        } catch (IllegalAccessException | InstantiationException e) {
            System.out.println("Cannot create instance of " + classToGetInstanceOf.getName());
            e.printStackTrace();
        }
    }

    return instance;
}

private void injectFields(Object instance) {
    if (instance != null) {
        final Object instancePassedToLambda = instance;
        this.fieldsScanner.findAllFieldsHavingAnnotation(instance, Injected.class)
               .forEach(currentField -> {
                   boolean visibilityOfField = currentField.isAccessible();

                   currentField.setAccessible(true);

                   try {
                       Class<Injectable> implType = this.implementations.get(currentField.getType());
                       Object instanceCreated = this.getClassInstance(implType);
                       currentField.set(instancePassedToLambda, instanceCreated);
                   } catch (IllegalAccessException e) {
                       e.printStackTrace();
                   }

                   currentField.setAccessible(visibilityOfField);
               });
    }
}
}
