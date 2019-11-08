import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ObjectCreatorTest {

    private final InputStream systemIn = System.in;

    private ByteArrayInputStream testIn;

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
    }

    @Test
    public void testCreateSimpleObject() {
        String x = "1\n2\n";
        provideInput(x);

        ObjectA ojbA = new ObjectCreator().createSimpleObject();
        assert ojbA.getX() == 1;
        assert ojbA.getY() == 2;
    }

    @Test
    public void testCreateReferenceObject() {
        String x = "1\n2\n";
        provideInput(x);

        ObjectB objB = new ObjectCreator().createReferenceObject();
        assert objB != null;
        assert objB.getObjectA().getX() == 1;
        assert objB.getObjectA().getY() == 2;
    }

    @Test
    public void testCreatePrimitiveObject() {
        String x = "2\n1\n2";
        provideInput(x);

        ObjectC objC = new ObjectCreator().createPrimitiveObject();
        assert objC != null;
        assert objC.getNumberList()[0] == 1;
        assert objC.getNumberList()[1] == 2;
    }

    @Test
    public void testCreateObjectArrayObject() {
        String x = "2\n1\n2\n12\n32";
        provideInput(x);

        ObjectD objD = new ObjectCreator().createObjectArrayObject();
        assert objD != null;
        assert objD.getObjectAList().length == 2;
        assert objD.getObjectAList()[0].getX() == 1;
        assert objD.getObjectAList()[0].getY() == 2;
        assert objD.getObjectAList()[1].getX() == 12;
        assert objD.getObjectAList()[1].getY() == 32;
    }

    @Test
    public void testCreateCollectionObject() {
        String x = "1\n2\n\n\n12\n32\nq";
        provideInput(x);

        ObjectE objE = new ObjectCreator().createCollectionObject();
        assert objE != null;
        assert objE.getObjectAArrayList().size() == 2;
        assert objE.getObjectAArrayList().get(0).getX() == 1;
        assert objE.getObjectAArrayList().get(0).getY() == 2;
        assert objE.getObjectAArrayList().get(1).getX() == 12;
        assert objE.getObjectAArrayList().get(1).getY() == 32;
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
}
