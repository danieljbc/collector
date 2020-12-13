import collector.Collector;

/**
 * Main Class for an example of an instantiation of the COLLECTOR framework.
 * This instantiation automates a simple process. Where commits are retrieved from a remote repository,
 * and from the information contained in those commits, metrics are calculated that are then communicated via
 * the generation of a csv file.
 */
class Main {

    /**
     * Entry point of the program. Simply instantiates the hot spots of the framework by passing task schedulers to
     * the facade object of the framework.
     * Also starts the collection process.
     */
    public static void main(String[] args) {
        Collector collector = new Collector(new CommitInfoTaskScheduler(), new MetricCalculatorTaskScheduler(), new SummaryTaskScheduler(1600));
        collector.startCollector();
    }
}