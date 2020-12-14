import collector.DBConnection;
import collector.DataObject;
import collector.ProcessSteps;
import collector.TaskScheduler;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TaskScheduler (see the TaskScheduler class for more info) for the Processing Step.
 * It is responsible for reading the data from the collection step and calculating two metrics
 * (commit message length and the number of previous commit from the author of a commit).
 * More details are contained in the inner comments.
 */
public class MetricCalculatorTaskScheduler extends TaskScheduler {

    /**
     * The run method is the entry point of a spawned thread. Further details are contained in inner comments.
     */
    @Override
    public void run() {
        // Clear the database before populating it.
        collector.DBConnection.dropDatabase(ProcessSteps.PROCESSING);
        // Get the data from the collection Step.
        MongoDatabase collectionDatabase = DBConnection.getDatabase(ProcessSteps.COLLECTION);
        // Look specifically for the commit objects.
        MongoCollection<Document> commitCollection = collectionDatabase.getCollection("CollectedCommit");
        // Wait for the previous step (collection) to finish.
        while (commitCollection.countDocuments() == 0){
            try {
                System.out.println("PROCESSING: Waiting 5 seconds for the collection to finish...");
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                System.out.println("PROCESSING: Thread interrupted by manager. Closing thread.");
                return;
            }
        }

        System.out.println("PROCESSING: Collection finished. Started processing data...");

        // Create a list for the data objects that will be generated in the following loop.
        ArrayList<DataObject> processedCommitsList = new ArrayList<>();
        HashMap<String, Long> authorTracking = new HashMap<>();
        // Iterate over the commits on the database.
        for (Document document : commitCollection.find()){
            String hash = document.getString("hash");
            long messageLength = document.getString("message").length();
            String authorName = document.get("author", Document.class).getString("name");
            String email = document.get("author", Document.class).getString("emailAddress");
            String key = authorName + email;
            // Use a hash map to persist the number of previous commits that a developer has authored.
            // When we read a commit, if the author was never seen, we use his identity as the key and set his value as 1.
            // If the author was previously seen, we add 1 to that value and continue.
            authorTracking.compute(key, (k, v) -> {
                if (v == null){
                    return Long.valueOf(1);
                } else {
                    return v + 1;
                }
            });
            long numberPreviousCommits = authorTracking.get(key);
            processedCommitsList.add(new ProcessedCommit(hash, messageLength, numberPreviousCommits));
        }

        // Add all objects generated in the previous loop to the database.
        collector.DBConnection.storeData(processedCommitsList, ProcessSteps.PROCESSING);
        System.out.println("PROCESSING: Processed and saved data successfully.");
    }
}

