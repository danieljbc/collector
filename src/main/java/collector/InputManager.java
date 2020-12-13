package collector;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Class that manages the invocation of the thread that will run the taskScheduler for the Collection step.
 * It uses a paradigm known as a bag of tasks, and uses constructs provided by the java language that are suited
 * for this sort of algorithm.
 * Currently, this class is mostly identical to ProcessingManager and CommunicationManager. They were kept separate as
 * (1) this limits the number of steps in a process, as this framework was created to support a very specific
 * type of process; and (2) allows for future customization in each step of the process, in the case that
 * becomes necessary.
 */
class InputManager {

    private TaskScheduler taskScheduler;
    // While futures are generally used to receive values from threads. In this case we are retrieving it
    // to have future control over when it stops.
    private ScheduledFuture<?> schedulerFuture;

    InputManager(TaskScheduler taskScheduler){
        this.taskScheduler = taskScheduler;
    }

    /**
     *  Creates a new Executor that is capable of scheduling threads to perform tasks that implement the
     *  Runnable interface (Command pattern). Using that executor, it schedules the execution of the TaskScheduler.
     */
    protected void start(){
        // In this case, we only need a single thread as this executor will only be used to execute the taskScheduler.
        ScheduledExecutorService taskSchedulerExecutor = Executors.newSingleThreadScheduledExecutor();
        if (taskScheduler.isUpdatable()) {
            // Spawns a thread that will execute a tasks immediately, and will repeat that task in a certain interval, in seconds.
            schedulerFuture = taskSchedulerExecutor.scheduleAtFixedRate(this.taskScheduler, 0, taskScheduler.getUpdateInterval(), TimeUnit.SECONDS);
        }
        else {
            // Spawns a thread that will execute a tasks immediately, only once.
            schedulerFuture = taskSchedulerExecutor.schedule(this.taskScheduler, 0, TimeUnit.SECONDS);
        }
    }

    /**
     * Stops the taskScheduler and all worker threads spawned by it.
     */
    protected void stop(){
        schedulerFuture.cancel(true);
        taskScheduler.getExecutor().shutdownNow();
    }
}
