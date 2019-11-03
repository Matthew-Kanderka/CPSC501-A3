import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class Deserializer {

    private IdentityHashMap<Object, String> ihm = new IdentityHashMap<>();

    public Object deserialize(Document document) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<Object> objects = new ArrayList<>();
        Element root = document.getRootElement();
        List<Element> elements = root.getChildren();

        for (int i = 0; i < elements.size(); i++) {
            Element node = elements.get(i);
            Object objFromDocument = null;

            String className = node.getAttribute("class").getValue();
            Class c = Class.forName(className);

            if (c.isArray()) {

            } else {
                objFromDocument = c.newInstance();

                // Read fields from xml and set

            }

//            String objectId = node.getAttribute("id").getValue();
//            ihm.put(objFromDocument, objectId);

            objects.add(objFromDocument);
        }
        return objects;
    }
}
