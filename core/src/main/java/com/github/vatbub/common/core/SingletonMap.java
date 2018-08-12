package com.github.vatbub.common.core;

/*-
 * #%L
 * FOKProjects Common Core
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.HashMap;
import java.util.Map;

/**
 * Class that tries to imitate a generic Singleton class. Since a traditional generic singleton class is not possible in Java,
 * this class tries to simplify the process of implementing a Singleton by storing instances in a {@code HashMap}.
 */
public class SingletonMap {
    private static Map<Class, Object> classObjectMap;

    private SingletonMap() {
        throw new IllegalStateException("Class may not be instantiated");
    }

    private static Map<Class, Object> getOrInstantiateClassObjectMap() {
        if (classObjectMap == null) {
            classObjectMap = new HashMap<>();
        }
        return classObjectMap;
    }

    /**
     * Resets the instances of all classes by resetting the SingletonMap class itself.
     */
    public static void resetSingletonMap() {
        classObjectMap = null;
    }

    /**
     * Causes the instance of the specified class to be reset.
     * When calling {@link #getInstance(Class)} for the specified class the next time, a new instance will be created.
     *
     * @param clazz The class of which the instance shall be reset.
     */
    public static void resetInstance(Class clazz) {
        getOrInstantiateClassObjectMap().remove(clazz);
    }

    public static <T> boolean isInstantiated(Class<T> clazz) {
        return getOrInstantiateClassObjectMap().containsKey(clazz);
    }

    /**
     * Throws an IllegalStateException if {@link #isInstantiated(Class)} returns {@code true}, does nothing otherwise
     *
     * @param clazz The class to check if it is already instantiated
     * @param <T>   The type of the class to check
     */
    public static <T> void throwIllegalStateExceptionIfAlreadyInstantiated(Class<T> clazz) {
        if (isInstantiated(clazz)) {
            throw new IllegalStateException(clazz.getSimpleName() + " has already been instantiated. Call SingletonMap.getInstance(" + clazz.getSimpleName() + ".class) to get the initialized instance.");
        }
    }

    /**
     * Returns an instance of the specified class. If this method is called for the same class several times, the same instance will be returned every time.
     * For this method to work, the class must have a public parameterless constructor.
     * However, the constructor may call {@link #throwIllegalStateExceptionIfAlreadyInstantiated(Class)} to ensure that only one instance
     * is created.
     *
     * @param clazz The class to return a type of
     * @param <T>   The type of the class
     * @return The instance of that class
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible.
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason
     */
    public static <T> T getInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        if (!isInstantiated(clazz)) {
            T object = clazz.newInstance();
            getOrInstantiateClassObjectMap().put(clazz, object);
            return object;
        }

        //noinspection unchecked
        return (T) getOrInstantiateClassObjectMap().get(clazz);
    }
}
