package com.zeed.assignment.sms.service;

import com.zeed.assignment.sms.apimodels.CardRequestModel;
import com.zeed.assignment.sms.enums.CardsSearchFilter;
import com.zeed.assignment.sms.enums.Status;
import com.zeed.assignment.sms.exception.ITunesCardException;
import com.zeed.assignment.sms.models.Cards;
import com.zeed.assignment.sms.repository.CardsRepository;
import com.zeed.assignment.sms.repository.UserRepositoy;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import javax.smartcardio.Card;
import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zeed on 11/22/17.
 */
@Service
public class CardServiceImpl{

    @Autowired
    CardsRepository cardsRepository;

    @Autowired
    UserRepositoy userRepositoy;

    private Logger logger = LoggerFactory.getLogger(CardServiceImpl.class.getName());

    private static final String FILE_DECLINED_UPLOAD_PATH = "cardsUpload/cardDeclined_";


    public Cards addCards(Cards cards, byte[] bytes) {
        try {
            FileUtils.writeByteArrayToFile(new File(cards.getFilePath()), bytes);
            cardsRepository.save(cards);
            cards.getUser().getCards().add(cards);
            userRepositoy.save(cards.getUser());
            return cards;
        } catch (Exception e) {
            logger.error("Error occurred while adding card due to ", e);
        }
        return null;
    }

    public boolean declineCard(MultipartFile multipartFile, Long id, String declineReason) throws Exception {

        if (id == null)
            throw new ITunesCardException("Card Id cannot be null");

        if (org.springframework.util.StringUtils.isEmpty(declineReason))
            throw new ITunesCardException("Decline reason cannot be blank");

        Cards cards = cardsRepository.findCardsById(id);

        if (cards == null)
            throw new ITunesCardException("Card not found");

        if (cards.getStatus() != Status.PENDING)
            throw new ITunesCardException("Card not be verified");

        String filePath =  FILE_DECLINED_UPLOAD_PATH + "/" + cards.getUser().getUsername() + (new Date()).getTime()+".png";
        FileUtils.writeByteArrayToFile(new File(filePath), multipartFile.getBytes());
        cards.setStatus(Status.VERIFICATION_DECLINED);
        cards.setComment(declineReason);
        cards.setVerifiedOn(new Date());
        cards.setDeclinedImage(filePath);
        cardsRepository.save(cards);
        return true;
    }

    public boolean verifyCard(Long id) throws Exception {

        if (id == null)
            throw new ITunesCardException("Card Id cannot be null");

        Cards cards = cardsRepository.findCardsById(id);

        if (cards == null)
            throw new ITunesCardException("Card not found");

        if (cards.getStatus() != Status.PENDING)
            throw new ITunesCardException("Card not be verified");

        cards.setStatus(Status.VERIFIED);
        cards.setVerifiedOn(new Date());
        cardsRepository.save(cards);
        return true;
    }

    public List<Cards> pendingCards() {
        return cardsRepository.findCardsByStatus(Status.PENDING);
    }

    public void logSomething() {
        logger.info("I am the log something service");

    }

    public List<Cards> getUpdateCards(Long last) {
        List<Cards> cardsList = cardsRepository.findCardsByIdGreaterThan(last);
        return cardsList;
    }

    public List<Cards> getUserCardsAndStatus(String username, Status status) {
        return cardsRepository.findAllByUser_UsernameAndStatus(username, status);
    }


    public List<Cards> getUserCardsByPage(int pageNo, int pageSize, Principal principal) {
        return cardsRepository.findAllByUser_Username(principal.getName(), new PageRequest(pageNo, pageSize, Sort.Direction.DESC));
    }

    public Page<Cards> getCardsByPageWithFilterForUser(String username, CardRequestModel requestModel) {
        List<Predicate> searchPredicate = new ArrayList<>();

        return cardsRepository.findAll((Specification<Cards>) (root, criteriaQuery, criteriaBuilder) -> {
            if (requestModel.getFrom() != null && requestModel.getTo() != null)
                searchPredicate.add(criteriaBuilder.between((root.get("uploadedOn")), new Date(requestModel.getFrom()), new Date(requestModel.getTo())));

            if (requestModel.getStatus() != null)
                searchPredicate.add(criteriaBuilder.equal((root.get("status")), requestModel.getStatus()));

            searchPredicate.add(criteriaBuilder.equal((root.get("user").get("username")), username));

            return criteriaBuilder.and(searchPredicate.toArray(new Predicate[0]));

        }, new PageRequest(requestModel.getPageNo(), requestModel.getPageSize(), Sort.Direction.DESC, "id"));
    }

