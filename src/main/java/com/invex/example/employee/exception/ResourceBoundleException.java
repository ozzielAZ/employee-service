package com.invex.example.employee.exception;

import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * @author Ozziel
 */
@Slf4j
public class ResourceBoundleException extends RuntimeException {

    /** The Constant resourceBundle. */
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("business-exception");

    public static final String BASE_ERROR_PROPERTY = "message.error.%s";

    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /** Error code. */
    private int errorCode;

    /** HTTP status. */
    private HttpStatus httpStatus;

    /** Error description. */
    private String description;

    /**
     * Default constructor.
     */
    public ResourceBoundleException() {
    }

    /**
     * Constructor with error message.
     *
     * @param message the error message
     */
    public ResourceBoundleException(String message) {
        super(message);
    }

    private static String getCustomMessage(int errorCode) {
        String key = String.format(BASE_ERROR_PROPERTY, errorCode);
        String errorMessage = null;
        try {
            errorMessage = resourceBundle.getString(key);
            log.debug("ResourceBoundleException - errorCode: {} message: {}", errorCode, errorMessage);
        } catch (Exception e) {
            log.error("Error message not found for key {}", key, e);
            errorMessage = "";
        }
        return errorMessage;
    }

    /**
     * Constructor with error code.
     *
     * @param errorCode the error code
     */
    public ResourceBoundleException(Integer errorCode) {
        super(getCustomMessage(errorCode));
        this.description = getMessage();
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // Default status
    }

    /**
     * Constructor with error code and HTTP status.
     *
     * @param errorCode the error code
     * @param httpStatus the HTTP status
     */
    public ResourceBoundleException(Integer errorCode, HttpStatus httpStatus) {
        super(getCustomMessage(errorCode));
        this.description = getMessage();
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        super.initCause(new Throwable(getMessage()));
    }

    // Getters and setters
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getHttpStatusCode() {
        return httpStatus != null ? httpStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}