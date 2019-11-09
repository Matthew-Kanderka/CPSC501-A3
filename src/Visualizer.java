import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Visualizer {

    public void visualize(Object object, boolean recursive) {
        Class c = object.getClass();
        inspectClass(c, object, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {

        formattedPrint("Class Name: " + c.getName(), depth);

        getConstructorInfo(c, obj, depth);
        getFieldInfo(c, obj, recursive, depth);
    }

    public void getSuperClassInfo(Class c, Object obj, boolean recursive, int depth) {

        if (c.equals(Object.class)) {
            return;
        }

        Class superClass = c.getSuperclass();

        if (superClass != null) {
            formattedPrint("Super Class Name: " + superClass.getName(), depth);
            inspectClass(superClass, obj, recursive, depth + 1);
        } else {
            formattedPrint("No super class", depth);
        }
    }

    public void getConstructorInfo(Class c, Object obj, int depth) {

        Constructor[] constructors = c.getDeclaredConstructors();

        for (Constructor constructor: constructors) {
            formattedPrint("Constructor Name: " + constructor.getName(), depth);

            Class[] parameterTypes = constructor.getParameterTypes();
            for (Class pt: parameterTypes) {
                formattedPrint("Parameter Types: " + pt.getName(), depth+1);
            }

            formattedPrint("Modifiers: " + Modifier.toString(constructor.getModifiers()), depth+1);
        }
    }

    public void getFieldInfo(Class c, Object obj, boolean recursive, int depth) {

        Field[] fields = c.getDeclaredFields();

        for (Field f: fields) {
            formattedPrint("Field Name: " + f.getName(), depth);
            formattedPrint("Type: " + f.getType().getName(), depth+1);
            formattedPrint("Modifiers: " + Modifier.toString(f.getModifiers()), depth+1);

            f.setAccessible(true);

            Object value = null;
            try {
                value = f.get(obj);
            } catch (IllegalAccessException e) {
                System.out.println("Cannot Access Field");
            }

            if (value == null) {
                formattedPrint("Value: " + null, depth + 1);
            } else if (f.getType().isArray()) {
                inspectArray(f.getType(), value, recursive, depth);
            } else if (f.getType().isPrimitive()) {
                formattedPrint("Value: " + value, depth+1);
            } else {
                referenceValueCheck(recursive, depth, value);
            }
        }
    }

    public void inspectArray(Class c, Object obj, boolean recursive, int depth) {

        Class componentType = c.getComponentType();
        formattedPrint("Component Type: " + componentType.getName(), depth);

        int arrayLength = Array.getLength(obj);
        formattedPrint("Array Length: " + arrayLength, depth);

        for (int i = 0; i < arrayLength; i++) {
            Object object = Array.get(obj, i);

            if (object == null) {
                formattedPrintNoNewLine("null", depth);
            } else if (componentType.isPrimitive()) {
                formattedPrintNoNewLine(object.getClass().getName(), depth);
            } else if (componentType.isArray()) {
                inspectArray(object.getClass(), object, recursive, 1);
            } else {
                referenceValueCheck(recursive, depth, object);
            }
        }
        System.out.println();
    }

    private void referenceValueCheck(boolean recursive, int depth, Object object) {
        if (recursive) {
            inspectClass(object.getClass(), object, recursive, depth + 1);
        } else {
            formattedPrint("Reference Value: " + object.getClass().getName() + "@" + object.hashCode(), depth);
        }
    }

    public void formattedPrint(String message, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("\t");
        }
        System.out.println(message);
    }

    public void formattedPrintNoNewLine(String message, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("\t");
        }
        System.out.print(message);
    }
}
