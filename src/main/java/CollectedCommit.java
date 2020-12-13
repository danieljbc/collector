import collector.DataObject;
import org.eclipse.jgit.lib.PersonIdent;

public class CollectedCommit implements DataObject {

    public String message;
    public PersonIdent author;

    CollectedCommit(String message, PersonIdent author){
        this.message = message;
        this.author = author;
    }
}
