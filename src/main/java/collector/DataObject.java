package collector;

/**
 * An empty interface (Marker interface pattern)
 * that marks which objects can be serialized with
 * gson to be inserted in the database.
 * This is one of the hot spots for the framework,
 * as the user must provide his own DataObjects.
 * (see DataConverter javadoc for more info)
 */
public interface DataObject {
}
