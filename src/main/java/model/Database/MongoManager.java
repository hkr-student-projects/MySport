package model.Database;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import eu.dozd.mongo.MongoMapper;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoManager {

    static {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);
    }

    public void addActivity(LocalDate date, Activity activity){
        try(MongoClient client = getClient()) {
            MongoCollection<Day> collection = getCollection(client, date);
            if (collection.find(Filters.eq("_id", date.getDayOfMonth())).first() == null)
                insertDay(collection, date.getDayOfMonth());
            collection.updateOne(Filters.eq("_id", date.getDayOfMonth()),// "_id" is day
                    Updates.addToSet("activities", activity
            ));
            //printAll(collection);
        }
    }

    public long removeDay(LocalDate date){
        try(MongoClient client = getClient()) {
            return getCollection(client, date).deleteOne(Filters.eq(
                    "_id", date.getDayOfMonth())
            ).getDeletedCount();
        }
    }

    public UpdateResult removeActivity(LocalDate date, String sport){
        BasicDBObject selectID = new BasicDBObject("activities",
                new BasicDBObject( "_id", sport));
        BasicDBObject pull = new BasicDBObject("$pull", selectID);
        try(MongoClient client = getClient()) {
            return getCollection(client, date).updateOne(
                    Filters.eq("_id", date.getDayOfMonth()),
                    pull
            );
        }
    }



//    DBObject listItem = new BasicDBObject("scores", new BasicDBObject("type","quiz").append("score",99));
//    DBObject updateQuery = new BasicDBObject("$push", listItem);
//    myCol.update(findQuery, updateQuery);



    public UpdateResult addParticipant(LocalDate date, String sport, int id, boolean isLeader){
        String table = isLeader ? "leaders" : "members";
        try(MongoClient client = getClient()) {
            return getCollection(client, date).updateOne(
                    BasicDBObject.parse("{ _id: "+date.getDayOfMonth()+", \"activities._id\": \""+sport+"\" }"),
                    BasicDBObject.parse("{ $push: {\"activities.$."+table+"\": "+id+"}}")
            );
        }
    }

    public void printAll(MongoCollection<Day> collection){
        Consumer<Day> consumer = System.out::println;
        collection.find().forEach(consumer);
    }

    private void insertDay(MongoCollection<Day> collection, int dayOfMonth){
        collection.insertOne(new Day(dayOfMonth, new ArrayList<>(5)));
    }

    private MongoCollection<Day> getCollection(MongoClient client, LocalDate date){
        return client.getDatabase(String.valueOf(date.getYear())).getCollection(date.getMonth().name(), Day.class);
    }

    private static MongoClient getClient(){
        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(MongoMapper.getProviders()),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(new ConnectionString("mongodb+srv://admin:azino777@mysport-4hkzu.mongodb.net/test?retryWrites=true&w=majority"))
                .build();

        return MongoClients.create(settings);
    }

    @Entity
    public static final class Day {
        @Id
        private int day;
        private ArrayList<Activity> activities;

        public Day(){

        }

        public Day(int day, ArrayList<Activity> activities){
            this.day = day;
            this.activities = activities;
        }

        public ArrayList<Activity> getActivities() {
            return activities;
        }

        public void setActivities(ArrayList<Activity> activities) {
            this.activities = activities;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        @Override
        public String toString() {
            return "day: " + day + " activities count: " + activities.size();
        }
    }

    @Entity
    public static final class Activity {
        @Id
        private String name;
        private String location;
        private int rating;
        private int start; //in minutes (e.g: 15:30 is (15 * 60 + 30) = 930, then to convert back 930 div 60 + 930 mod 60)
        private int end; //in minutes (ee.g: 16:45 is (16 * 60 + 45) = 1005, then to convert back 1005 div 60 + 1005 mod 60)
        private ArrayList<Integer> leaders;
        private ArrayList<Integer> members;

        public Activity(){

        }

        public Activity(String name, String location, int rating, int start, int end, ArrayList<Integer> leaders, ArrayList<Integer> members){
            this.name = name;
            this.location = location;
            this.rating = rating;
            this.start = start;
            this.end = end;
            this.leaders = leaders;
            this.members = members;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public ArrayList<Integer> getLeaders() {
            return leaders;
        }

        public void setLeaders(ArrayList<Integer> leaders) {
            this.leaders = leaders;
        }

        public ArrayList<Integer> getMembers() {
            return members;
        }

        public void setMembers(ArrayList<Integer> members) {
            this.members = members;
        }

        @Override
        public String toString() {
            return "Location: " + location + " Rating: " + rating + " Starts: " + start / 60 + ":" + start % 60 +
                    " Ends: " + end / 60 + ":" + end % 60 + " Leaders count: " + leaders.size() + " Members count: " + members.size();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

// Server = years
// Database = year
// Collection = Month
// Document = Day

//db.games.update({'_id': 73}, {$pull: {'goals': {'goal': 4}}})
//BasicDBObject query = new BasicDBObject("_id", 73);
//    BasicDBObject fields = new BasicDBObject("goals", 
//        new BasicDBObject( "goal", 4));
//    BasicDBObject update = new BasicDBObject("$pull",fields);
//    games.update( query, update );