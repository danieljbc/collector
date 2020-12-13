import collector.DataObject;

public class ProcessedCommit implements DataObject {

    public String hash;
    public long messageLength;
    public long numberPreviousCommits;

    ProcessedCommit(String hash, long messageLength, long numberPreviousCommits) {
        this.hash = hash;
        this.messageLength = messageLength;
        this.numberPreviousCommits = numberPreviousCommits;
    }
}