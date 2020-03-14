package com.zeed.assignment.sms.controller.restcontroller;

import com.zeed.assignment.sms.apimodels.BitCoinRequestModel;
import com.zeed.assignment.sms.models.BitCoin;
import com.zeed.assignment.sms.service.BitCoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/user/bitcoin")
public class BitCoinController {

    @Autowired
    private BitCoinService bitCoinService;

    private Logger logger = LoggerFactory.getLogger(BitCoinController.class.getName());

    @PostMapping("/buy")
    public boolean uploadBitCoinForSelling(@RequestParam("bitcoinFile") MultipartFile multipartFile, @RequestParam("desc") String desc, @RequestParam("walletId") String walletId,
                                           @RequestParam("amount") BigDecimal amount, Principal principal) {

        try {
            return bitCoinService.uploadBitCoinForSale(multipartFile, walletId, desc, amount, principal);
        } catch (Exception e) {
            logger.error("Error occurred while adding card due to ", e);
            return false;
        }
    }


    @GetMapping("get-card-as-base64")
    @PreAuthorize(value = "hasAnyAuthority('USER')")
    public BitCoin getBitCoinImageAsBase64(@RequestParam("id") Long id, Principal principal) throws Exception {

        return bitCoinService.getImageWithIdAndPrincipal(id, principal);

    }

    @GetMapping("get-card-as-base64-for-admin")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public BitCoin getBitCoinImageAsBase64(@RequestParam("id") Long id) throws Exception {

        return bitCoinService.getImageWithId(id);

    }


    @GetMapping("download-bitcoin-image")
    @PreAuthorize(value = "hasAnyAuthority('USER')")
    public void downloadUploadedCardImage(@RequestParam("id") Long id, Principal principal, HttpServletResponse servletResponse) throws Exception {

        bitCoinService.downloadUploadedCardImage(id, principal, servletResponse);

    }

    @GetMapping("download-bitcoin-image-for-admin")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public void downloadUploadedBitCoinImageForAdmin(@RequestParam("id") Long id, HttpServletResponse servletResponse) throws Exception {

        bitCoinService.downloadUploadedCard(id, servletResponse);

    }

    @PreAuthorize(value = "hasAnyAuthority('USER')")
    @PostMapping("/my-upload")
    public Page<BitCoin> getUserCardsByPageWithFilters(@RequestBody BitCoinRequestModel requestModel, Principal principal){
        return bitCoinService.getBitCoinsByPageWithFilterForUser(principal.getName(), requestModel);
    }


    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @PostMapping("/user-uploads")
    public Page<BitCoin> getUserCardsByPageWithFilters(@RequestBody BitCoinRequestModel requestModel){
        return bitCoinService.getBitCoinsByPageWithFilterForAdmin(requestModel);
    }

    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @GetMapping("/verify-bitcoin")
    public boolean verifyCard(@RequestParam("id") Long id) throws Exception {
        return bitCoinService.verifyCard(id);
    }


    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    @PostMapping("/decline-bitcoin")
    public boolean declineCard(@RequestParam("bitcoinDeclineFile") MultipartFile multipartFile, @RequestParam("id") Long id,
                               @RequestParam("declineReason") String declineReason) throws Exception {
        return bitCoinService.declineBitCoin(multipartFile, id, declineReason);
    }


    @GetMapping("/download-card-decline-evidence")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public void downloadDeclineReasonFile(@RequestParam("id") Long id, HttpServletResponse servletResponse) throws Exception {
        bitCoinService.downloadDeclineReasonImage(id, servletResponse);

    }

    @GetMapping("/download-my-card-decline-evidence")
    @PreAuthorize(value = "hasAnyAuthority('USER')")
    public void downloadMyDeclineReasonFile(@RequestParam("id") Long id, Principal principal, HttpServletResponse servletResponse) throws Exception {
        bitCoinService.downloadMyDeclineReasonImage(id, principal, servletResponse);

    }
}
