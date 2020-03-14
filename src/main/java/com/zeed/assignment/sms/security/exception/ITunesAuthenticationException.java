package com.zeed.assignment.sms.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ITunesAuthenticationException extends AuthenticationException {
    public ITunesAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public ITunesAuthenticationException(String msg) {
        super(msg);
    }
}
