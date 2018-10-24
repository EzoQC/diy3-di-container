package com.ezoqc.blog.di.scanners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class FieldsScannerDemo extends FieldsScanner {
    @Override
    public <T extends Annotation> List<Field> findAllFieldsHavingAnnotation(Object instance, Class<T> annotation) {
        List<Field> fieldsAnnotated = new LinkedList<>();

        for (Field currentField : instance.getClass().getDeclaredFields()) {
            if (currentField.isAnnotationPresent(annotation)) {
                fieldsAnnotated.add(currentField);
            }
        }

        return fieldsAnnotated;
    }
}
