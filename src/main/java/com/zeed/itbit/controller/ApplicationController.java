package com.zeed.itbit.controller;


import com.zeed.itbit.service.CardServiceImpl;
import com.zeed.itbit.enums.Status;
import com.zeed.itbit.models.Cards;
import com.zeed.itbit.repository.UserRepositoy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by longbridge on 11/13/17.
 */
@Controller
public class ApplicationController {

    @Autowired
    private CardServiceImpl cardService;
    
    @Autowired
    private UserRepositoy userRepositoy;

    @Autowired
    private JdbcTokenStore jdbcTokenStore;

    private Logger logger = LoggerFactory.getLogger(ApplicationController.class.getName());

    @GetMapping("/")
    public String home(Model model){
        List<Cards> cardsList = cardService.getUserCardsAndStatus(getUsername(), Status.PENDING);
        Collections.reverse(cardsList);
        model.addAttribute("user",getUsername());
        model.addAttribute("usercard",cardsList);

        return "dashboard";
    }

    @GetMapping("/ping")
    public void ping() {
        return;
    }

    @GetMapping("/dashboard")
    public String login(Model model){
        
        List<Cards> cardsList = cardService.getUserCardsAndStatus(getUsername(), Status.PENDING);
        Collections.reverse(cardsList);
        model.addAttribute("user",getUsername());
        model.addAttribute("role",getAuthority());
        model.addAttribute("usercard",cardsList);
        return "dashboard";
    }

    @GetMapping("/declined")
    public String declined(Model model){
            List<Cards> cardsList = cardService.getUserCardsAndStatus(getUsername(), Status.VERIFICATION_DECLINED);
            Collections.reverse(cardsList);
            model.addAttribute("user",getUsername());
            model.addAttribute("role",getAuthority());
            model.addAttribute("usercard",cardsList);
            return "declined";
        
    }

    @GetMapping("/verified")
    public String verified(Model model){
            List<Cards> cardsList = cardService.getUserCardsAndStatus(getUsername(), Status.VERIFIED);
            Collections.reverse(cardsList);
            model.addAttribute("user",getUsername());
            model.addAttribute("role",getAuthority());
            model.addAttribute("usercard",cardsList);
        return "verified";
    }

    @GetMapping("/usersignup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/home")
    public String login(){
        return "home";
    }

    @GetMapping("/uploadCard")
    public String uploadCard(HttpSession httpSession){
        return "uploadCard";
    }
    
    @GetMapping("/editProfile")
    public String editProfile(Model model){
            model.addAttribute("user",userRepositoy.findByUsername(getUsername()));
            return "editProfile";
    }
    
    @GetMapping("/userlogout")
    public String logout(Principal principal){
            return logoutUser(principal.getName());
        }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    private String getAuthority() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
    }

    public String logoutUser(String username) {
        try {
            Collection<OAuth2AccessToken> oAuth2AccessTokens = ((JdbcTokenStore) jdbcTokenStore).findTokensByUserName(username);

            for (OAuth2AccessToken oAuth2AccessToken : oAuth2AccessTokens) {
                ((JdbcTokenStore) jdbcTokenStore).removeAccessToken(oAuth2AccessToken);
            }
        } catch (Exception e) {
            logger.error("Error occured while logging out user due to ", e);
        }
        return "home";
    }

}
