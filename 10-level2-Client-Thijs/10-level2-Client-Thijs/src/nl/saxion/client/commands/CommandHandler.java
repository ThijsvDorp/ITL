package nl.saxion.client.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.saxion.client.core.ClientMediator;
import nl.saxion.shared.commands.WelcomeCommand;

/**
 *The {@link CommandHandler} class handles the command the server will display.
 * {@link #processCommand(String, String)} Builds server message
 * {@link #setMediator(ClientMediator)} Sets a new mediator
 */
public class CommandHandler {
    ClientMediator mediator;
    private final ObjectMapper objectMapper;

    public CommandHandler() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     *This method builds a server message, based on the header (Command)
     * @param header Contains the command
     * @param jsonBody Contains the message that is being sent with the command.
     * @throws JsonProcessingException Throws an exception when the value couldn't be read.
     */
    public void processCommand(String header, String jsonBody) throws JsonProcessingException {
        switch (header){
            case WelcomeCommand.RESPONSE_HEADER:
                WelcomeCommand welcome = objectMapper.readValue(jsonBody, WelcomeCommand.class);
                mediator.display(welcome.toString());
                mediator.initiateLogin();
                break;
            default:
                mediator.display("Unidentified server message, Header: " + header + ", payLoad: " +jsonBody);
                break;
        }
    }

    /**
     * This method sets a new mediator for the {@link CommandHandler} class
     * @param mediator The mediator.
     */
    public void setMediator(ClientMediator mediator) {
        this.mediator = mediator;
    }
}
