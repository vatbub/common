package common;

public abstract class Singleton<T extends Singleton<T>> {
    protected Class<T> subclass;
    private T instance;

    /**
     * Returns a static instance of {@code T}
     *
     * @return A static instance of {@code T}
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible.
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason.
     */
    public T getInstance() throws IllegalAccessException, InstantiationException {
        if (instance == null) {
            instance = subclass.newInstance();
        }

        return instance;
    }
}
