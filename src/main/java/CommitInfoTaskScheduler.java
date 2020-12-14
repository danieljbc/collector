import collector.DataObject;
import collector.ProcessSteps;
import collector.TaskScheduler;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * TaskScheduler (see the TaskScheduler class for more info) for the Collection Step.
 * It is responsible for reading the data from a git repository and generating objects with metadata from commits.
 */
public class CommitInfoTaskScheduler extends TaskScheduler {

    private static final String REMOTE_URL = "https://github.com/opus-research/organic.git";
    private static final String REPO_PATH = "temp/";

    /**
     * Helper method to recursively delete a directory.
     * @param f File object that points to a folder.
     * @throws IOException when the deletion fails.
     */
    static private void deleteFolder(File f) throws IOException {
        if (f.isDirectory())
            for (File c : f.listFiles())
                deleteFolder(c);
        f.delete();
    }

    /**
     * Method that receives the path to a folder that will be used to store the repository, sees if it already exists.
     * If it already exists, delete the folder.
     * @return Returns a file handler to the folder.
     */
    private File overwriteRepoFolder(){
        File dir = new File(REPO_PATH);
        System.out.println(dir.getAbsolutePath());
        if (dir.exists()) {
            try {
                deleteFolder(dir);
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println("COLLECTION: Could not clean /temp/ folder. Stopping collection thread.");
                return null;
            }
        }
        dir.mkdir();

        return dir;
    }

    /**
     * Uses the JGit library to clone a repository into a temporary folder.
     * @param dir File handler to the folder where the repository will be saved.
     * @return An object representing the Git repository.
     */
    private Git cloneRepository(File dir) {
        try (Git repo = Git.cloneRepository()
                .setURI(REMOTE_URL)
                .setDirectory(dir)
                .call()) {
            System.out.println("COLLECTION: Repository \"" + REMOTE_URL + "\" cloned to " + repo.getRepository().getDirectory() + "...");
            return repo;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("COLLECTION: Could not clone repository. Stopping collection thread.");
            return null;
        }
    }

    /**
     * The run method is the entry point of a spawned thread. Further details are contained in inner comments.
     */
    @Override
    public void run() {
        // Clear the database before populating it.
        collector.DBConnection.dropDatabase(ProcessSteps.COLLECTION);

        File dir = overwriteRepoFolder();
        if (dir == null){ return; }
        Git repo = cloneRepository(dir);
        if (repo == null){ return; }

        try {
            // Iterate over the comments in the repository, generate DataObjects containing metadata and add them to a list.
            ArrayList<DataObject> commitsList = new ArrayList<>();
            Iterable<RevCommit> logs = repo.log().call();
            for (RevCommit rev : logs){
                CollectedCommit commit = new CollectedCommit(rev.getName(), rev.getFullMessage(), rev.getAuthorIdent());
                commitsList.add(commit);
            }
            // Add the objects generated in the previous loop to the database.
            collector.DBConnection.storeData(commitsList, ProcessSteps.COLLECTION);
            // Close handlers to resources and delete temporary folder.
            repo.getRepository().close();
            repo.close();
            deleteFolder(dir);
            System.out.println("COLLECTION: Data for repository collected and stored successfully.");
            return;
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("COLLECTION: Data for repository collected and stored, but the temp folder could not be cleaned.");
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("COLLECTION: Could not access repository. Stopping collection thread.");
            return;
        }
    }
}