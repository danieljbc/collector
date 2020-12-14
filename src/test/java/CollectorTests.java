import collector.Collector;
import fixtures.CollectionTaskScheduler;
import fixtures.EmptyTaskScheduler;
import org.junit.Test;
import fixtures.*;

public class CollectorTests {

    @Test(timeout = 5000)
    public void onlyCollection(){
        Status.clear();
        System.out.println("Initializing collector and starting schedulers.");
        Collector collector = new Collector(new CollectionTaskScheduler(), new EmptyTaskScheduler(), new EmptyTaskScheduler());
        collector.startCollector();
        while (!Status.collectionFinished){
            // DO NOTHING, WILL TIMEOUT IF IT DOESN'T FINISH SUCESSFULLY.
        }
        System.out.println("Collector finished successfully.");
    }
    @Test(timeout = 5000)
    public void onlyProcessing(){
        Status.clear();
        System.out.println("Initializing collector and starting schedulers.");
        Collector collector = new Collector(new EmptyTaskScheduler(), new ProcessingTaskScheduler(), new EmptyTaskScheduler());
        collector.startCollector();
        while (!Status.processingFinished){
            // DO NOTHING, WILL TIMEOUT IF IT DOESN'T FINISH SUCESSFULLY.
        }
        System.out.println("Collector finished successfully.");
    }
    @Test(timeout = 5000)
    public void onlyCommunication(){
        Status.clear();
        System.out.println("Initializing collector and starting schedulers.");
        Collector collector = new Collector(new EmptyTaskScheduler(), new EmptyTaskScheduler(), new CommunicationTaskScheduler());
        collector.startCollector();
        while (!Status.communicationFinished){
            // DO NOTHING, WILL TIMEOUT IF IT DOESN'T FINISH SUCESSFULLY.
        }
        System.out.println("Collector finished successfully.");
    }
    @Test(timeout = 5000)
    public void collectionAndProcessing(){
        Status.clear();
        System.out.println("Initializing collector and starting schedulers.");
        Collector collector = new Collector(new CollectionTaskScheduler(), new ProcessingTaskScheduler(), new EmptyTaskScheduler());
        collector.startCollector();
        while (!Status.collectionFinished || !Status.processingFinished){
            // DO NOTHING, WILL TIMEOUT IF IT DOESN'T FINISH SUCESSFULLY.
        }
        System.out.println("Collector finished successfully.");
    }
    @Test(timeout = 5000)
    public void processingAndCommunication(){
        Status.clear();
        System.out.println("Initializing collector and starting schedulers.");
        Collector collector = new Collector(new EmptyTaskScheduler(), new ProcessingTaskScheduler(), new CommunicationTaskScheduler());
        collector.startCollector();
        while (!Status.communicationFinished || !Status.processingFinished){
            // DO NOTHING, WILL TIMEOUT IF IT DOESN'T FINISH SUCESSFULLY.
        }
        System.out.println("Collector finished successfully.");
    }
    @Test(timeout = 5000)
    public void collectionAndCommunication(){
        Status.clear();
        System.out.println("Initializing collector and starting schedulers.");
        Collector collector = new Collector(new CollectionTaskScheduler(), new EmptyTaskScheduler(), new CommunicationTaskScheduler());
        collector.startCollector();
        while (!Status.collectionFinished || !Status.communicationFinished){
            // DO NOTHING, WILL TIMEOUT IF IT DOESN'T FINISH SUCESSFULLY.
        }
        System.out.println("Collector finished successfully.");
    }
    @Test(timeout = 5000)
    public void allSteps(){
        Status.clear();
        System.out.println("Initializing collector and starting schedulers.");
        Collector collector = new Collector(new CollectionTaskScheduler(), new ProcessingTaskScheduler(), new CommunicationTaskScheduler());
        collector.startCollector();
        while (!Status.collectionFinished || !Status.processingFinished || !Status.communicationFinished){
            // DO NOTHING, WILL TIMEOUT IF IT DOESN'T FINISH SUCESSFULLY.
        }
        System.out.println("Collector finished successfully.");
    }
}
