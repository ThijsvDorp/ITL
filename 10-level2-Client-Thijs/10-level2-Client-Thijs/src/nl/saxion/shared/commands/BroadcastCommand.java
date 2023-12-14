package nl.saxion.shared.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BroadcastCommand implements Command {
    public static final String REQUEST_HEADER = "BROADCAST_REQ";
    public static final String COMMAND_HEADER = "BROADCAST";
    public static final String RESPONSE_HEADER = "BROADCAST_RESP";
    public static final String DESCRIPTION = "Sends a message to all users in the server";
    private String username;
    private final String message;

    public BroadcastCommand(String message) {
        this.message = message;
    }

    @JsonCreator
    public BroadcastCommand(@JsonProperty("username") String username, @JsonProperty("message") String message) {
        this.username = username;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Broadcast from" + username + ": " + message;
    }

    @Override
    public String toJson() {
        return REQUEST_HEADER + " {\"message\":\"" + message + "\"}";
    }
}
