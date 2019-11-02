import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.List;

public class Serializer {

    private IdentityHashMap<Object, Integer> ihm = new IdentityHashMap<>();
    private Integer id = 0;
    private Document document;

    public Document serialize(Object object) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        document = new Document();
        document.setRootElement(new Element("serialized"));

        Class cls = object.getClass();
        Class[] noparams = {};
        Method method = cls.getDeclaredMethod("size", noparams);
        Object size = method.invoke(object, (Object[]) null);

        for (int i = 0; i < Integer.parseInt(size.toString()); i++) {
            Method get = List.class.getDeclaredMethod("get", int.class);
            get.invoke(object, i);
            Object objectToSerialize = get.invoke(object, i);
            serObject(objectToSerialize);
        }

        return document;
    }

    private void serObject(Object objectToSerialize) throws IllegalAccessException {

        Integer id = getId(objectToSerialize);

        Element objectElement = new Element("object");
        objectElement.setAttribute(new Attribute("class", objectToSerialize.getClass().getName()));
        objectElement.setAttribute(new Attribute("id", id.toString()));

        Field[] fields = objectToSerialize.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            String fieldName = f.getName();
            String fieldDeclaringClass = f.getDeclaringClass().getName();

            Element fieldElement = new Element("field");
            fieldElement.setAttribute(new Attribute("name",fieldName));
            fieldElement.setAttribute(new Attribute("declaringClass", fieldDeclaringClass));

            Object value = f.get(objectToSerialize);

            if (f.getType().isPrimitive()) {
                Element valueElement = new Element("value");
                valueElement.setText(value.toString());
                fieldElement.addContent(valueElement);
            } else {
                Element referenceElement = new Element("reference");
                referenceElement.setText(getId(value).toString());
                fieldElement.addContent(referenceElement);
                serObject(value);
            }
            objectElement.addContent(fieldElement);
        }

        document.getRootElement().addContent(objectElement);
    }

    public Integer getId(Object obj) {
        int objectId = id;

        if (ihm.containsKey(obj)) {
            objectId = ihm.get(obj);
        } else {
            ihm.put(obj, objectId);
            id++;
        }
        return objectId;
    }
}
