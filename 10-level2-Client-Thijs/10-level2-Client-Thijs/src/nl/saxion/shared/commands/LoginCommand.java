package nl.saxion.shared.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginCommand implements Command {
    private final String username;
    public static final String REQUEST_HEADER = "LOGIN";
    public static final String RESPONSE_HEADER = "LOGIN_RESP";

    /**
     * This constructor sets the username of the to-be logged-in user, so it can be used to create a output by the server.
     * @param username Is the username of the to-be logged-in user.
     */
    @JsonCreator
    public LoginCommand(@JsonProperty("username") String username) {
        this.username = username;
    }

    /**
     *
     * @return Returns a json string
     */
    @Override
    public String toJson() {
        return "LOGIN {\"username\":\"" + username + "\"}";
    }

    /**
     * Verifies if the username has been set.
     * @return {@code true} if username is not 'null'; otherwise {@code false}
     */
    public boolean verifyUsername() {
        return username != null;
    }
}
