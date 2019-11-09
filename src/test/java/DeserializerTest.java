import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class DeserializerTest {

    @Test
    public void testDeserialize_ObjectA() throws Exception {
        Document objectADocument = readFile("src/test/resources/objectA.xml");

        Deserializer deserializer = new Deserializer();
        Object objA = deserializer.deserialize(objectADocument);

        assert objA.getClass().getName().equals("ObjectA");

        Field field1 = objA.getClass().getDeclaredFields()[0];
        field1.setAccessible(true);
        assert field1.get(objA).equals(32);

        Field field2 = objA.getClass().getDeclaredFields()[1];
        field2.setAccessible(true);
        assert field2.get(objA).equals(1);
    }

    @Test
    public void testDeserialize_ObjectB() throws Exception {
        Document objectBDocument = readFile("src/test/resources/objectB.xml");

        Deserializer deserializer = new Deserializer();
        Object objB = deserializer.deserialize(objectBDocument);

        assert objB.getClass().getName().equals("ObjectB");

        Field field = objB.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        Object innerObj = field.get(objB);
        assert innerObj.getClass().getName().equals("ObjectA");

        Field innerObjectField1 = innerObj.getClass().getDeclaredFields()[0];
        innerObjectField1.setAccessible(true);
        assert innerObjectField1.get(innerObj).equals(4);

        Field innerObjectField2 = innerObj.getClass().getDeclaredFields()[1];
        innerObjectField2.setAccessible(true);
        assert innerObjectField2.get(innerObj).equals(3);
    }

    @Test
    public void testDeserialize_ObjectC() throws Exception {
        Document objectCDocument = readFile("src/test/resources/objectC.xml");

        Deserializer deserializer = new Deserializer();
        Object objC = deserializer.deserialize(objectCDocument);

        assert objC.getClass().getName().equals("ObjectC");
        Field field = objC.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        Object list = field.get(objC);
        assert Array.get(list,0).equals(1);
        assert Array.get(list,1).equals(2);
        assert Array.get(list,2).equals(3);
    }

    private Document readFile(String path) throws JDOMException, IOException {
        File xmlFile = new File(path);
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(xmlFile);
    }
}
