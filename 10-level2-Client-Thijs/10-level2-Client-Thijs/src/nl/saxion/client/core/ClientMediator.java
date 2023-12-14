package nl.saxion.client.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.saxion.client.commands.CommandManager;
import nl.saxion.client.communication.NetworkCommunicator;
import nl.saxion.client.network.ServerMessageProcessor;
import nl.saxion.client.session.SessionManager;
import nl.saxion.client.session.UserSession;
import nl.saxion.client.userinterface.UserInterfaceManager;
import nl.saxion.client.userinterface.input.ConsoleInput;
import nl.saxion.client.userinterface.output.ConsoleOutput;
import nl.saxion.shared.commands.Command;
import nl.saxion.shared.responses.Response;

/**
 *The class {@link ClientMediator} is a central coordinator to access different methods from other classes
 */
public class ClientMediator{
    private UserInterfaceManager uiManager;
    private ServerMessageProcessor messageProcessor;
    private NetworkCommunicator communicator;
    private CommandManager commandManager;
    private SessionManager sessionManager;

    /**
     * The constructor initiates all the classes so their methods can be implemented in the mediator
     * @param uiManager
     * @param messageProcessor
     * @param communicator
     * @param commandManager
     * @param sessionManager
     */
    public ClientMediator(UserInterfaceManager uiManager,
                          ServerMessageProcessor messageProcessor,
                          NetworkCommunicator communicator,
                          CommandManager commandManager,
                          SessionManager sessionManager) {
        this.uiManager = uiManager;
        this.messageProcessor = messageProcessor;
        this.communicator = communicator;
        this.commandManager = commandManager;
        this.sessionManager = sessionManager;
    }

    /**
     * This method invokes the {@link UserInterfaceManager#getUserInput()}
     * which in turn invokes the {@link ConsoleInput#getInput()}
     * @param prompt  Is the prompt that the user will be shown on screen
     * @return The user input
     */
    public String getInput(String prompt) {
        return uiManager.getUserInput(prompt);
    }

    /**
     *This method will invoke {@link ConsoleOutput#display(String)} to send a message to the client(s)
     * @param message The message that the server will send to the client
     */
    public void display(String message) {
        uiManager.display(message);
    }

    /**
     *This method will take an argument to send to the server
     * @param command The argument that gets sent to the server
     */
    public void sendToServer(Command command) {
        System.out.println("[LOG]: Sending command: " + command.toJson());
        communicator.sendRequest(command.toJson());
    }

    /**
     *The method that invokes {@link UserInterfaceManager#showMenu()} to display the command menu
     */
    public void showCommandMenu(){
        uiManager.showMenu();
    }

    /**
     *The method that invokes {@link CommandManager#processUserInput(String)}
     * to check what command is being used and if it is a valid command
     * @param userInput The argument that contains the command
     */
    public void processUserInput(String userInput) {
        commandManager.processUserInput(userInput);
    }

    /**
     *
     * @param message Contains the server message that will be sent to the client(s)
     */
    public void processServerMessage(String message) {
        try {
            System.out.println("[LOG]: response from server: " + message);
            messageProcessor.handleMessage(message);
        } catch (JsonProcessingException e) {
            display("Error processing server message: " + e);
        }
    }

    /**
     *The method that starts listening for user input
     */
    public void startListeningForUserInput(){
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                String userInput = uiManager.getUserInput();
                System.out.println("verwerk input: " + userInput);   // Verwerk de gebruikers input
                processUserInput(userInput);
            }
        }).start();
    }

    /**
     *The method that invokes {@link SessionManager#initiateLogin()} to ask the user to login
     */
    public void initiateLogin(){
        sessionManager.initiateLogin();
    }

    /**
     * The method that handles the login, and returns a server response
     * @param response The server response
     */
    public void handleLoginResponse(Response response) {
        sessionManager.handleLoginResponse(response);
    }

    /**
     *The method that invokes {@link UserSession#logout()} that logs the user out.
     */
    //TODO: Create logout functionality
    public void logout() {
        sessionManager.logout();
    }
    public String getUsername() { return sessionManager.getUsername(); }
}
