package com.zeed.itbit.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zeed.itbit.enums.Status;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "bit_coin")
public class BitCoin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String walletId;

    @NotNull
    @OneToOne
    @JsonIgnoreProperties(value = {"cards"})
    private User user;

    @NotNull
    private Date dateAdded;

    @Lob
    @NotBlank
    private String filePath;

    @Lob
    private String description;

    private BigDecimal amount;

    @Lob
    private String declinedReasonFilePath;

    @Lob
    private String declineReason;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Transient
    private String base64Image;

    private Date verifiedOn;

    @Lob
    private String comment;

    @Lob
    private String declinedImage;

    public BitCoin(String walletId, String description, BigDecimal amount) {
        this.walletId = walletId;
        this.description = description;
        this.amount = amount;
    }

    public BitCoin() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDeclinedReasonFilePath() {
        return declinedReasonFilePath;
    }

    public void setDeclinedReasonFilePath(String declinedReasonFilePath) {
        this.declinedReasonFilePath = declinedReasonFilePath;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getVerifiedOn() {
        return verifiedOn;
    }

    public void setVerifiedOn(Date verifiedOn) {
        this.verifiedOn = verifiedOn;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setDeclinedImage(String declinedImage) {
        this.declinedImage = declinedImage;
    }

    public String getDeclinedImage() {
        return declinedImage;
    }
}
