package com.ezoqc.blog.di.scanners;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClassScannerDemo extends ClassScanner {
    private final String rootPackage;

    public ClassScannerDemo(String rootPackage) {
        this.rootPackage = rootPackage;
    }

@Override
public <T> List<Class> findAllClassHavingAnnotation(Class<T> annotation) {
    List<Class> returned = new LinkedList<>();

    System.out.println("Looking for all classes annotated with " + annotation.getSimpleName());
    try {
        Enumeration<URL> urls = listFilesInClassLoader();

        while(urls.hasMoreElements()) {
            URL next = urls.nextElement();
            File curDir = new File(next.getFile());
            for (Class curClass : this.findClassesInPackage(curDir)) {
                if (curClass.isAnnotationPresent(annotation)) {
                    System.out.println("Class " + curClass + " found!");
                    returned.add(curClass);
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return returned;
}

private Enumeration<URL> listFilesInClassLoader() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    return classLoader.getResources("");
}

private <T> Collection<Class> findClassesInPackage(File curDir) {
    List<Class> classes = new LinkedList<>();

    if (curDir.exists()) {
        File[] content = curDir.listFiles();
        for (File curFile : content) {
            if (curFile.isDirectory()) {
                classes.addAll(this.findClassesInPackage(curFile));
            } else {
                try {
                    String path = curFile.getAbsolutePath();
                    String asPackageFormat = path.replaceAll("\\\\", ".");
                    int indexOfPackageRoot = asPackageFormat.indexOf(this.rootPackage);
                    String fullClassName = asPackageFormat.substring(indexOfPackageRoot, asPackageFormat.length() - 6);
                    classes.add(Class.forName(fullClassName));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    return classes;
}

    @Override
    public <T> Class<? extends T> findImplementationOf(Class<T> injectableClass) {
        Class returned = null;
        Enumeration<URL> urls = null;

        System.out.println("Trying to find an implementation of " + injectableClass.getSimpleName());
        try {
            urls = this.listFilesInClassLoader();
            while(urls.hasMoreElements()) {
                URL next = urls.nextElement();
                File curDir = new File(next.getFile());
                for (Class curClass : this.findClassesInPackage(curDir)) {
                    if (Arrays.asList(curClass.getInterfaces()).contains(injectableClass)) {
                        System.out.println("Class " + curClass.getSimpleName() + " found!");
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
