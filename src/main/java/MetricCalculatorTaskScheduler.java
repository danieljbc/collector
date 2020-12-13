import collector.DBConnection;
import collector.DataObject;
import collector.ProcessSteps;
import collector.TaskScheduler;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class MetricCalculatorTaskScheduler extends TaskScheduler {

    @Override
    public void run() {
        collector.DBConnection.dropDatabase(ProcessSteps.PROCESSING);

        MongoDatabase collectionDatabase = DBConnection.getDatabase(ProcessSteps.COLLECTION);
        MongoCollection<Document> commitCollection = collectionDatabase.getCollection("CollectedCommit");
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

        ArrayList<DataObject> processedCommitsList = new ArrayList<>();
        HashMap<String, Long> authorTracking = new HashMap<>();
        for (Document document : commitCollection.find()){
            String hash = document.getString("hash");
            long messageLength = document.getString("message").length();
            String authorName = document.get("author", Document.class).getString("name");
            String email = document.get("author", Document.class).getString("emailAddress");
            String key = authorName + email;
            authorTracking.compute(key, (k, v) -> {
                if (v == null){
                    return Long.valueOf(0);
                } else {
                    return v + 1;
                }
            });
            long numberPreviousCommits = authorTracking.get(key);
            processedCommitsList.add(new ProcessedCommit(hash, messageLength, numberPreviousCommits));
        }

        collector.DBConnection.storeData(processedCommitsList, ProcessSteps.PROCESSING);
        System.out.println("PROCESSING: Processed and saved data successfully.");
    }
}

