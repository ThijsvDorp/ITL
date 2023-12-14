package nl.saxion.client.core;

import nl.saxion.client.commands.CommandManager;
import nl.saxion.client.communication.NetworkCommunicator;
import nl.saxion.client.network.ServerMessageProcessor;
import nl.saxion.client.session.SessionManager;
import nl.saxion.client.userinterface.UserInterfaceManager;
import nl.saxion.client.userinterface.input.InputInterface;
import nl.saxion.client.userinterface.output.OutputInterface;

import java.io.IOException;


public class ChatClient {
    private ClientMediator mediator;
    private final NetworkCommunicator communicator;
    private final SessionManager sessionManager;
    private final CommandManager commandManager;
    private final ServerMessageProcessor serverMessageProcessor;
    private final UserInterfaceManager uiManager;

    /**
     * This constructor initiates {@link ClientMediator},{@link NetworkCommunicator},
     * {@link SessionManager},{@link CommandManager},{@link ServerMessageProcessor},{@link UserInterfaceManager}
     * @param serverAddress The server address for the client
     * @param serverPort The server port for the client
     * @param inputHandler Handles the input of the client
     * @param outputHandler Handles the output to the client
     * @throws IOException Throws an exception when the input or output is interrupted
     */
    public ChatClient(String serverAddress, int serverPort, InputInterface inputHandler, OutputInterface outputHandler) throws IOException {
        this.communicator = new NetworkCommunicator(serverAddress, serverPort);
        this.sessionManager = new SessionManager();
        this.serverMessageProcessor = new ServerMessageProcessor();
        this.uiManager = new UserInterfaceManager(inputHandler, outputHandler);
        this.commandManager = new CommandManager();

        // Eerst alle componenten initialiseren
        this.mediator = new ClientMediator(uiManager, serverMessageProcessor, communicator, commandManager, sessionManager);

        this.sessionManager.setMediator(mediator);
        this.serverMessageProcessor.setMediator(mediator);
        this.commandManager.setMediator(mediator);
        startListeningForServerMessages();
    }

    /**
     * This method starts a thread that listens to incoming messages from the server.
     */
    private void startListeningForServerMessages() {
        new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String message = communicator.readResponse();
                    mediator.processServerMessage(message);
                }
            } catch (IOException e) {
                System.out.println("Error: " + e); // Behandel uitzonderingen
            }
        }).start();
    }

    public void disconnect() {
        try {
            communicator.close();
            sessionManager.logout();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
