import collector.DataObject;
import org.eclipse.jgit.lib.PersonIdent;

/**
 * Data Class representing the commit data that is collected in the collection step.
 */
public class CollectedCommit implements DataObject {

    public String hash;
    public String message;
    public PersonIdent author;

    CollectedCommit(String hash, String message, PersonIdent author){
        this.hash = hash;
        this.message = message;
        this.author = author;
    }
}