import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class SerializerTest {

    @Test
    public void testSerialize_ObjectA() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Serializer serializer = new Serializer();
        Document document = serializer.serialize(new ObjectA(4,6));

        assert document != null;
        assert document.getRootElement().getName().equals("serialized");

        Element child = document.getRootElement().getChildren().get(0);
        assert child.getName().equals("object");
        assert child.getAttribute("class").getValue().equals("ObjectA");
        assert child.getAttribute("id").getValue().equals("0");

        assert child.getChildren().get(0).getChildren().get(0).getText().equals("4");
        assert child.getChildren().get(1).getChildren().get(0).getText().equals("6");
    }

    @Test
    public void testSerialize_ObjectB() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Serializer serializer = new Serializer();
        ObjectB objectB = new ObjectB(new ObjectA(4,6));
        Document document = serializer.serialize(objectB);

        assert document != null;
        assert document.getRootElement().getName().equals("serialized");

        Element child = document.getRootElement().getChildren().get(0);
        assert child.getName().equals("object");
        assert child.getAttribute("class").getValue().equals("ObjectA");
        assert child.getAttribute("id").getValue().equals("1");

        assert child.getChildren().get(0).getChildren().get(0).getName().equals("value");
        assert child.getChildren().get(0).getChildren().get(0).getText().equals("4");
        assert child.getChildren().get(1).getChildren().get(0).getText().equals("6");

        Element child2 = document.getRootElement().getChildren().get(1);
        assert child2.getName().equals("object");
        assert child2.getAttribute("class").getValue().equals("ObjectB");
        assert child2.getAttribute("id").getValue().equals("0");

        assert child2.getChildren().get(0).getChildren().get(0).getName().equals("reference");
        assert child2.getChildren().get(0).getChildren().get(0).getText().equals("1");
    }

    @Test
    public void testSerialize_ObjectC() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Serializer serializer = new Serializer();
        ObjectC objectC = new ObjectC(new int[]{1,2,3});
        Document document = serializer.serialize(objectC);

        assert document != null;
        assert document.getRootElement().getName().equals("serialized");

        Element child = document.getRootElement().getChildren().get(0);
        assert child.getName().equals("object");
        assert child.getAttribute("class").getValue().equals("[I");
        assert child.getAttribute("id").getValue().equals("1");

        assert child.getChildren().get(0).getName().equals("value");
        assert child.getChildren().get(0).getText().equals("1");
        assert child.getChildren().get(1).getText().equals("2");
        assert child.getChildren().get(2).getText().equals("3");

        Element child2 = document.getRootElement().getChildren().get(1);
        assert child2.getName().equals("object");
        assert child2.getAttribute("class").getValue().equals("ObjectC");
        assert child2.getAttribute("id").getValue().equals("0");
    }
}
