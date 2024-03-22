package net.safety.alerts.safetynet.utils;

import net.safety.alerts.safetynet.database.JSONDatabase;
import net.safety.alerts.safetynet.exceptions.repository.NullDatabaseException;
import org.json.*;

import java.util.ArrayList;
import java.util.List;

public abstract class JsonRepository<T> implements IRepository<T> {
    private final List<T> entities;
    private final JSONArray jsonData;

    public JsonRepository() throws NullDatabaseException, org.json.JSONException {
        JSONDatabase database = JSONDatabase.getInstance();
        if(database == null) throw new NullDatabaseException();

        this.jsonData = this.handleJsonDataLoad(
                database.getJSONData()
        );

        this.entities = this.loadEntities();
    }

    protected List<T> loadEntities() {
        ArrayList<T> entities = new ArrayList<T>();

        for(int i = 0; i < this.jsonData.length(); i++) {
            T entity = this.toEntityInstance(this.jsonData.getJSONObject(i));
            entities.add(entity);
        }

        return entities;
    }

    protected abstract JSONArray handleJsonDataLoad(JSONObject object) throws org.json.JSONException;

    protected abstract T toEntityInstance(JSONObject object);

    protected JSONArray getJSONData() {
        return this.jsonData;
    }

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