public class ObjectD {

    private ObjectA[] objectAList;

    public ObjectD() {}

    public ObjectD(int length) {
        objectAList = new ObjectA[length];
    }

    public void setElement(int item, ObjectA objectA) {
        objectAList[item] = objectA;
    }
}
