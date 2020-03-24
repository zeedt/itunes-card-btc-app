package com.zeed.itbit.controller.restcontroller;

import com.zeed.itbit.apimodels.CardRequestModel;
import com.zeed.itbit.service.CardServiceImpl;
import com.zeed.itbit.service.UserUtil;
import com.zeed.itbit.enums.Status;
import com.zeed.itbit.models.Cards;
import com.zeed.itbit.models.User;
import com.zeed.itbit.repository.CardsRepository;
import com.zeed.itbit.repository.UserRepositoy;
import com.zeed.itbit.security.CustomPasswordEncoder;
import com.zeed.itbit.security.UserDetailsTokenEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by longbridge on 11/13/17.
 */
@RestController
@RequestMapping("/user")
public class UsersRestController {

    @Autowired
    UserUtil userUtil;

    @Autowired
    private CardServiceImpl cardService;

    @Autowired
    CardsRepository cardsRepository;

    @Autowired
    UserRepositoy userRepositoy;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    private Logger logger = LoggerFactory.getLogger(UsersRestController.class.getName());

    private static final String FILE_UPLOAD_PATH = "cardsUpload/card_";

    @PostMapping("/validateUser")
    public Object validateUser(@RequestBody User user, HttpSession httpSession){
        return userUtil.validateLogin(user);
    }
    @PostMapping("/validateAdminUser")
    public Object validateAdminUser(@RequestBody User user){
        return userUtil.validateAdminUser(user);
    }

    @PostMapping("/registerUser")
    public Object signUpUser(@RequestBody User userObject) throws Exception {
        return userUtil.registerUser(userObject);
    }

    @PreAuthorize(value = "hasAnyAuthority('USER')")
    @PostMapping("/user-cards")
    public Page<Cards> getUserCardsByPageWithFilters(@RequestBody CardRequestModel requestModel, Principal principal){
        return cardService.getCardsByPageWithFilterForUser(principal.getName(), requestModel);
    }

    @PostMapping("/getUserDetails")
    public Object getUserDetails(){

        return null;
    }

    @PreAuthorize(value = "hasAnyAuthority('USER')")
    @PostMapping("/uploadCard")
    public Object uploadCard(@RequestParam("itunescard") MultipartFile uploadfile,@RequestParam("desc") String desc,
                             @RequestParam("amount") String amount,HttpSession httpSession){
        HashMap<String,String> response = new HashMap<>();
        try {
            String filePath = FILE_UPLOAD_PATH + (new Date()).getTime()+".png";
            User user = userRepositoy.findByUsername(getLoggedInUser().getUsername());
                Cards cards = new Cards(desc, amount, filePath, user, new Date(), null, Status.PENDING);
                cards = cardService.addCards(cards, uploadfile.getBytes());
                if (cards != null) {
                    response.put("status", "success");
                    response.put("message", "Upload successful... Kindly wait for verification");
                } else {
                    response.put("status", "failure");
                    response.put("message", "Error in upload... Kindly retry or contact us");
                }
                return response;

        } catch (Exception e) {
            logger.error("Error occurred while uploading card due to ", e);
        }
        response.put("status","failure");
        return response;
    }

    @PostMapping("/upload-card")
    @PreAuthorize(value = "hasAnyAuthority('USER')")
    public boolean uploadCard(@RequestParam("itunescard") MultipartFile uploadfile, @RequestParam("desc") String desc,
                             @RequestParam("amount") String amount, Principal principal){
        try {
            String filePath =  FILE_UPLOAD_PATH + "/" + principal.getName() + (new Date()).getTime()+".png";
            User user = userRepositoy.findByUsername(getLoggedInUser().getUsername());
            Cards cards = new Cards(desc, amount, filePath, user, new Date(), null, Status.PENDING);
            cardService.addCards(cards, uploadfile.getBytes());
        } catch (Exception e) {
            logger.error("Error occurred while uploading card due to ", e);
            return false;
        }
        return true;
    }

    @GetMapping("get-card-as-base64")
    @PreAuthorize(value = "hasAnyAuthority('USER')")
    public Cards getCardAsBase64(@RequestParam("id") Long id, Principal principal) throws Exception {

        return cardService.getImageWithIdAndPrincipal(id, principal);

    }

    @GetMapping("download-card")
    @PreAuthorize(value = "hasAnyAuthority('USER')")
    public void downloadUploadedCardImage(@RequestParam("id") Long id, Principal principal, HttpServletResponse servletResponse) throws Exception {

        cardService.downloadUploadedCardImage(id, principal, servletResponse);

    }

    @PostMapping("/updateProfileDetails")
    @PreAuthorize(value = "hasAnyAuthority('USER')")
    public Object updateProfileDetails(@RequestBody HashMap<String,String> data,HttpSession httpSession){
        HashMap<String,String> response = new HashMap<>();
        try {
            User user = userRepositoy.findByUsername(getLoggedInUser().getUsername());
            if (user!=null) {
                user.setGender(data.get("gender"));
                user.setAccountNumber(data.get("accountNumber"));
                user.setLastName(data.get("lastName"));
                user.setFirstName(data.get("firstName"));
                user.setBank(data.get("bank"));
                user.setEmail(data.get("email"));
                userRepositoy.save(user);
                response.put("status","success");
                response.put("message","Profile Update successful.");
            }else{
                response.put("status","failure");
                response.put("message","Error in profile update... Kindly retry or contact us");
            }
            return response;
        } catch (Exception e) {
            logger.error("Error occurred while updateing profile due to ", e);
        }
        response.put("status","failure");
        response.put("message","Error in upload... Kindly retry or contact us");
        return response;
    }

    @PostMapping("/updatePasswordDetails")
    @PreAuthorize(value = "hasAnyAuthority('USER')")
    public Object updatePasswordDetails(@RequestBody HashMap<String,String> data,HttpSession httpSession){
        HashMap<String,String> response = new HashMap<>();
        try {
            User user = userRepositoy.findByUsername(getLoggedInUser().getUsername());
            if (user!=null) {
                if(customPasswordEncoder.passwordEncoder().matches(data.get("oldpassword"),user.getPassword())){
                    user.setPassword(customPasswordEncoder.passwordEncoder().encode(data.get("newpassword")));
                    userRepositoy.save(user);
                    response.put("status","success");
                    response.put("message","Password Update successful.");
                }else{
                    response.put("status","success");
                    response.put("message","Current password not correct.");
                }
            }else{
                response.put("status","failure");
                response.put("message","Error in password update... Kindly retry or contact us");
            }
            return response;
        } catch (Exception e) {
            logger.error("Error occurred due to ", e);
        }
        response.put("status","failure");
        response.put("message","Error in upload... Kindly retry or contact us");
        return response;
    }


    private User getLoggedInUser() {
        return ((UserDetailsTokenEnvelope) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }
}
