package com.zeed.assignment.sms.service;

import com.zeed.assignment.sms.ItunesCardApplication;
import com.zeed.assignment.sms.apimodels.BitCoinRequestModel;
import com.zeed.assignment.sms.apimodels.CardRequestModel;
import com.zeed.assignment.sms.enums.Status;
import com.zeed.assignment.sms.exception.ITunesCardException;
import com.zeed.assignment.sms.models.BitCoin;
import com.zeed.assignment.sms.repository.BitCoinRepository;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BitCoinService {

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private BitCoinRepository bitCoinRepository;

    private static final String FILE_UPLOAD_PATH = "bitcoinupload/";

    @Transactional
    public boolean uploadBitCoinForSale(MultipartFile multipartFile, String walletId, String description, BigDecimal amount, Principal principal) throws ITunesCardException, IOException {

        BitCoin bitCoin = new BitCoin(walletId, description, amount);

        String filePath = String.format("%s/%s%s.png",FILE_UPLOAD_PATH,principal.getName(),(new Date()).getTime());
        byte[] byteFile = multipartFile.getBytes();
        FileUtils.writeByteArrayToFile(new File(filePath), byteFile);

        bitCoin.setFilePath(filePath);
        bitCoin.setDateAdded(new Date());
        bitCoin.setUser(userUtil.getLoggedInUser());
        bitCoin.setAmount(amount);

        bitCoinRepository.save(bitCoin);

        return true;

    }


    public BitCoin getImageWithIdAndPrincipal(Long id, Principal principal) throws Exception {

        BitCoin bitCoin = bitCoinRepository.findTopByIdAndUser_Username(id, principal.getName());

        if (bitCoin == null)
            throw new ITunesCardException("Bitcoin record not found");

        if (org.springframework.util.StringUtils.isEmpty(bitCoin.getFilePath()))
            throw new ITunesCardException("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(bitCoin.getFilePath()));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("data:image/png;base64,");
        stringBuilder.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageBytes,false)));
        bitCoin.setBase64Image(stringBuilder.toString());
        return bitCoin;

    }

    public BitCoin getImageWithId(Long id) throws Exception {

        BitCoin bitCoin = bitCoinRepository.findOne(id);

        if (bitCoin == null)
            throw new ITunesCardException("Bitcoin record not found");

        if (org.springframework.util.StringUtils.isEmpty(bitCoin.getFilePath()))
            throw new ITunesCardException("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(bitCoin.getFilePath()));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("data:image/png;base64,");
        stringBuilder.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageBytes,false)));
        bitCoin.setBase64Image(stringBuilder.toString());
        return bitCoin;

    }


    public void downloadUploadedCardImage(Long id, Principal principal, HttpServletResponse httpServletResponse) throws ITunesCardException, IOException {
        BitCoin bitCoin = bitCoinRepository.findTopByIdAndUser_Username(id, principal.getName());

        if (bitCoin == null)
            throw new ITunesCardException("BitCoin record not found");

        if (org.springframework.util.StringUtils.isEmpty(bitCoin.getFilePath()))
            throw new ITunesCardException("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(bitCoin.getFilePath()));

        String fileName = String.format("Uploaded card for %s", principal.getName());
        httpServletResponse.setContentType("image/png");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + fileName + "\"";
        httpServletResponse.setHeader(headerKey, headerValue);

        httpServletResponse.getOutputStream().write(imageBytes);

        httpServletResponse.flushBuffer();
    }

    public void downloadUploadedCard(Long id, HttpServletResponse httpServletResponse) throws ITunesCardException, IOException {
        BitCoin bitCoin = bitCoinRepository.findOne(id);

        if (bitCoin == null)
            throw new ITunesCardException("BitCoin record not found");

        if (org.springframework.util.StringUtils.isEmpty(bitCoin.getFilePath()))
            throw new ITunesCardException("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(bitCoin.getFilePath()));

        String fileName = String.format("Uploaded card for %s", bitCoin.getUser().getFirstName());
        httpServletResponse.setContentType("image/png");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + fileName + "\"";
        httpServletResponse.setHeader(headerKey, headerValue);

        httpServletResponse.getOutputStream().write(imageBytes);

        httpServletResponse.flushBuffer();
    }

    public Page<BitCoin> getBitCoinsByPageWithFilterForUser(String username, BitCoinRequestModel requestModel) {

        List<Predicate> searchPredicate = new ArrayList<>();

        return bitCoinRepository.findAll((Specification<BitCoin>) (root, criteriaQuery, criteriaBuilder) -> {
            if (requestModel.getFrom() != null && requestModel.getTo() != null)
                searchPredicate.add(criteriaBuilder.between((root.get("dateAdded")), new Date(requestModel.getFrom()), new Date(requestModel.getTo())));

            if (requestModel.getStatus() != null)
                searchPredicate.add(criteriaBuilder.equal((root.get("status")), requestModel.getStatus()));

            searchPredicate.add(criteriaBuilder.equal((root.get("user").get("username")), username));

            return criteriaBuilder.and(searchPredicate.toArray(new Predicate[0]));

        }, new PageRequest(requestModel.getPageNo(), requestModel.getPageSize(), Sort.Direction.DESC, "id"));
    }

    public Page<BitCoin> getBitCoinsByPageWithFilterForAdmin(BitCoinRequestModel requestModel) {
        List<Predicate> searchPredicate = new ArrayList<>();

        return bitCoinRepository.findAll((Specification<BitCoin>) (root, criteriaQuery, criteriaBuilder) -> {
            if (requestModel.getFrom() != null && requestModel.getTo() != null)
                searchPredicate.add(criteriaBuilder.between((root.get("dateAdded")), new Date(requestModel.getFrom()), new Date(requestModel.getTo())));

            if (requestModel.getStatus() != null)
                searchPredicate.add(criteriaBuilder.equal((root.get("status")), requestModel.getStatus()));

            if (!org.springframework.util.StringUtils.isEmpty(requestModel.getUsername())) {
                searchPredicate.add(criteriaBuilder.equal((root.get("user").get("username")), requestModel.getUsername()));
            }

            return criteriaBuilder.and(searchPredicate.toArray(new Predicate[0]));

        }, new PageRequest(requestModel.getPageNo(), requestModel.getPageSize(), Sort.Direction.DESC, "id"));
    }

    public boolean verifyCard(Long id) throws ITunesCardException {


        if (id == null)
            throw new ITunesCardException("Bitcoin Id cannot be null");

        BitCoin bitCoin = bitCoinRepository.findOne(id);

        if (bitCoin == null)
            throw new ITunesCardException("BitCoin not found");

        if (bitCoin.getStatus() != Status.PENDING)
            throw new ITunesCardException("BitCoin not be verified");

        bitCoin.setStatus(Status.VERIFIED);
        bitCoin.setVerifiedOn(new Date());
        bitCoinRepository.save(bitCoin);
        return true;
    }

    public boolean declineBitCoin(MultipartFile multipartFile, Long id, String declineReason) throws ITunesCardException, IOException {

        if (multipartFile == null)
            throw new ITunesCardException("No file uploaded.");

        if (id == null)
            throw new ITunesCardException("Bitcoin Id cannot be null");

        if (org.springframework.util.StringUtils.isEmpty(declineReason))
            throw new ITunesCardException("Decline reason cannot be blank");

        BitCoin bitCoin = bitCoinRepository.findOne(id);

        if (bitCoin == null)
            throw new ITunesCardException("Bit coin not found");

        if (bitCoin.getStatus() != Status.PENDING)
            throw new ITunesCardException("BitCoin not be verified");

        if (!multipartFile.getContentType().contains("image")) {
            throw new ITunesCardException("File uploaded must be an image");
        }

        String filePath =  FILE_UPLOAD_PATH + "decline/" + bitCoin.getUser().getUsername() + (new Date()).getTime()+".png";
        FileUtils.writeByteArrayToFile(new File(filePath), multipartFile.getBytes());
        bitCoin.setStatus(Status.VERIFICATION_DECLINED);
        bitCoin.setComment(declineReason);
        bitCoin.setVerifiedOn(new Date());
        bitCoin.setDeclinedImage(filePath);
        bitCoinRepository.save(bitCoin);
        return true;

    }

    public void downloadDeclineReasonImage(Long id, HttpServletResponse httpServletResponse) throws ITunesCardException, IOException {
        BitCoin bitCoin = bitCoinRepository.findOne(id);

        if (bitCoin == null)
            throw new ITunesCardException("Bitcoin record not found");

        if (org.springframework.util.StringUtils.isEmpty(bitCoin.getDeclinedImage()))
            throw new ITunesCardException("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(bitCoin.getDeclinedImage()));

        String fileName = String.format("Uploaded card for %s", bitCoin.getUser().getFirstName());
        httpServletResponse.setContentType("image/png");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + fileName + "\"";
        httpServletResponse.setHeader(headerKey, headerValue);

        httpServletResponse.getOutputStream().write(imageBytes);

        httpServletResponse.flushBuffer();

    }

    public void downloadMyDeclineReasonImage(Long id, Principal principal, HttpServletResponse httpServletResponse) throws ITunesCardException, IOException {
        BitCoin bitCoin = bitCoinRepository.findTopByIdAndUser_Username(id, principal.getName());

        if (bitCoin == null)
            throw new ITunesCardException("Bitcoin record not found");

        if (org.springframework.util.StringUtils.isEmpty(bitCoin.getDeclinedImage()))
            throw new ITunesCardException("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(bitCoin.getDeclinedImage()));

        String fileName = String.format("Uploaded card for %s", bitCoin.getUser().getFirstName());
        httpServletResponse.setContentType("image/png");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + fileName + "\"";
        httpServletResponse.setHeader(headerKey, headerValue);

        httpServletResponse.getOutputStream().write(imageBytes);

        httpServletResponse.flushBuffer();

    }
}
