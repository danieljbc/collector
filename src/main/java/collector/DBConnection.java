package collector;

import java.io.IOException;
import java.util.ArrayList;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

/**
 * Class that abstracts connections to the database.
 * It uses the Singleton pattern to manage database connections.
 * It also provided helper methods for common database operations.
 */

public class DBConnection {
    static MongoClient instance = null;

    /**
     * Singleton method. Returns an instance to a database connection object.
     * @return database connection instance.
     */
    private static MongoClient getConnection() {
        if (instance == null) {
            instance = MongoClients.create();
        }
        return instance;
    }

    /**
     * Uses a connection to return an object representing a single database (a collection of documents)
     * inside the database. We use this mechanism to separate the different steps of the process
     * in the database.
     * @param step Step whose database should be retrieved.
     * @return Object representing the requested database.
     */
    public static MongoDatabase getDatabase(ProcessSteps step) {
        MongoClient client = DBConnection.getConnection();
        MongoDatabase db = client.getDatabase(step.toString());
        return db;
    }

    /**
     * In the case of non updatable task. It is common to wipe the database before making any new changes.
     * @param step step whose database will be wiped.
     */
    public static void dropDatabase(ProcessSteps step){
        MongoClient client = DBConnection.getConnection();
        MongoDatabase db = client.getDatabase(step.toString());
        db.drop();
    }

    /**
     * Method that, after requesting for the conversion between DataObjects and a format that is usable by MongoDB,
     * stores that information in a database.
     * @param data data that will be insernet in the database.
     * @param step step that the data being inserted into the database refers to.
     */
    synchronized public static void storeData(ArrayList<DataObject> data, ProcessSteps step) {
        ArrayList<Document> dataDocs = DataConverter.convertDataObjectToDocument(data);
        MongoDatabase database = getDatabase(step);
        // Groups the objects by the simple name of their class.
        // This can create conflicts in cases where DataObjects have duplicated names.
        // This also means that multiple types of DataObjects can be collected in the same step.
        // The approach of using multiple objects is preferable, as it is more easily manageable than using nested objects.
        database.getCollection(data.get(0).getClass().getSimpleName()).insertMany(dataDocs);
    }

    /**
     * Generates a JSON file containing a snapshot of the data that is currently on the database, for a specific step.
     * This method is called only by the Collection object, to avoid calling this class directly.
     */
    protected static void exportData(ProcessSteps step){
        try {
            Runtime.getRuntime().exec("mongoexport --host localhost --port 27017 " +
                    "-d" + step.toString() +
                    "-o ./dump/" + step.toString() + ".json");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}