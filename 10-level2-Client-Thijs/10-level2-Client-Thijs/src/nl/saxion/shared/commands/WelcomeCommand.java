package nl.saxion.shared.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WelcomeCommand implements Command{
    public static final String REQUEST_HEADER = "WELCOME";
    public static final String RESPONSE_HEADER = "WELCOME";
    private String message = "welcome to the server ${VERSION}";

    @JsonCreator
    public WelcomeCommand(@JsonProperty("msg") String message) {
        this.message = message;
    }


    @Override
    public String toJson() {
        return "WELCOME {\"name\":\"" + message + "\"}";
    }

    @Override
    public String toString() {
        return message;
    }
}
