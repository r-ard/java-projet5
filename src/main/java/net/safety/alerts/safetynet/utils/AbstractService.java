package net.safety.alerts.safetynet.utils;

import java.util.List;

public abstract class AbstractService<T> {
    protected AbstractService() {

    }

    public int getCount() {
        return this.getRepository().getCount();
    }

    public List<T> getAll() {
        return this.getRepository().getAll();
    }

    public boolean insert(T entity) {
        return this.getRepository().insert(entity);
    }

    public boolean remove(T entity) {
        return this.getRepository().remove(entity);
    }

    public int getIndexOf(T entity) {
        return this.getRepository().getIndexOf(entity);
    }

    public T getByIndex(int index) {
        return this.getRepository().getByIndex(index);
    }

    protected abstract IRepository<T> getRepository();
}
