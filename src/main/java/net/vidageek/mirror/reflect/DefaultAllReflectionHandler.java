/**
 * 
 */
package net.vidageek.mirror.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.provider.ReflectionProvider;
import net.vidageek.mirror.reflect.dsl.AllAnnotationsHandler;
import net.vidageek.mirror.reflect.dsl.AllReflectionHandler;

/**
 * This class is responsible for reflecting lists of reflection elements
 * 
 * @author jonasabreu
 */
public final class DefaultAllReflectionHandler<T> implements AllReflectionHandler<T> {

    private final Class<T> clazz;

    private final ReflectionProvider provider;

    public DefaultAllReflectionHandler(final ReflectionProvider provider, final Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz cannot be null");
        }
        this.provider = provider;
        this.clazz = clazz;
    }

    public List<Field> fields() {
        return provider.getClassReflectionProvider(clazz).reflectAllFields();
    }

    public List<Method> methods() {
        return provider.getClassReflectionProvider(clazz).reflectAllMethods();
    }

    public List<Constructor<T>> constructors() {
        return provider.getClassReflectionProvider(clazz).reflectAllConstructors();
    }

    public AllAnnotationsHandler annotations() {
        return new DefaultAllAnnotationsHandler(provider, clazz);
    }

    public List<Method> setters() {
        List<Method> list = new ArrayList<Method>();
        for (Method method : new Mirror(provider).on(clazz).reflectAll().methods()) {
            if (method.getName().startsWith("set") && (method.getParameterTypes().length == 1)
                    && (method.getReturnType().equals(void.class))) {
                list.add(method);
            }
        }
        return list;
    }

    public List<Method> getters() {
        List<Method> list = new ArrayList<Method>();
        for (Method method : new Mirror(provider).on(clazz).reflectAll().methods()) {
            if (method.getName().startsWith("get") && (method.getParameterTypes().length == 0)
                    && (!method.getReturnType().equals(void.class))) {
                list.add(method);
            }
        }
        return list;
    }

}
