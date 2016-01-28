package org.globex.support;

public class AccountError {

    private int errorCode;
    private String errorMessage;
    private String message;
    private Status status;

    public AccountError(int errorCode, String errorMessage, String message) {
        this(errorCode, errorMessage, message, Status.ERROR);
    }

    public AccountError(int errorCode, String errorMessage, String message, Status status) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.message = message;
        this.status = status;
    }

    public void fix() {
        if (status != Status.ERROR) {
            throw new IllegalStateException(String.format("You cannot fix an account error with status [%s]. Only ERROR status allowed", status));
        }
        status = Status.FIXED;
    }

    public void close() {
        if (status != Status.FIXED) {
            throw new IllegalStateException(String.format("You cannot fix an account error with status [%s]. Only FIXED status allowed", status));
        }
        status = Status.CLOSE;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "AccountError{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
