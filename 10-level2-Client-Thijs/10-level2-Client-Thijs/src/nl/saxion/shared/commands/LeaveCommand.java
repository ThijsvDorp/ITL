package nl.saxion.shared.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LeaveCommand implements Command{
    public static final String REQUEST_HEADER = "LOGOUT_REQ";
    public static final String RESPONSE_HEADER = "LOGOUT_RESP";
    public static final String DESCRIPTION = "Logs the user out of the system";
    private String username;

    public LeaveCommand(String username){
        this.username = username;
    }

    @Override
    public String toJson() {
        return REQUEST_HEADER + " {\"username\":\"" + username + "\"}";
    }

    @JsonCreator
    public LeaveCommand(@JsonProperty("username") String username, @JsonProperty("message") String message) {
        this.username = username;
        this.username = message;

    }

}
