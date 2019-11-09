import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Scanner;

public class Sender {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {

        ObjectCreator objectCreator = new ObjectCreator();
        Serializer serializer = new Serializer();

        System.out.println("Please enter desired host: ");
        String host = scanner.nextLine();
        System.out.println("Please enter desired port: ");
        int port = scanner.nextInt();
        scanner.nextLine();

        objectCreator.printMenu();

        while (true) {
            String userChoice = scanner.nextLine();
            if (userChoice.equalsIgnoreCase("quit")) {
                break;
            } else {

                Object objectToSerialize = objectCreator.create(userChoice);
                Document document = serializer.serialize(objectToSerialize);

                try {
                    new XMLOutputter(Format.getPrettyFormat()).output(document, new FileWriter("fileSent.xml"));
                } catch (Exception e) {

                }

                System.out.println("Creating Socket...");
                Socket socket = new Socket(host, port);
                DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

                System.out.println("Sending document to Receiver");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                new XMLOutputter().output(document, bos);
                dOut.write(bos.toByteArray());
                dOut.close();
                socket.close();
            }
        }
    }
}
