package net.safety.alerts.safetynet.utils;

import java.util.List;

public interface IRepository<T> {
    public boolean insert(T entity);

    public boolean remove(T entity);

    public int getIndexOf(T entity);

    public T getByIndex(int index);

    public int getCount();

    public List<T> getAll();
}
