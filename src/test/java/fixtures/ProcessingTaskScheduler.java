package fixtures;

import collector.TaskScheduler;

public class ProcessingTaskScheduler extends TaskScheduler {
    @Override
    public void run() {
        Status.processingFinished = true;
        return;
    }
}
