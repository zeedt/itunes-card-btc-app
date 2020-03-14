package com.zeed.assignment.sms.security;

import com.zeed.assignment.sms.security.exception.ITunesAuthenticationException;
import com.zeed.assignment.sms.security.providers.UserAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OauthAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    private Logger logger = LoggerFactory.getLogger(OauthAuthenticationProvider.class.getName());

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = (authentication.getCredentials() !=null ) ? authentication.getCredentials().toString() : null;

        UsernamePasswordAuthenticationToken tokenAuthentication = new UsernamePasswordAuthenticationToken(userName, password, null);
        tokenAuthentication.setDetails(authentication.getDetails());

        List<com.zeed.assignment.sms.security.AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(userAuthenticationProvider);

        for (com.zeed.assignment.sms.security.AuthenticationProvider authenticationProvider:authenticationProviders) {

            try {
                return authenticationProvider.authenticate(tokenAuthentication);
            } catch (Exception e) {
                throw new ITunesAuthenticationException(e.getMessage(),e);
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
