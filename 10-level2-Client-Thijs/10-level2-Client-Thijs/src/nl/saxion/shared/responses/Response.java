package nl.saxion.shared.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Response {
    private String status;
    private final int code;


    @JsonCreator
    public Response(@JsonProperty("status") String status, @JsonProperty("code") Integer code) {
        this.status = status;
        // Stel een standaardwaarde in voor code indien niet gespecificeerd
        this.code = (code != null) ? code : -1;

        // Stel een standaardwaarde in voor status indien niet gespecificeerd
        if (this.status == null && this.code != -1) {
            this.status = "ERROR";
        }
    }

    public String getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage(){
        return this.code == -1 ? "Error " + this.getCode() + ": " + ErrorMessage.get(this.getCode()) : "Geen foutmelding";
    }
}
