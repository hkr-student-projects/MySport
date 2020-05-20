package model.Tools;

import model.Logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config{

    public String languageCode;
    public String DatabaseAddress;
    public String DatabaseUsername;
    public String DatabasePassword;
    public String DatabaseName;
    public int DatabasePort;
    private String name;

    public Config(){
        this("config");
    }

    public Config(String name){
        this.name = name;
        File f = new File(name + ".json");
        if(!f.exists()){
            loadDefaults();
        }
        else {
            try (FileReader reader = new FileReader(f))
            {
                deserialize(reader);
            }
            catch (IOException ex) {
                Logger.logException(ex);
            }
        }
    }

    public void deserialize(FileReader reader) {
        try
        {
            JSONArray json = (JSONArray) new JSONParser().parse(reader);
            for(Object field : json){
                for(Object key : ((JSONObject)field).keySet()){
                    switch (key.toString()){
                        case "lang" :
                            languageCode = ((JSONObject)field).get("lang").toString();
                            break;
                        case "DatabaseAddress" :
                            DatabaseAddress = ((JSONObject)field).get("DatabaseAddress").toString();
                            break;
                        case "DatabaseUsername" :
                            DatabaseUsername = ((JSONObject)field).get("DatabaseUsername").toString();
                            break;
                        case "DatabasePassword" :
                            DatabasePassword = ((JSONObject)field).get("DatabasePassword").toString();
                            break;
                        case "DatabaseName" :
                            DatabaseName = ((JSONObject)field).get("DatabaseName").toString();
                            break;
                        case "DatabasePort" :
                            DatabasePort = Integer.parseInt((((JSONObject)field).get("DatabasePort")).toString());
                            break;
                        default:
                            break;
                    }
                }
            }

        }  catch (IOException | ParseException ex) {
            Logger.logException(ex);
        }
    }

    public String serialize() {

        JSONObject obj = new JSONObject();
        obj.put("lang", languageCode);
        obj.put("DatabaseAddress", DatabaseAddress);
        obj.put("DatabaseUsername", DatabaseUsername);
        obj.put("DatabasePassword", DatabasePassword);
        obj.put("DatabaseName", DatabaseName);
        obj.put("DatabasePort", DatabasePort);
        JSONArray json = new JSONArray();
        json.add(obj);

        return json.toJSONString();
    }

    public void loadDefaults(){
        languageCode = "en";
        DatabaseAddress = "jdbc:mysql://localhost/hotel?&allowMultiQueries=true&serverTimezone=Europe/Stockholm&useSSL=false";
        DatabaseUsername = "root";
        DatabasePassword = "password";
        DatabaseName = "hotel";
        DatabasePort = 3306;
        writeConfig();
    }

    public void writeConfig(){
        try (FileWriter file = new FileWriter(this.name + ".json", false)) {
            file.write(serialize());
            file.flush();
        } catch (IOException ex) {
            Logger.logException(ex);
        }
    }

    public String setLanguageCode(Language language){
        return this.languageCode = language.getCode();
    }
}
