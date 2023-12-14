package nl.saxion.client.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.saxion.client.commands.CommandHandler;
import nl.saxion.client.core.ClientMediator;
import nl.saxion.shared.responses.Response;

public class ServerMessageProcessor {
    ClientMediator mediator;
    private ObjectMapper objectMapper;
    ResponseHandler responseHandler;
    CommandHandler commandHandler;

    public ServerMessageProcessor() {
        this.objectMapper = new ObjectMapper();
        this.responseHandler = new ResponseHandler();
        this.commandHandler = new CommandHandler();
    }

    /**
     *  The method that checks whether the message is a response or a command
     * @param messageJson the message that needs to be handled.
     * @throws JsonProcessingException
     */
    //TODO: Make a nice try-catch instead of method throwing
    public void handleMessage(String messageJson) throws JsonProcessingException {
        String[] parts = messageJson.split(" ", 2);
        String header = parts[0];
        String jsonBody = parts.length > 1 ? parts[1] : "{}";

        if (isResponse(jsonBody)) {
            Response response = objectMapper.readValue(jsonBody, Response.class);
            responseHandler.handleResponse(header, response);
        } else {
            commandHandler.processCommand(header, jsonBody);
        }
    }

    /**
     *
     * @param jsonBody the body of the response.
     * @return boolean on whether the response has a status or code.
     * @throws JsonProcessingException
     */
    private boolean isResponse(String jsonBody) throws JsonProcessingException {
        JsonNode rootNode = objectMapper.readTree(jsonBody);
        return rootNode.has("status") || rootNode.has("code");
    }

    /**
     * This method sets the mediator for the {@link CommandHandler}, {@link ResponseHandler} classes.
     * @param mediator the mediator
     */
    public void setMediator(ClientMediator mediator) {
        this.mediator = mediator;
        commandHandler.setMediator(mediator);
        responseHandler.setMediator(mediator);
    }
}
