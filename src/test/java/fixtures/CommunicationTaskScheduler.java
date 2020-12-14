package fixtures;

import collector.TaskScheduler;

public class CommunicationTaskScheduler extends TaskScheduler {
    @Override
    public void run() {
        Status.communicationFinished = true;
        return;
    }
}
