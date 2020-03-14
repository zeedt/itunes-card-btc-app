package com.zeed.assignment.sms.controller;

import com.zeed.assignment.sms.service.CardServiceImpl;
import com.zeed.assignment.sms.service.UserUtil;
import com.zeed.assignment.sms.models.Authority;
import com.zeed.assignment.sms.models.Cards;
import com.zeed.assignment.sms.models.User;
import com.zeed.assignment.sms.repository.CardsRepository;
import com.zeed.assignment.sms.repository.UserRepositoy;
import com.zeed.assignment.sms.security.UserDetailsTokenEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zeed on 11/23/17.
 */
@Controller
public class AdminController {

    @Autowired
    private CardServiceImpl cardService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    CardsRepository cardsRepository;

    @Autowired
    UserRepositoy userRepositoy;

    private Logger logger = LoggerFactory.getLogger(AdminController.class.getName());

    @GetMapping("/adminHome")
    public String home(HttpSession httpSession, Model model){
            List<Cards> cardsList = cardService.pendingCards();
            if(!CollectionUtils.isEmpty(cardsList)){model.addAttribute("last",cardsList.get(cardsList.size()-1).getId());}else{
                model.addAttribute("last",0);
            }
            Collections.reverse(cardsList);
            model.addAttribute("user",getUsername());
            model.addAttribute("usercard",cardsList);
        return "admindashboard";
    }

    @GetMapping("/adminsignup")
    public String adminSignup(HttpSession httpSession, Model model){
        return "adminReg";
    }

    @GetMapping("/admindashboard")
    public String admindashboard(HttpSession httpSession, Model model){

            List<Cards> cardsList = cardService.pendingCards();
            if(!CollectionUtils.isEmpty(cardsList)){model.addAttribute("last",cardsList.get(cardsList.size()-1).getId());}else{
                model.addAttribute("last",0);
            }
            Collections.reverse(cardsList);
            model.addAttribute("user",getUsername());
            model.addAttribute("role",getAuthority());
            model.addAttribute("usercard",cardsList);

        return "admindashboard";
    }

    @PostMapping("/modalVerDecDet")
    public ModelAndView modalVerDecDet(HttpSession httpSession, Model model, @RequestBody Map<String,String> data){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminVerifyDeclineModal");
        Cards cards = cardsRepository.findCardsById(Long.valueOf(data.get("cardid")));

            if(cards != null && cards.getUser().getUsername().equals(data.get("user"))){
                    modelAndView.addObject("user",getUsername());
                    modelAndView.addObject("role",getAuthority());
                    modelAndView.addObject("cards",cards);
                    modelAndView.addObject("cardstatus",cards.getStatus().toString());
                    return modelAndView;
            }

        return null;
    }
    @PostMapping("/profileModal")
    public ModelAndView profileModal(HttpSession httpSession, Model model, @RequestBody Map<String,String> data){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminUserProfile");
        Cards cards = cardsRepository.findCardsById(Long.valueOf(data.get("cardid")));

            if(cards != null && cards.getUser().getUsername().equals(data.get("user"))){
                    modelAndView.addObject("user",getUsername());
                    modelAndView.addObject("role",getAuthority());
                    modelAndView.addObject("carduser",cards.getUser());
                    modelAndView.addObject("cardstatus",cards.getUser().toString());
                    return modelAndView;
            }
        return null;
    }

    @GetMapping("/adminlogout")
    public String logout(Principal principal, HttpServletRequest servletRequest){
        return logoutAdmin();
    }

    @GetMapping("/userEnquiry")
    public String userEnquiry(){
        return "userEnquiryPage";
    }

    @PostMapping("/getuserEnquiry")
    public ModelAndView getuserEnquiry(HttpSession httpSession, Model model, @RequestBody Map<String,String> data){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("adminUserProfile");
        User user1 = userRepositoy.findByUsername(data.get("username"));

        if(user1!=null){
            modelAndView.addObject("carduser",user1);
            return modelAndView;
        }
        return null;
    }

    @PostMapping("/updateDashB")
    public ModelAndView updateDashB(HttpSession httpSession,@RequestBody Map<String,String> data){

        try{
                List<Cards> cardsList = cardService.getUpdateCards(Long.valueOf(data.get("last")));
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.setViewName("appendupdate");
                if(!CollectionUtils.isEmpty(cardsList)) {
                    modelAndView.addObject("usercard", cardsList);
                    modelAndView.addObject("last", cardsList.get(cardsList.size()-1).getId());
                    return modelAndView;
                }else{
                    return null;
                }

        } catch(Exception e){
            logger.error("Error occurred due to ", e);
        }
        return null;
    }

    private String getUsername() {
        return ((UserDetailsTokenEnvelope) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    private String getAuthority() {
        return ((List<Authority>) ((UserDetailsTokenEnvelope) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities()).get(0).getAuthority();
    }

    public String logoutAdmin() {
        try {
            SecurityContextHolder.getContext().setAuthentication(null);
        } catch (Exception e) {
            logger.error("Error occured while logging out user due to ", e);
        }
        return "adminHome";
    }
}
