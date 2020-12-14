import collector.DBConnection;
import collector.ProcessSteps;
import collector.TaskScheduler;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileWriter;
import java.io.IOException;

/**
 * TaskScheduler (see the TaskScheduler class for more info) for the Communication Step.
 * It is responsible for reading the data from the processing step and generating a summarization csv.
 */
public class SummaryTaskScheduler extends TaskScheduler {

    public SummaryTaskScheduler(long updateInterval){
        super(updateInterval);
    }

    @Override
    /**
     * The run method is the entry point of a spawned thread. Further details are contained in inner comments.
     */
    public void run() {
        try {
            // Get the data from the processing Step.
            MongoDatabase collectionDatabase = DBConnection.getDatabase(ProcessSteps.PROCESSING);
            // Look specifically for the commit objects.
            MongoCollection<Document> commitCollection = collectionDatabase.getCollection("ProcessedCommit");
            // Wait for the previous step (processing) to finish.
            while (commitCollection.countDocuments() == 0){
                try {
                    System.out.println("COMMUNICATION: Waiting 5 seconds for the processing to finish...");
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("COMMUNICATION: Thread interrupted by manager. Closing thread.");
                    return;
                }
            }

            System.out.println("COMMUNICATION: Processing finished. Starting communication step...");

            // Open the csv file handler and write it manually.
            FileWriter csvWriter = new FileWriter("summary.csv");
            csvWriter.append("Hash" + "," + "MessageLength" + "," + "NumPreviousCommits" + "\n");
            for (Document document : commitCollection.find()){
                String hash = document.getString("hash");
                String messageLength = document.getInteger("messageLength").toString();
                String numberPreviousCommits = document.getInteger("numberPreviousCommits").toString();
                csvWriter.append(hash + "," + messageLength + "," + numberPreviousCommits + "\n");
            }
            csvWriter.flush();
            csvWriter.close();
            System.out.println("COMMUNICATION: Saved summary successfully to summary.csv.");
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("COMMUNICATION: Failed to open summary.csv. Exiting thread.");
            return;
        }
    }
}
