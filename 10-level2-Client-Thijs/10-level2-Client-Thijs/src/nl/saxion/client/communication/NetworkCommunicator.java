package nl.saxion.client.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *This {@link NetworkCommunicator} class handles all the network functionalities.
 * {@link #sendRequest(String)} Sends a request to the server
 * {@link #readResponse()} Received the response for the server
 * {@link #close()} Closes the connection for the socket, input and output stream
 */
public class NetworkCommunicator {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     *This method creates a new socket connection and a input and output stream
     * @param serverAddress The IP address of the new socket connection
     * @param serverPort The port of the new socket connection
     * @throws IOException Throws error when input or output is interrupted
     */
    public NetworkCommunicator(String serverAddress, int serverPort) throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * This method sends a request to the server
     * @param request The request that gets sent to the server
     */
    public void sendRequest(String request) {
        out.println(request);
    }

    /**
     * This method receives a response by the server
     * @return The response that gets sent by the server
     * @throws IOException Throws error when input or output is interrupted
     */
    public String readResponse() throws IOException {
        return in.readLine(); // Of gebruik een andere manier om te bepalen hoe je wilt lezen
    }

    /**
     * This method closes the socket, input and output stream when invoked
     * @throws IOException Throws error when input or output is interrupted
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }


    // Mogelijk meer methoden voor specifieke soorten requests
}
