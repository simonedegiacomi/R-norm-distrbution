package rnormcalculator.delegator;

/**
 * Created by Slava on 08/04/2017.
 */
public class InvalidResponseException extends Exception {

    private String receivedResult;

    public InvalidResponseException(String receivedResult) {
        this.receivedResult = receivedResult;
    }

    public String getReceivedResult() {
        return receivedResult;
    }
}
