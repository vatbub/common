package common;

/*-
 * #%L
 * FOKProjects Common
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


public abstract class Singleton<T extends Singleton<T>> {
    private T instance;

    /**
     * Returns a static instance of {@code T}
     *
     * @return A static instance of {@code T}
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible.
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason.
     */
    public abstract T getInstance() throws IllegalAccessException, InstantiationException;

    /**
     * Returns a static instance of {@code T}
     *
     * @param subclass The {@code Class} of the subclass
     * @return A static instance of {@code T}
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible.
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason.
     */
    protected T getInstance(Class<T> subclass) throws IllegalAccessException, InstantiationException {
        if (instance == null) {
            instance = subclass.newInstance();
        }

        return instance;
    }
}