    public Page<Cards> getCardsByPageWithFilterForAdmin(CardRequestModel requestModel) {
        List<Predicate> searchPredicate = new ArrayList<>();

        return cardsRepository.findAll((Specification<Cards>) (root, criteriaQuery, criteriaBuilder) -> {
            if (requestModel.getFrom() != null && requestModel.getTo() != null)
                searchPredicate.add(criteriaBuilder.between((root.get("uploadedOn")), new Date(requestModel.getFrom()), new Date(requestModel.getTo())));

            if (requestModel.getStatus() != null)
                searchPredicate.add(criteriaBuilder.equal((root.get("status")), requestModel.getStatus()));

            if (!org.springframework.util.StringUtils.isEmpty(requestModel.getUsername())) {
                searchPredicate.add(criteriaBuilder.equal((root.get("user").get("username")), requestModel.getUsername()));
            }

            return criteriaBuilder.and(searchPredicate.toArray(new Predicate[0]));

        }, new PageRequest(requestModel.getPageNo(), requestModel.getPageSize(), Sort.Direction.DESC, "id"));
    }

    public Cards getImageWithIdAndPrincipal(Long id, Principal principal) throws Exception {

        Cards card = cardsRepository.findCardsByIdAndUser_Username(id, principal.getName());

        if (card == null)
            throw new ITunesCardException("Card not found");

        if (org.springframework.util.StringUtils.isEmpty(card.getFilePath()))
            throw new ITunesCardException("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(card.getFilePath()));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("data:image/png;base64,");
        stringBuilder.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageBytes,false)));
        card.setBase64Image(stringBuilder.toString());
        return card;

    }

    public Cards getImageWithIdForAdmin(Long id) throws Exception {

        Cards card = cardsRepository.findOne(id);

        if (card == null)
            throw new ITunesCardException("Card not found");

        if (org.springframework.util.StringUtils.isEmpty(card.getFilePath()))
            throw new ITunesCardException("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(card.getFilePath()));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("data:image/png;base64,");
        stringBuilder.append(StringUtils.newStringUtf8(Base64.encodeBase64(imageBytes,false)));
        card.setBase64Image(stringBuilder.toString());
        return card;

    }

    public void downloadUploadedCardImage(Long id, Principal principal, HttpServletResponse httpServletResponse) throws Exception {

        Cards card = cardsRepository.findCardsByIdAndUser_Username(id, principal.getName());

        if (card == null)
            throw new ITunesCardException("Card not found");

        if (org.springframework.util.StringUtils.isEmpty(card.getFilePath()))
            throw new ITunesCardException("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(card.getFilePath()));

        String fileName = String.format("Uploaded card for %s", principal.getName());
        httpServletResponse.setContentType("image/png");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + fileName + "\"";
        httpServletResponse.setHeader(headerKey, headerValue);

        httpServletResponse.getOutputStream().write(imageBytes);

        httpServletResponse.flushBuffer();

    }

    public void downloadUploadedCardImageForAdmin(Long id,  HttpServletResponse httpServletResponse) throws Exception {

        Cards card = cardsRepository.findOne(id);

        if (card == null)
            throw new Exception("Card not found");

        if (org.springframework.util.StringUtils.isEmpty(card.getFilePath()))
            throw new Exception("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(card.getFilePath()));

        String fileName = String.format("Uploaded card for %s", card.getUser());
        httpServletResponse.setContentType("image/png");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + fileName + "\"";
        httpServletResponse.setHeader(headerKey, headerValue);

        httpServletResponse.getOutputStream().write(imageBytes);

        httpServletResponse.flushBuffer();

    }
    public void downloadDeclineReasonImage(Long id,  HttpServletResponse httpServletResponse) throws Exception {

        Cards card = cardsRepository.findOne(id);

        if (card == null)
            throw new ITunesCardException("Card not found");

        if (org.springframework.util.StringUtils.isEmpty(card.getDeclinedImage()))
            throw new ITunesCardException("No file path specified");

        byte[] imageBytes = FileUtils.readFileToByteArray(new File(card.getDeclinedImage()));

        String fileName = String.format("Uploaded card for %s", card.getUser());
        httpServletResponse.setContentType("image/png");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=\"" + fileName + "\"";
        httpServletResponse.setHeader(headerKey, headerValue);

        httpServletResponse.getOutputStream().write(imageBytes);

        httpServletResponse.flushBuffer();

    }
}
