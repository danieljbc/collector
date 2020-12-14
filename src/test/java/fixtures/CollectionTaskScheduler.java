package fixtures;

import collector.TaskScheduler;

public class CollectionTaskScheduler extends TaskScheduler {
    @Override
    public void run() {
        Status.collectionFinished = true;
        return;
    }
}
