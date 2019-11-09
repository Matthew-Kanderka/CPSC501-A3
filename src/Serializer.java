import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class Serializer {

    private IdentityHashMap<Object, Integer> ihm = new IdentityHashMap<>();
    private Integer id = 0;
    private Document document;

    public Document serialize(Object object) throws IllegalAccessException {

        document = new Document();
        document.setRootElement(new Element("serialized"));
        serObject(object);
        return document;
    }

    private void serObject(Object objectToSerialize) throws IllegalAccessException {

        Integer id = getId(objectToSerialize);

        Element objectElement = createObjectElement(objectToSerialize, id);

        if (objectToSerialize.getClass().isArray()) {
            objectElement = serializeArray(objectToSerialize);
        } else {

            Field[] fields = objectToSerialize.getClass().getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);

                Element fieldElement = createFieldElement(f);

                Object value = f.get(objectToSerialize);

                if (f.getType().isPrimitive()) {
                    Element element = createValueElement(value);
                    fieldElement.addContent(element);
                } else {
                    Element referenceElement = createReferenceElement(getId(value).toString());
                    fieldElement.addContent(referenceElement);
                    serObject(value);
                }
                objectElement.addContent(fieldElement);
            }
        }

        document.getRootElement().addContent(objectElement);
    }

    public Element serializeArray(Object arrayObject) throws IllegalAccessException {

        Element objectElement = createObjectArrayElement(arrayObject, getId(arrayObject));

        Class type = arrayObject.getClass().getComponentType();

        for (int i = 0; i < Array.getLength(arrayObject); i++) {

            if (Array.get(arrayObject, i) == null) {
                Element nullElement = createValueElement(null);
                objectElement.addContent(nullElement);
            } else if (type.isPrimitive()) {
                Element valueElement = createValueElement(Array.get(arrayObject, i));
                objectElement.addContent(valueElement);
            } else {
                Element referenceElement = createReferenceElement(getId(Array.get(arrayObject, i)).toString());
                objectElement.addContent(referenceElement);
                serObject(Array.get(arrayObject, i));
            }
        }

        return objectElement;
    }

    private Integer getId(Object obj) {
        int objectId = id;

        if (ihm.containsKey(obj)) {
            objectId = ihm.get(obj);
        } else {
            ihm.put(obj, objectId);
            id++;
        }
        return objectId;
    }

    private Element createObjectElement(Object obj, Integer id) {
        Element objectElement = new Element("object");
        objectElement.setAttribute(new Attribute("class", obj.getClass().getName()));
        objectElement.setAttribute(new Attribute("id", id.toString()));
        return objectElement;
    }

    private Element createObjectArrayElement(Object obj, Integer id) {
        Element objectElement = new Element("object");
        objectElement.setAttribute(new Attribute("class", obj.getClass().getName()));
        objectElement.setAttribute(new Attribute("id", id.toString()));
        objectElement.setAttribute(new Attribute("length", Integer.toString(Array.getLength(obj))));
        return objectElement;
    }

    private Element createFieldElement(Field field) {
        Element fieldElement = new Element("field");
        fieldElement.setAttribute(new Attribute("name", field.getName()));
        fieldElement.setAttribute(new Attribute("declaringClass", field.getDeclaringClass().getName()));
        return fieldElement;
    }

    private Element createValueElement(Object valueObj) {
        Element valueElement = new Element("value");
        if (valueObj == null) {
            valueElement.setText("null");
        } else {
            valueElement.setText(valueObj.toString());
        }
        return valueElement;
    }

    private Element createReferenceElement(String id) {
        Element referenceElement = new Element("reference");
        referenceElement.setText(id);
        return referenceElement;

    }
}
