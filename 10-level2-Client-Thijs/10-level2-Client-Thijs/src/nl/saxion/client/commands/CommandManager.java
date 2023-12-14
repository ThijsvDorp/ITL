package nl.saxion.client.commands;

import nl.saxion.client.core.ClientMediator;
import nl.saxion.client.core.ClientMediator;
import nl.saxion.client.session.SessionManager;
import nl.saxion.shared.commands.BroadcastCommand;
import nl.saxion.shared.commands.LeaveCommand;

import java.io.Console;

/**
 * The {@link CommandManager} class manages the validation of the command and what kind of command is being put in.
 * {@link #processUserInput(String)} Checks what command is being sent, and processes it.
 * {@link #buildBroadcastCommand(String)} Checks if the message is not empty before getting sent to the client(s).
 */
public class CommandManager {
    ClientMediator mediator;

    public CommandManager() {
    }

    /**
     * This method processes the user input to check if there is a valid command being used.
     * @param userInput The command the user has submitted.
     */
    public void processUserInput(String userInput) {
        String[] parts = userInput.split(" ", 2);
        String command = parts[0];
        String arguments = parts.length > 1 ? parts[1] : "";
        String[] whisperParts = arguments.split("", 2);
        String username = parts[0];

        switch (command) {
            case "/broadcast":
                buildBroadcastCommand(arguments);
                break;
            case "/help":
                mediator.showCommandMenu();
                break;
            case "/leave":
                buildLeaveCommand();
                mediator.logout();
                break;
                //TODO: Create a dm functionality.
            case "/dm":

                break;
                //TODO: Create a guessing game functionality
            case "/startGame":

                break;
                //TODO: Create functionality to join the guessing game
            case "/join":

                break;
                //TODO: Create funtionality to guess a number of a game you joined
            case "/guess":

                break;
            default:
                System.out.println("Onbekend commando: " + command);
                break;
        }
    }
    //TODO: Create a leave command
    private void buildLeaveCommand() {
        if (mediator.getUsername() != null){
            LeaveCommand leaveCommand = new LeaveCommand(mediator.getUsername());
            mediator.sendToServer(leaveCommand);
        }
        else {
            mediator.display("[SYSTEM] Gebruiker is al uitgelogd]");
        }
    }

    /**
     * Checks if the message is not empty before it gets sent to the clients.
     * @param message Message containing user input.
     */
    private void buildBroadcastCommand(String message) {
        if (!message.isEmpty()) {
            BroadcastCommand broadcast = new BroadcastCommand(message);
            mediator.sendToServer(broadcast);
        }
        else {
            mediator.display("[SYSTEM]: Geen bericht opgegeven voor de broadcast.");
        }
    }

    /**
     * Sets the mediator class for {@link CommandManager}
     * @param mediator Takes a new mediator object
     */
    public void setMediator(ClientMediator mediator) {
        this.mediator = mediator;
    }
}

