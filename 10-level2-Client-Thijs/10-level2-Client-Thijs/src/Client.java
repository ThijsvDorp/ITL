import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String CLIENT_ADDRESS = "127.0.0.1";
    private static final int CLIENT_PORT = 1337;
        Socket socket = new Socket(CLIENT_ADDRESS, CLIENT_PORT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    public Client() throws IOException {
    }
}