import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ObjectCreator {

    private static List<Object> objectList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        System.out.println("-- Actions --");
        System.out.println("Select an option: \n" +
                "1) Create Simple Object\n" +
                "2) Create Reference Object\n" +
                "3) Create Primitive Array Object\n" +
                "4) Create Object Array Object\n" +
                "5) Create Collection Object\n" +
                "6) 'q' to quit");

        String selection;

        while((selection = scanner.nextLine()) != null) {
            Object obj = null;

            if (selection.equals("1")) {
                obj = createSimpleObject();
            } else if (selection.equals("2")) {
                obj = createReferenceObject();
            } else if (selection.equals("3")) {
                obj = createPrimitiveObject();
            } else if (selection.equals("4")) {
                obj = createObjectArrayObject();
            } else if (selection.equals("5")) {
                obj = createCollectionObject();
            } else if (selection.equals("quit")) {
                break;
            } else {
                System.err.println("Please give a valid input");
            }
            
            objectList.add(obj);
        }

        System.out.println("Time to cereal");
    }

    private static ObjectA createSimpleObject() {
        int userInputX;
        int userInputY;

        System.out.println("Creating simple object...");
        System.out.println("Please enter int value for x: ");
        userInputX = scanner.nextInt();

        System.out.println("Please enter int value for y: ");
        userInputY = scanner.nextInt();

        return new ObjectA(userInputX, userInputY);
    }

    private static ObjectB createReferenceObject() {
        System.out.println("Creating reference object...");
        ObjectA userObjectA = createSimpleObject();
        return new ObjectB(userObjectA);
    }

    private static ObjectC createPrimitiveObject() {
        int[] userList;
        int userInt;
        int size;

        System.out.println("Creating primitive object");
        System.out.println("Please enter size of array:");

        size = scanner.nextInt();
        userList = new int[size];

        for (int i = 0; i < userList.length; i++) {
            System.out.println("Enter int item for array");
            userInt = scanner.nextInt();
            userList[i] = userInt;
        }

        return new ObjectC(userList);
    }

    private static ObjectD createObjectArrayObject() {
        int arraySize;

        System.out.println("Creating array of objects object:");
        System.out.println("Please enter size of array:");

        arraySize = scanner.nextInt();
        ObjectD objD = new ObjectD(arraySize);

        for(int i = 0; i < arraySize; i++) {
            objD.setElement(i, createSimpleObject());
        }

        return objD;
    }

    private static ObjectE createCollectionObject() {
        ObjectE objE = new ObjectE();

        System.out.println("Creating collection of objects");

        String input = "";
        while (!input.equals("q")) {
            objE.addObject(createSimpleObject());
            scanner.nextLine();
            System.out.println("Press enter to add new object or 'q' to quit");
            input = scanner.nextLine();
        }

        return objE;
    }
}
