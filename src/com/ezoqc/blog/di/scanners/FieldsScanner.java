package com.ezoqc.blog.di.scanners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public abstract class FieldsScanner {
    public static FieldsScanner getInstance() {
        return new FieldsScannerDemo();
    }

    public abstract <T extends Annotation> List<Field> findAllFieldsHavingAnnotation(Object instance, Class<T> annotation);
}
