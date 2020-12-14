package fixtures;

import collector.DataObject;

public class DummyObject implements DataObject {
    public int value;

    public DummyObject(int value){
        this.value = value;
    }
}
