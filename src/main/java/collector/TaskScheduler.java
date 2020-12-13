package collector;

import java.util.concurrent.ExecutorService;

/**
 * Class that will be executed by its respective Manager class, depending on from which step of the process it is.
 * Can have multiple roles, which are differentiated by the constructor used:
 *      Can be Updatable, if it receives an updateInterval. This means that it will repeat on that interval
 *              to search for updates.
 *      Can perform the collection by itself, in the case where it doesn't receive an ExecutorService.
 *              This should be used by simples and/or faster tasks or ones that need to be synchronous.
 *      Can spawn workers using a bag of tasks model, when it receives an ExecutorService.
 *              In this case, its job is to identify tasks and deploy workers that will execute them.
 * All of the actions for the scheduler should be specified in the run() method, as this class implements
 *      the Runnable interface (command pattern).
 * This is the main hotspot for this framework, and this class can be reused for any of the steps of the process.
 *      (Collection, Processing and Communication).
 */
public abstract class TaskScheduler implements Runnable {
    private ExecutorService executor;
    private boolean updatable = false;
    private long updateInterval = -1;

    /**
     * Constructor for synchronous tasks that are not updatable.
     */
    public TaskScheduler() { }

    /**
     * Constructor for synchronous tasks that are updatable.
     */
    public TaskScheduler(long updateInterval) {
        this.updatable = true;
        this.updateInterval = updateInterval;
    }

    /**
     * Constructor for asynchronous tasks that are not updatable.
     */
    public TaskScheduler(ExecutorService executor){
        this.executor = executor;
    }

    /**
     * Constructor for asynchronous tasks that are updatable.
     */
    public TaskScheduler(ExecutorService executor, long updateInterval){
        this.executor = executor;
        this.updatable = true;
        this.updateInterval = updateInterval;
    }

    /**
     * @return The executorService being used by this task scheduler.
     */
    protected ExecutorService getExecutor() {
        return executor;
    }

    /**
     * @return The interval in which this task scheduler repeats itself. Returns -1 if tasks is not updatable.
     */
    protected long getUpdateInterval(){
        return this.updateInterval;
    }

    /**
     *
     * @return Returns true if scheduler needs to be executed on an interval. False if not.
     */
    protected boolean isUpdatable(){
        return this.updatable;
    };
}
