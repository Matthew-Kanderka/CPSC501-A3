import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Sender {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {

        ObjectCreator objectCreator = new ObjectCreator();
        Serializer serializer = new Serializer();

        List<Object> objectsToSerialize = objectCreator.create();
        Document document = serializer.serialize(objectsToSerialize);

        try {
            new XMLOutputter(Format.getPrettyFormat()).output(document, new FileWriter("fileSent.xml"));
        } catch (Exception e) {

        }

        System.out.println("Please enter desired host: ");
        String host = scanner.nextLine();
        System.out.println("Please enter desired port: ");
        int port = scanner.nextInt();

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
