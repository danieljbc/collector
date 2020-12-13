import collector.DataObject;
import collector.ProcessSteps;
import collector.TaskScheduler;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CommitInfoTaskScheduler extends TaskScheduler {

    private static final String REMOTE_URL = "https://github.com/opus-research/organic.git";
    private static final String REPO_PATH = "temp/";

    static void deleteFolder(File f) throws IOException{
        if (f.isDirectory())
            for (File c : f.listFiles())
                deleteFolder(c);
        f.delete();
    }

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

    @Override
    public void run() {
        collector.DBConnection.dropDatabase(ProcessSteps.COLLECTION);

        File dir = overwriteRepoFolder();
        if (dir == null){ return; }
        Git repo = cloneRepository(dir);
        if (repo == null){ return; }

        try {
            ArrayList<DataObject> commitsList = new ArrayList<>();
            Iterable<RevCommit> logs = repo.log().call();
            for (RevCommit rev : logs){
                CollectedCommit commit = new CollectedCommit(rev.getFullMessage(), rev.getAuthorIdent());
                commitsList.add(commit);
            }
            collector.DBConnection.storeData(commitsList, ProcessSteps.COLLECTION);
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