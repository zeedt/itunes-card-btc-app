package com.zeed.assignment.sms.security;

import com.zeed.assignment.sms.models.User;
import org.springframework.security.core.Authentication;

public interface AuthenticationProvider<T extends User> {

    Authentication authenticate(Authentication var1) throws Exception;

    boolean supports(Class<?> var1);

}
