package com.ezoqc.blog.di.scanners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class FieldsScannerDemo extends FieldsScanner {
    @Override
    public <T extends Annotation> List<Field> findAllFieldsHavingAnnotation(Object instance, Class<T> annotation) {
        for (Field currentField : instance.getClass().getDeclaredFields()) {
            T[] annotationsFound = currentField.getAnnotationsByType(annotation);
            System.out.println(annotationsFound.length);
        }
        return new LinkedList<>();
    }
}
