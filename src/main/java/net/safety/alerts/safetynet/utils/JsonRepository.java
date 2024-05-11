package net.safety.alerts.safetynet.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.safety.alerts.safetynet.database.JSONDatabase;
import net.safety.alerts.safetynet.exceptions.repository.NullDatabaseException;
import org.json.*;

import java.util.ArrayList;
import java.util.List;

public abstract class JsonRepository<T> implements IRepository<T> {
    private final List<T> entities;
    private final JSONArray jsonData;

    private final Class<T> entityClass;

    private final ObjectMapper objectMapper;

    public JsonRepository(Class<T> entityClass) throws NullDatabaseException, org.json.JSONException {
        JSONDatabase database = JSONDatabase.getInstance();
        if(database == null) throw new NullDatabaseException();

        this.entityClass = entityClass;
        this.objectMapper = new ObjectMapper();

        this.jsonData = this.handleJsonDataLoad(
                database.getJSONData()
        );

        this.entities = this.loadEntities();
    }

    public void resetEntities() {
        List<T> reloadedEntities = this.loadEntities();

        this.entities.clear();
        this.entities.addAll(reloadedEntities);
    }

    protected List<T> loadEntities() {
        ArrayList<T> entities = new ArrayList<T>();

        for(int i = 0; i < this.jsonData.length(); i++) {
            T entity =  this.toEntityInstance(this.jsonData.getJSONObject(i));
            entities.add(entity);
        }

        return entities;
    }

    private T toEntityInstance(JSONObject object) {
        try {
            return this.objectMapper.readValue(object.toString(), this.entityClass);
        }
        catch(Exception e) {
            System.out.println("Failed to parse entity, " + e.getMessage());
        }
        return null;
    }

    protected abstract JSONArray handleJsonDataLoad(JSONObject object) throws org.json.JSONException;

    protected List<T> getEntities() { return this.entities; }

    public boolean insert(T entity) {
        if(this.getIndexOf(entity) != -1) return false;

        this.entities.add(entity);
        return true;
    }

    public boolean remove(T entity) {
        return this.entities.remove(entity);
    }

    public int getIndexOf(T entity) {
        return this.entities.indexOf(entity);
    }

    public T getByIndex(int index) {
        return this.entities.get(index);
    }

    public int getCount() {
        return this.entities.size();
    }

    public List<T> getAll() {
        return new ArrayList<>(this.entities);
    }
}