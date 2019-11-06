import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Deserializer {

    private HashMap<String, Object> ihm = new HashMap<>();

    public Object deserialize(Document document) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        List<Object> objects = new ArrayList<>();
        Element root = document.getRootElement();
        List<Element> elements = root.getChildren();

        for (int i = 0; i < elements.size(); i++) {
            Element node = elements.get(i);
            Object objFromDocument;

            String className = node.getAttribute("class").getValue();
            Class c = Class.forName(className);

            if (c.isArray()) {
                int length = Integer.parseInt(node.getAttribute("length").getValue());
                objFromDocument = Array.newInstance(c.getComponentType(), length);
            } else {
                objFromDocument = c.newInstance();
            }

            ihm.put(node.getAttribute("id").getValue(), objFromDocument);

            if (c.isArray()) {

                List<Element> fieldsList = node.getChildren();
                for (int k = 0; k < fieldsList.size(); k++) {

                    Element field = fieldsList.get(k);

                    if (field.getName().equals("value")) {
                        int v = Integer.parseInt(field.getText());
                        Array.set(objFromDocument, k, v);
                    } else if (field.getName().equals("reference")) {
                        Object obj = ihm.get(field.getText());
                        Array.set(objFromDocument, k, obj);
                    }
                }

            } else {

                // Read fields from xml and set
                List<Element> fieldsList = node.getChildren();
                for (Element field: fieldsList) {

                    Element value = field.getChildren().get(0);
                    Field correspondingField = c.getDeclaredField(field.getAttribute("name").getValue());
                    correspondingField.setAccessible(true);

                    if (value.getName().equals("value")) {
                        int v = Integer.parseInt(value.getText());
                        correspondingField.set(objFromDocument, v);

                    } else if (value.getName().equals("reference")) {
                        Object obj = ihm.get(value.getText());
                        correspondingField.set(objFromDocument, obj);
                    }
                }
            }

            objects.add(objFromDocument);
        }
        return objects.get(objects.size()-1);
    }
}
