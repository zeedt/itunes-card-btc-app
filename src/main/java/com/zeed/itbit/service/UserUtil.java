package com.zeed.itbit.service;

import com.zeed.itbit.enums.Role;
import com.zeed.itbit.models.User;
import com.zeed.itbit.repository.UserRepositoy;
import com.zeed.itbit.security.CustomPasswordEncoder;
import com.zeed.itbit.security.UserDetailsTokenEnvelope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by zeed on 11/14/17.
 */
@Service
public class UserUtil {

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    private static final String USER_FOUND_MESSAGE = "User found";

    private static final String INVALID_CREDENTIALS = "Invalid username/password";

    @Autowired
    UserRepositoy userRepositoy;
    public User registerUser(User user) throws Exception {
        User user1 = userRepositoy.findByUsername(user.getUsername());
        if(user1==null){
            user.setPassword(customPasswordEncoder.passwordEncoder().encode(user.getPassword()));
            user.setRole(Role.USER);
            userRepositoy.save(user);
            user.setMessage("Registration successfull");
        }else{
            user.setMessage("Username already exists");
        }
        return user;
    }
    public User registerAdminUser(User user) throws Exception {
        User user1 = userRepositoy.findByUsername(user.getUsername());
        if(user1==null){
            user.setPassword(customPasswordEncoder.passwordEncoder().encode(user.getPassword()));
            user.setRole(Role.ADMIN);
            userRepositoy.save(user);
            user.setMessage("Registration successfull");
        }else{
            user.setMessage("Username already exists");
        }
        return user;
    }
    public User validateLogin(User user){
        User existingUser = userRepositoy.findByUsernameAndRole(user.getUsername(),Role.USER);
        return getUser(user, existingUser);
    }
    public User validateAdminUser(User user){
        User existingUser = userRepositoy.findByUsernameAndRole(user.getUsername(),Role.ADMIN);
        return getUser(user, existingUser);
    }

    private User getUser(User user, User existingUser) {
        if(existingUser!=null) {
            if (customPasswordEncoder.passwordEncoder().matches(user.getPassword(), existingUser.getPassword())) {
                existingUser.setMessage(USER_FOUND_MESSAGE);
                existingUser.setPassword("");
                return existingUser;
            } else {
                existingUser.setPassword("");
                existingUser.setMessage(INVALID_CREDENTIALS);
                return existingUser;
            }
        }else{
            user.setMessage(INVALID_CREDENTIALS);
            return user;
        }
    }

    public User getLoggedInUser() {
        return ((UserDetailsTokenEnvelope) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

}
