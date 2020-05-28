package model.observable.subjects;


import model.observable.listeners.LocalListener;

public interface LocalSubject<T, U, V> {

    boolean addListener(LocalListener<T, U, V> var);

    boolean removeListener(LocalListener<T, U, V> var);
}
