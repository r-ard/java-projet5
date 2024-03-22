package net.safety.alerts.safetynet.database;

import net.safety.alerts.safetynet.exceptions.database.DatabaseAlreadyInitException;
import net.safety.alerts.safetynet.exceptions.database.DatabaseParseFileException;
import net.safety.alerts.safetynet.exceptions.database.DatabaseReadFileException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONDatabase {
    private static JSONDatabase instance = null;

    public static JSONDatabase getInstance() { return instance; }

    public static void initDatabase(String jsonPath) throws DatabaseAlreadyInitException, DatabaseReadFileException, DatabaseParseFileException {
        if(instance != null) throw new DatabaseAlreadyInitException();

        instance = new JSONDatabase(jsonPath);
    }

    //private final Logger logger = LogManager.getLogger("LOL");

    private final JSONObject jsonData;

    private JSONDatabase(String jsonPath) throws DatabaseReadFileException, DatabaseParseFileException {
        String jsonData = "";

        try {
            jsonData = Files.readString(Paths.get(jsonPath), StandardCharsets.UTF_8);
        }
        catch(Exception e) {
            //logger.error(e);
            throw new DatabaseReadFileException(jsonPath);
        }

        try {
            this.jsonData = new JSONObject(jsonData);
        }
        catch(Exception e) {
            //logger.error(e);
            throw new DatabaseParseFileException(jsonPath);
        }
    }

    public JSONObject getJSONData() { return this.jsonData; }
}
