package com.ezoqc.blog.di.scanners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class FieldsScannerDemo extends FieldsScanner {
    @Override
    public <T extends Annotation> List<Field> findAllFieldsHavingAnnotation(Object instance, Class<T> annotation) {
        List<Field> fieldsAnnotated = new LinkedList<>();

        System.out.println("Looking for fields annotated with " + annotation.getSimpleName() + " on instance of " + instance.getClass().getSimpleName());
        for (Field currentField : instance.getClass().getDeclaredFields()) {
            if (currentField.isAnnotationPresent(annotation)) {
                System.out.println("Field " + currentField.getName() + " of type " + currentField.getType().getSimpleName() + " found.");
                fieldsAnnotated.add(currentField);
            }
        }

        if (fieldsAnnotated.size() == 0) {
            System.out.println("No injected fields found on type " + instance.getClass().getSimpleName());
        }

        return fieldsAnnotated;
    }
}
