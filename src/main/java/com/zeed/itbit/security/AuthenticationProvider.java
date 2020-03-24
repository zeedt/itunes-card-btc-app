package com.zeed.itbit.security;

import com.zeed.itbit.models.User;
import org.springframework.security.core.Authentication;

public interface AuthenticationProvider<T extends User> {

    Authentication authenticate(Authentication var1) throws Exception;

    boolean supports(Class<?> var1);

}
