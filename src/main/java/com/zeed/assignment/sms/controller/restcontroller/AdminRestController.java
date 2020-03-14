package com.zeed.assignment.sms.controller.restcontroller;

import com.zeed.assignment.sms.apimodels.CardRequestModel;
import com.zeed.assignment.sms.models.Cards;
import com.zeed.assignment.sms.service.CardServiceImpl;
import com.zeed.assignment.sms.service.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;

/**
 * Created by zeed on 11/24/17.
 */
@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @Autowired
    UserUtil userUtil;

    @Autowired
    CardServiceImpl cardService;

    private Logger logger = LoggerFactory.getLogger(AdminRestController.class.getName());

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @PostMapping("/user-cards")
    public Page<Cards> getUserCardsByPageWithFilters(@RequestBody CardRequestModel requestModel){
        return cardService.getCardsByPageWithFilterForAdmin(requestModel);
    }


    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @PostMapping("/decline-card")
    public boolean declineCard(@RequestParam("itunescardDeclineFile") MultipartFile multipartFile, @RequestParam("id") Long id,
                               @RequestParam("declineReason") String declineReason) throws Exception {
            return cardService.declineCard(multipartFile, id, declineReason);
    }


    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("/verify-card")
    public boolean verifyCard(@RequestParam("id") Long id) throws Exception {
            return cardService.verifyCard(id);
    }


    @GetMapping("get-card-as-base64")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public Cards getCardAsBase64(@RequestParam("id") Long id) throws Exception {

        return cardService.getImageWithIdForAdmin(id);

    }

    @GetMapping("download-card")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public void downloadUploadedCardImage(@RequestParam("id") Long id, HttpServletResponse servletResponse) throws Exception {

        cardService.downloadUploadedCardImageForAdmin(id, servletResponse);

    }

    @GetMapping("download-card-decline-evidence")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public void downloadDeclineReasonFile(@RequestParam("id") Long id, HttpServletResponse servletResponse) throws Exception {
        cardService.downloadDeclineReasonImage(id, servletResponse);

    }
}
