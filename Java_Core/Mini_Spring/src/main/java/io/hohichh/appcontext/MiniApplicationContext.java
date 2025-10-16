
/**
 * Author: Yelizaveta Verkovich aka Hohich
 * Task: Implement a simplified IoC container (MiniSpring) to understand how Inversion of Control
 * and Dependency Injection work internally. The container scans for components, manages bean
 * lifecycles (singleton, prototype), and injects dependencies using reflection.
 */

package io.hohichh.appcontext;

import io.hohichh.appcontext.annotations.Autowired;
import io.hohichh.appcontext.annotations.Component;
import io.hohichh.appcontext.annotations.Scope;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


public class MiniApplicationContext {

    private final Map<Class<?>, Object> singletonBeans = new HashMap<>();
    private final Map<Class<?>, Class<?>> prototypes = new HashMap<>();

    /**
     * Initializes the application context by scanning the specified package for components,
     * instantiating beans, and injecting dependencies.
     *
     * @param packageName The root package to scan for classes annotated with @Component.
     */
    public MiniApplicationContext(String packageName) {
        try {
            Set<Class<?>> componentClasses = scanPackageForComponents(packageName);

            for (Class<?> clazz : componentClasses) {
                Scope scope = clazz.getAnnotation(Scope.class);
                if (scope != null && scope.value().equals("prototype")) {
                    prototypes.put(clazz, clazz);
                } else {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    singletonBeans.put(clazz, instance);
                }
            }

            for (Object bean : singletonBeans.values()) {
                injectDependencies(bean);
            }

            for (Object bean : singletonBeans.values()) {
                if (bean instanceof InitializingBean) {
                    ((InitializingBean) bean).afterPropertiesSet();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MiniApplicationContext", e);
        }
    }

    /**
     * Retrieves a bean instance from the container.
     * <p>
     * If the bean is a singleton, it returns the shared instance.
     * If the bean is a prototype, it creates, configures, and returns a new instance.
     * The method can also resolve dependencies by interface or superclass.
     *
     * @param type The class of the bean to retrieve.
     * @param <T>  The generic type of the bean.
     * @return An instance of the requested bean.
     * @throws RuntimeException if a bean of the specified type cannot be found or created.
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        Object bean = singletonBeans.get(type);
        if (bean != null) {
            return (T) bean;
        }

        Class<?> prototype = prototypes.get(type);
        if (prototype != null) {
            try {
                Object protoInstance = prototype.getDeclaredConstructor().newInstance();
                injectDependencies(protoInstance);
                if (protoInstance instanceof InitializingBean) {
                    ((InitializingBean) protoInstance).afterPropertiesSet();
                }
                return (T) protoInstance;
            } catch (Exception e) {
                throw new RuntimeException("Failed to create prototype bean", e);
            }
        }

        for (Object obj : singletonBeans.values()) {
            if (type.isAssignableFrom(obj.getClass())) {
                return (T) obj;
            }
        }

        throw new RuntimeException("Bean of type " + type.getSimpleName() + " not found.");
    }

    /**
     * Injects dependencies into the fields of a given bean instance.
     * It scans for fields annotated with @Autowired and sets their values
     * by retrieving corresponding beans from the container.
     *
     * @param bean The object instance into which dependencies should be injected.
     * @throws IllegalAccessException if a field cannot be accessed.
     */
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

    /**
     * Scans a given package and its subpackages for classes annotated with @Component.
     * This method handles resources located in the file system or within JAR files.
     *
     * @param packageName The name of the package to scan (e.g., "io.hohichh.app").
     * @return A set of classes marked as components.
     * @throws IOException            if an I/O error occurs.
     * @throws URISyntaxException     if the resource URL is not a valid URI.
     * @throws ClassNotFoundException if a class file cannot be loaded.
     */
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

    /**
     * A helper method to find component classes within a directory on the file system.
     * It recursively scans directories to find all .class files.
     *
     * @param directory   The base directory to start scanning from.
     * @param packageName The corresponding package name for the current directory.
     * @return A set of component classes found in the directory.
     * @throws ClassNotFoundException if a class definition cannot be found.
     */
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

    /**
     * A helper method to find component classes within a JAR file.
     *
     * @param jarUrl      The URL of the JAR file to scan.
     * @param packagePath The resource path of the package within the JAR (e.g., "io/hohichh/app").
     * @return A set of component classes found in the JAR.
     * @throws IOException            if an I/O error occurs while reading the JAR.
     * @throws ClassNotFoundException if a class definition cannot be found.
     */
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