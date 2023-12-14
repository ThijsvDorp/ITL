package nl.saxion.client.network;

import nl.saxion.client.core.ClientMediator;
import nl.saxion.shared.commands.BroadcastCommand;
import nl.saxion.shared.commands.LeaveCommand;
import nl.saxion.shared.commands.LoginCommand;
import nl.saxion.shared.responses.Response;

/**
 * The {@link ResponseHandler} class handles all the server responses, so it gets sent to the user.
 */
public class ResponseHandler {
    ClientMediator mediator;
    public ResponseHandler() {
    }

    /**
     * The method that checks whether the return response is valid or invalid.
     * It also adds the correct response header
     * @param responseHeader Contains the response header (e.g. "LOGIN_RESP")
     * @param response Contains the response status
     */
    public void handleResponse(String responseHeader, Response response){
        if (response.getStatus().equals("ERROR") || response.getStatus() == null){
            mediator.display(response.getErrorMessage());
        } else if (response.getStatus().equals("OK")) {
            switch (responseHeader){
                case LoginCommand.RESPONSE_HEADER:
                    mediator.handleLoginResponse(response);
                    break;
                case BroadcastCommand.RESPONSE_HEADER:
                    handleBroadCastResponse(response);
                    break;
                case LeaveCommand.RESPONSE_HEADER:
                    handleLogoutResponse(response);
                    break;
                default:
                    mediator.display("[SERVER]: unidentified response: " + responseHeader + ", " + response.getErrorMessage());
                    break;
            }
        }
    }

    /**
     * This method checks whether the response status is valid or invalid
     * In the case of a valid response status, it displays a server confirmation that the broadcast has been sent to all users.
     * In the case of a invalid response status, it displays a server error message.
     * @param response Contains the server response
     */
    private void handleBroadCastResponse(Response response) {
        if ("OK".equals(response.getStatus())){
            mediator.display("[SYSTEM]: Broadcast sent to all users");
        }else{
            mediator.display(response.getErrorMessage());
        }
    }
    private void handleLogoutResponse(Response response) {
        if ("OK".equals(response.getStatus())){
            mediator.display("[SYSTEM]: BYE");
        }else{
            mediator.display(response.getErrorMessage());
        }
    }

    public void setMediator(ClientMediator mediator) {
        this.mediator = mediator;
    }
}
