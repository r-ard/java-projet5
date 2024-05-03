package net.safety.alerts.safetynet.database;

import net.safety.alerts.safetynet.exceptions.database.DatabaseAlreadyInitException;
import net.safety.alerts.safetynet.exceptions.database.DatabaseParseFileException;
import net.safety.alerts.safetynet.exceptions.database.DatabaseReadFileException;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
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

    public static void initDatabase(URL jsonUrl) throws DatabaseAlreadyInitException, DatabaseReadFileException, DatabaseParseFileException {
        if(instance != null) throw new DatabaseAlreadyInitException();

        instance = new JSONDatabase(jsonUrl);
    }

    private final Logger logger = LoggerFactory.getLogger(JSONDatabase.class);

    private final JSONObject jsonData;

    private JSONDatabase(URL url) throws DatabaseReadFileException, DatabaseParseFileException {
        this(url.getFile());
    }

    private JSONDatabase(String jsonPath) throws DatabaseReadFileException, DatabaseParseFileException {
        String jsonData = "";

        try {
            jsonData = Files.readString(Paths.get(jsonPath), StandardCharsets.UTF_8);
        }
        catch(Exception e) {
            logger.error(e.getMessage());
            throw new DatabaseReadFileException(jsonPath);
        }

        try {
            this.jsonData = new JSONObject(jsonData);
        }
        catch(Exception e) {
            logger.error(e.getMessage());
            throw new DatabaseParseFileException(jsonPath);
        }

        logger.info("Successfully loaded JSON datas from '" + jsonPath + "' !");
    }

    public JSONObject getJSONData() { return this.jsonData; }
}
