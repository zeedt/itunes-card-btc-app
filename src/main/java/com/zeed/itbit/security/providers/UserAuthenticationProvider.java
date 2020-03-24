package com.zeed.itbit.security.providers;

import com.zeed.itbit.models.Authority;
import com.zeed.itbit.models.User;
import com.zeed.itbit.repository.UserRepositoy;
import com.zeed.itbit.security.AuthenticationProvider;
import com.zeed.itbit.security.CustomPasswordEncoder;
import com.zeed.itbit.security.UserDetailsTokenEnvelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepositoy userRepositoy;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws Exception {

        User managedUser = userRepositoy.findByUsername(authentication.getPrincipal().toString());

        if (managedUser == null)
            throw new Exception("User not found");

        if (!customPasswordEncoder.passwordEncoder().matches(authentication.getCredentials().toString(),managedUser.getPassword()))
            throw new Exception("Invalid credentials");

            UserDetailsTokenEnvelope userDetailsTokenEnvelope = new UserDetailsTokenEnvelope(managedUser);
            Authority authority = new Authority();
            authority.setAuthority(managedUser.getRole().toString());
            UsernamePasswordAuthenticationToken authenticationDetails = new UsernamePasswordAuthenticationToken(userDetailsTokenEnvelope,null, Collections.singleton(authority));
            return authenticationDetails;

    }

    @Override
    public boolean supports(Class var1) {
        return true;
    }
}
