import java.util.Scanner;

public class ObjectCreator {

    private Scanner scanner = new Scanner(System.in);

    public Object create(String selection) {

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
        }

        return obj;
    }

    public void printMenu() {
        System.out.println("-- Actions --");
        System.out.println("Select an option: \n" +
                "1) Create Simple Object\n" +
                "2) Create Reference Object\n" +
                "3) Create Primitive Array Object\n" +
                "4) Create Object Array Object\n" +
                "5) Create Collection Object\n" +
                "--- 'quit' to stop");
    }

    public ObjectA createSimpleObject() {
        int userInputX;
        int userInputY;

        System.out.println("Creating simple object...");
        System.out.println("Please enter int value for x: ");
        userInputX = scanner.nextInt();

        System.out.println("Please enter int value for y: ");
        userInputY = scanner.nextInt();

        return new ObjectA(userInputX, userInputY);
    }

    public ObjectB createReferenceObject() {
        System.out.println("Creating reference object...");
        ObjectA userObjectA = createSimpleObject();
        return new ObjectB(userObjectA);
    }

    public ObjectC createPrimitiveObject() {
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

    public ObjectD createObjectArrayObject() {
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

    public ObjectE createCollectionObject() {
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
