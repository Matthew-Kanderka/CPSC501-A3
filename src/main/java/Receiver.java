import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.FileWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Receiver {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        System.out.println("Starting receiver server...");

        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("Server at: " + inetAddress.getHostAddress());

        System.out.print("Please enter desired port number: ");
        int portNumber;
        portNumber = scanner.nextInt();

        System.out.println("Creating server socket...");
        ServerSocket serverSocket = new ServerSocket(portNumber, 0, inetAddress);

        while (true) {

            Socket socket = serverSocket.accept();
            System.out.println("Received document from Sender");

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(socket.getInputStream());

            Deserializer deserializer = new Deserializer();
            Object object = deserializer.deserialize(document);

            Visualizer visualizer = new Visualizer();
            visualizer.visualize(object, true);

//            try {
//                new XMLOutputter(Format.getPrettyFormat()).output(document, new FileWriter("fileReceived.xml"));
//            } catch (Exception e) {
//            }

            }
    }
}
