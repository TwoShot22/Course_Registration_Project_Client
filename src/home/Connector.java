package home;

import home.frameworks.Invoker;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class Connector {
    private static Socket socket = null;
    private static InputStream inputStream = null;
    private static OutputStream outputStream = null;

    public static void connect() throws IOException {
        socket = new Socket("localhost", 10000);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public static Object invoke(Invoker invoker) throws InvocationTargetException {
        try {
            ObjectOutputStream output = new ObjectOutputStream(outputStream);
            output.writeObject(invoker);
            output.flush();
            ObjectInputStream input = new ObjectInputStream(inputStream);
            Object object = input.readObject();
            if (object instanceof InvocationTargetException) throw ((InvocationTargetException) object);
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Please Connector.initialize()");
        }
        return null;
    }

    public static void disconnect(){
        try {
            ObjectOutputStream output = new ObjectOutputStream(outputStream);
            output.writeObject("exit");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
