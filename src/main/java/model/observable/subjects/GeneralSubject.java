package model.observable.subjects;

import model.observable.listeners.GeneralListener;

public interface GeneralSubject<T> {
    boolean addListener(GeneralListener<T> var1) throws Exception;

    boolean removeListener(GeneralListener<T> var1) throws Exception;
}