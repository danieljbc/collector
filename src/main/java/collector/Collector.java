package collector;

/**
 * Facade serving as an interface to control the inner parts of the framework.
 * Internally, it control the managers that represent each step of the process being automated.
 */
public class Collector {

    InputManager inputManager;
    ProcessingManager processingManager;
    CommunicatingManager communicatingManager;

    /**
     * Constructor, that uses dependency injection to receive the task schedulers (one for the framework
     * @param inputScheduler Scheduler that implements the Collection step.
     * @param processingScheduler Scheduler that implements the Processing step.
     * @param communicationScheduler Scheduler that implements the Communication step.
     */
    public Collector(TaskScheduler inputScheduler, TaskScheduler processingScheduler, TaskScheduler communicationScheduler){
        this.inputManager = new InputManager(inputScheduler);
        this.processingManager = new ProcessingManager(processingScheduler);
        this.communicatingManager = new CommunicatingManager(communicationScheduler);
    }

    /**
     * Starts the managers for each of the three steps.
     */
    public void startCollector() {
        inputManager.start();
        processingManager.start();
        communicatingManager.start();
    }

    /**
     * Stops the managers for each of the three steps, included all threads spawned by them.
     */
    public void stopCollector() {
        inputManager.stop();
        processingManager.stop();
        communicatingManager.stop();
    }

    /**
     * Generates a JSON file containing a snapshot of the data that is currently on the database, for a specific step.
     * This method only exposes a method in DBConnection to this Facade.
     * @param step The step that should be exported.
     */
    public void exportSnapshot(ProcessSteps step) {
        DBConnection.exportData(step);
    }
}
