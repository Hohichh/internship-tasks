package io.hohichh.appcontext;

import io.hohichh.appcontext.annotations.Autowired;
import io.hohichh.appcontext.annotations.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class MiniApplicationContext {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public MiniApplicationContext(String packageName) {
        try {
            Set<Class<?>> componentClasses = scanPackageForComponents(packageName);

            for (Class<?> clazz : componentClasses) {
                Object beanInstance = clazz.getDeclaredConstructor().newInstance();
                beans.put(clazz, beanInstance);
            }

            for (Object bean : beans.values()) {
                injectDependencies(bean);
            }

            for (Object bean : beans.values()) {
                if (bean instanceof InitializingBean) {
                    ((InitializingBean) bean).afterPropertiesSet();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MiniApplicationContext", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        Object bean = beans.get(type);
        if (bean == null) {
            for (Object b : beans.values()) {
                if (type.isAssignableFrom(b.getClass())) {
                    return (T) b;
                }
            }
        }
        if (bean == null) {
            throw new RuntimeException("Bean of type " + type.getSimpleName() + " not found.");
        }
        return (T) bean;
    }

    private void injectDependencies(Object bean) throws IllegalAccessException {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Class<?> fieldType = field.getType();
                Object dependency = getBean(fieldType);
                field.setAccessible(true);
                field.set(bean, dependency);
            }
        }
    }

    private Set<Class<?>> scanPackageForComponents(String packageName) throws IOException, URISyntaxException, ClassNotFoundException {
        Set<Class<?>> componentClasses = new HashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resourceUrl = resources.nextElement();
            if ("file".equals(resourceUrl.getProtocol())) {
                componentClasses.addAll(findClassesInDirectory(new File(resourceUrl.toURI()), packageName));
            } else if ("jar".equals(resourceUrl.getProtocol())) {
                componentClasses.addAll(findClassesInJar(resourceUrl, path));
            }
        }
        return componentClasses;
    }

    private Set<Class<?>> findClassesInDirectory(File directory, String packageName) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClassesInDirectory(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Component.class)) {
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }

    private Set<Class<?>> findClassesInJar(URL jarUrl, String packagePath) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        try (JarInputStream jarStream = new JarInputStream(jarUrl.openStream())) {
            JarEntry entry;
            while ((entry = jarStream.getNextJarEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.startsWith(packagePath) && entryName.endsWith(".class")) {
                    String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Component.class)) {
                        classes.add(clazz);
                    }
                }
            }
        }
        return classes;
    }
}