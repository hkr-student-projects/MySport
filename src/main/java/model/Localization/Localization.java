package model.Localization;

import model.App;
import model.Logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Deprecated(since = "06.05.2020")
public class Localization {


    private static Map<String, String> bookingInfo;
    private static Map<String, String> booking;
    private static Map<String, String> admin;
    private static Map<String, String> intro;

    static {
        bookingInfo = new HashMap<String, String>();
        booking = new HashMap<String, String>();
        admin = new HashMap<String, String>();
        intro = new HashMap<String, String>();
        loadLocalization();
        loadDefaults();
        //runs before static methods
    }


    private static String Serialize() {

        JSONObject obj = new JSONObject();
        bookingInfo.entrySet().forEach(pair -> obj.put(pair.getKey(), pair.getValue()));
        JSONArray json = new JSONArray();
        json.add(obj);

        return json.toJSONString();
    }

    //@Override
    private static void Deserialize(FileReader reader) {

        //HashMap<String, String> tran = new HashMap<String, String>();
        try
        {
            JSONParser jsonParser = new JSONParser();
            Object json = jsonParser.parse(reader);

            JSONArray arr = (JSONArray) json;
            arr.forEach( obj -> {
                ((JSONObject)obj).forEach((key, value) -> {
                    //_translations.put((String)key, (String)value);
                });
            } );

        }  catch (IOException | ParseException ex) {
            Logger.logException(ex);
        }

        //return tran;
    }

    private static void loadLocalization(){
        File d = new File("Localization");
        if(!d.exists())
            d.mkdir();
        File[] fs = new File[] {
                new File("Localization//"+ App.config.languageCode +".menu.json"),
                new File("Localization//"+ App.config.languageCode +".bookinginfo.json"),
                new File("Localization//"+ App.config.languageCode +".booking.json"),
                new File("Localization//"+ App.config.languageCode +".admin.json")
        };
        for(File f : fs){
            if(!f.exists()){
                Logger.logError("Unable to load localization language pack: "+ App.config.languageCode +", because pack does not exist");
                return;
            }
            else {

                try (FileReader reader = new FileReader(""+ App.config.languageCode +".translation.json"))
                {
                    Deserialize(reader);
                }
                catch (IOException ex) {
                    Logger.logException(ex);
                    return;
                }
            }
        }
    }
    // "Name", "Surname", "19890518-4376", "+073-751-06-21", "Storagatan 12A-1006"
    private static void loadDefaults(){
        try (FileWriter file = new FileWriter("Localization//en.booking.json", false)) {

            bookingInfo.put("name", "Name");
            bookingInfo.put("surname", "Surname");
            bookingInfo.put("ssn", "SSN");
            bookingInfo.put("phone", "Phone");
            bookingInfo.put("Address", "Address");
            file.write(Serialize());
            file.flush();

        } catch (IOException ex) {
            Logger.logException(ex);
        }
    }



}
