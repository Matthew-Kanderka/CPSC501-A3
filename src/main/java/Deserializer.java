import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

public class Deserializer {

    private HashMap<String, Object> ihm = new HashMap<>();

    public Object deserialize(Document document) throws Exception {
        Element root = document.getRootElement();
        List<Element> elements = root.getChildren();

        initializeObjects(elements);
        setFieldValues(elements);
        return ihm.get("0");
    }

    private void initializeObjects(List<Element> elements) throws Exception{
        for (Element node : elements) {
            Object objFromDocument;

            String className = node.getAttribute("class").getValue();
            Class c = Class.forName(className);

            if (c.isArray()) {
                int length = Integer.parseInt(node.getAttribute("length").getValue());
                objFromDocument = Array.newInstance(c.getComponentType(), length);
            } else {
                Constructor constructor = c.getDeclaredConstructor(null);
                objFromDocument = constructor.newInstance(null);
            }

            ihm.put(node.getAttribute("id").getValue(), objFromDocument);
        }
    }

    private void setFieldValues(List<Element> list) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        for (Element element : list) {
            Object instance = ihm.get(element.getAttributeValue("id"));
            List<Element> fieldElements = element.getChildren();

            if (instance.getClass().isArray()) {

                Class componentType = instance.getClass().getComponentType();
                for (int k = 0; k < fieldElements.size(); k++) {
                    Element field = fieldElements.get(k);

                    if (field.getText().equals("null")) {
                        Array.set(instance, k, null);
                    } else if (field.getName().equals("value")) {
                        Array.set(instance, k, getValue(componentType, fieldElements.get(k)));
                    } else if (field.getName().equals("reference")) {
                        Object obj = ihm.get(field.getText());
                        Array.set(instance, k, obj);
                    }
                }

            } else {
                // Read fields from xml and set
                for (Element field : fieldElements) {
                    String className = field.getAttributeValue("declaringClass");
                    Class cls = Class.forName(className);

                    Field correspondingField = cls.getDeclaredField(field.getAttribute("name").getValue());
                    if (Modifier.isFinal(correspondingField.getModifiers())) {
                        continue;
                    }

                    correspondingField.setAccessible(true);
                    Element value = field.getChildren().get(0);

                    if (value.getName().equals("value")) {
                        correspondingField.set(instance, getValue(correspondingField.getType(), value));
                    } else if (value.getName().equals("reference")) {
                        Object obj = ihm.get(value.getText());
                        correspondingField.set(instance, obj);
                    }
                }
            }
        }
}


    private Object getValue(Class fieldType, Element ele) {
        if (fieldType.equals(boolean.class)) {
            if (ele.getText().equals("true")) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else if (fieldType.equals(byte.class)) {
            return Byte.valueOf(ele.getText());
        } else if (fieldType.equals(short.class)) {
            return Short.valueOf(ele.getText());
        } else if (fieldType.equals(int.class)) {
            return Integer.valueOf(ele.getText());
        } else {
            return ele.getText();
        }
    }
}
