import collector.DBConnection;
import collector.DataObject;
import collector.ProcessSteps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fixtures.DummyObject;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Class that contains the database test suite.
 */
public class DatabaseTests {

    private MongoDatabase database;

    /**
     * Acquires the connection to the database before executing the tests.
     */
    @Before
    public void before(){
        System.out.println("Acquiring connection to the database...");
        this.database = DBConnection.getDatabase(ProcessSteps.COLLECTION);
    }

    /**
     * Tests the connection by using our helper methods to acquire the database, but only uses methods
     * from the MongoClient library to effectively test the connection.
     */
    @Test
    public void connection(){
        System.out.println("Instantiating collection Test from the database.");
        MongoCollection<Document> collection = database.getCollection("Test");
        System.out.println("Inserting empty document in the database.");
        collection.insertOne(new Document());
        System.out.printf("Number of documents in the database: %d%n", collection.countDocuments());
        assert(collection.countDocuments() > 0);
        System.out.println("Dropping collection Test from the database.");
        collection.drop();
    }

    /**
     * Tests the helper method that inserts DataObjects a database.
     */
    @Test
    public void insertion(){
        System.out.println("Dropping the database.");
        DBConnection.dropDatabase(ProcessSteps.COLLECTION);
        System.out.println("Instantiating collection Test from the database.");
        MongoCollection<Document> collection = database.getCollection("DummyObject");
        assert(collection.countDocuments() == 0);
        System.out.println("Generating five dummy objects to be added to the database");
        ArrayList<DataObject> dummyObjects = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            dummyObjects.add(new DummyObject(i));
        }
        System.out.println("Inserting five dummy objects into the database.");
        DBConnection.storeData(dummyObjects, ProcessSteps.COLLECTION);
        assert(collection.countDocuments() == 5);
        System.out.println("Dropping database");
        collection.drop();
    }

    /**
     * Tests the helper method that clears a database.
     */
    @Test
    public void deletion(){
        System.out.println("Instantiating collection Test from the database.");
        MongoCollection<Document> collection = database.getCollection("Test");
        System.out.println("Inserting empty document in the database.");
        collection.insertOne(new Document());
        System.out.printf("Number of documents in the database: %d%n", collection.countDocuments());
        System.out.println("Dropping the database using the helper method from DBConnection.");
        DBConnection.dropDatabase(ProcessSteps.COLLECTION);
        System.out.printf("Number of documents in the database after deletion: %d%n", collection.countDocuments());
        assert(collection.countDocuments() == 0);
    }
}
