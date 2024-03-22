package net.safety.alerts.safetynet.utils;

import net.safety.alerts.safetynet.exceptions.utils.EntityUpdateException;

public interface IEntity<T> {
    public void update(T data) throws EntityUpdateException;
}
