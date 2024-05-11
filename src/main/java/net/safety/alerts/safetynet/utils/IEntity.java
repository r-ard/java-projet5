package net.safety.alerts.safetynet.utils;

import net.safety.alerts.safetynet.exceptions.entities.EntityUpdateException;

public interface IEntity<T> {
    public void update(T data) throws EntityUpdateException;
}
