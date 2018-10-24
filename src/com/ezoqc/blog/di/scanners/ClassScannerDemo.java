package com.ezoqc.blog.di.scanners;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClassScannerDemo extends ClassScanner {
    @Override
    public <T> List<Class> findAllClassHavingAnnotation(String rootPackage, Class<T> annotation) {
        List<Class> returned = new LinkedList<>();
        try {
            Enumeration<URL> urls = listFilesInClassLoader(rootPackage);

            List<File> dirs = new LinkedList<>();
            while(urls.hasMoreElements()) {
                URL next = urls.nextElement();
                File curDir = new File(next.getFile());
                for (Class curClass : this.findClassesInPackage(rootPackage, curDir)) {
                    if (curClass.isAnnotationPresent(annotation)) {
                        returned.add(curClass);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returned;
    }

    private Enumeration<URL> listFilesInClassLoader(String rootPackage) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResources(rootPackage);
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
    public <T> Class<? extends T> findImplementationOf(String rootPackage, Class<T> injectableClass) {
        Class returned = null;
        Enumeration<URL> urls = null;
        try {
            urls = this.listFilesInClassLoader(rootPackage);
            while(urls.hasMoreElements()) {
                URL next = urls.nextElement();
                File curDir = new File(next.getFile());
                for (Class curClass : this.findClassesInPackage(rootPackage, curDir)) {
                    if (Arrays.asList(curClass.getInterfaces()).contains(injectableClass)) {
                        returned = curClass;
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return returned;
    }
}
