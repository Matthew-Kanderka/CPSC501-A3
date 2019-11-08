import java.util.ArrayList;

public class ObjectE {

    private ArrayList<ObjectA> objectAArrayList;

    public ObjectE() {
        objectAArrayList = new ArrayList<>();
    }

    public void addObject(ObjectA objectA) {
        objectAArrayList.add(objectA);
    }

    public ArrayList<ObjectA> getObjectAArrayList() {
        return objectAArrayList;
    }
}
