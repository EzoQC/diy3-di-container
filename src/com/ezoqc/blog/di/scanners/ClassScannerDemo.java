package com.ezoqc.blog.di.scanners;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class ClassScannerDemo extends ClassScanner {
    @Override
    public <T> List<Class> findAllClassHavingAnnotation(String rootPackage, Class<T> injectableClass) {
        List<Class> returned = new LinkedList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urls = classLoader.getResources(rootPackage);

            List<File> dirs = new LinkedList<>();
            while(urls.hasMoreElements()) {
                URL next = urls.nextElement();
                File curDir = new File(next.getFile());
                returned.addAll(this.findClassesInPackage(rootPackage, curDir));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returned;
    }

    private <T> Collection<Class> findClassesInPackage(String packageName, File curDir) {
        List<Class> classes = new LinkedList<>();

        if (curDir.exists()) {
            File[] content = curDir.listFiles();
            for (File curFile : content) {
                if (curFile.isDirectory()) {
                    classes.addAll(this.findClassesInPackage(packageName + "." + curFile.getName(), curFile));
                } else {
                    try {
                        String className = packageName + "." + curFile.getName().substring(0, curFile.getName().length() - 6);
                        className = className.substring(1, className.length());
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return classes;
    }

    @Override
    public <T> Class<T> findImplementationOf(Class<T> injectableClass) {
        return null;
    }
}
