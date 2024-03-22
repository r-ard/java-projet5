package net.safety.alerts.safetynet.utils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static String[] getJsonArrayStrings(JSONArray array) {
        List<Object> data = array.toList();

        List<String> dataStrings = new ArrayList<>();

        for(Object item : data) {
            if(item instanceof String) dataStrings.add((String)item);
        }

        return dataStrings.toArray(new String[0]);
    }
}
