package com.pathofthefood.flyingburger.utils;

/**
 * Created by co_mmsalinas on 12/11/2014.
 */
public class ResponseErrorCodeException extends Exception {
    private int errorCode;

    public ResponseErrorCodeException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
