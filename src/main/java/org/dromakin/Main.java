package org.dromakin;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String JSON_FILENAME = "new_data.json";
    private static final String PATH_JSON_FILENAME = Paths.get(JSON_FILENAME).toAbsolutePath().toString();

    public static void main(String[] args) {

        try {
            logger.info("Start reading json");
            String jsonString = readString(PATH_JSON_FILENAME);

            logger.info("Get list of objects");
            List<Employee> jsonList = jsonToList(jsonString);

            logger.info("Result:");
            for (Employee emp : jsonList) {
                logger.info(emp.toString());
            }

        } catch (ParserException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static String readString(String fileName) throws ParserException {
        StringBuilder result = new StringBuilder();

        try (BufferedReader buf = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = buf.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            throw new ParserException(e.getMessage(), e);
        }

        return result.toString();
    }

    private static List<Employee> jsonToList(String jsonString) throws ParserException {

        List<Employee> result = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();

        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonString);

            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                Employee emp = gson.fromJson(jsonObject.toString(), Employee.class);
                result.add(emp);
            }

        } catch (ParseException e) {
            throw new ParserException(e.getMessage(), e);
        }

        return result;
    }

}