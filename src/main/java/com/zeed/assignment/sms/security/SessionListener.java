package com.zeed.assignment.sms.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    private Logger logger = LoggerFactory.getLogger(SessionListener.class.getName());

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setMaxInactiveInterval(5*60);

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        logger.info("Session expired for user");
    }
}
