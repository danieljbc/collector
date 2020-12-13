import collector.DBConnection;
import collector.ProcessSteps;
import collector.TaskScheduler;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.FileWriter;
import java.io.IOException;

public class SummaryTaskScheduler extends TaskScheduler {

    public SummaryTaskScheduler(long updateInterval){
        super(updateInterval);
    }

    @Override
    public void run() {
        try {
            MongoDatabase collectionDatabase = DBConnection.getDatabase(ProcessSteps.PROCESSING);
            MongoCollection<Document> commitCollection = collectionDatabase.getCollection("ProcessedCommit");
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
