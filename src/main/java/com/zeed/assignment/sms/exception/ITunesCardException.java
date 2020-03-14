package com.zeed.assignment.sms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ITunesCardException extends Exception {


    public ITunesCardException(String message) {
        super(message);
    }

    public ITunesCardException(String message, Throwable cause) {
        super(message, cause);
    }
}
