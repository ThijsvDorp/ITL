package nl.saxion.shared.commands;

/**
 * An interface that sends commands to the server in json.
 * {@link Command}
 */
public interface Command{
    String toJson();
}
