package fixtures;

/**
 * Class that used to monitor the state of the threads in the dummy collector;
 */
public class Status {
    static public boolean collectionFinished = false;
    static public boolean processingFinished = false;
    static public boolean communicationFinished = false;

    public static void clear(){
        collectionFinished = false;
        processingFinished = false;
        communicationFinished = false;
    }
}
