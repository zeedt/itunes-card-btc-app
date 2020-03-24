package com.zeed.itbit.models;

import com.zeed.itbit.enums.Status;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zeed on 11/22/17.
 */
@Entity
public class Cards {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    private String amount;

    @Lob
    private String filePath;

    @ManyToOne
    private User user;

    private Date uploadedOn;

    private Date verifiedOn;

    @Lob
    private String comment;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Lob
    private String declinedImage;

    @Transient
    private String base64Image;

    public Cards(String description, String amount, String filePath, User user,Date uploadedOn,Date verifiedOn,Status status) {
        this.description = description;
        this.amount = amount;
        this.filePath = filePath;
        this.user = user;
        this.uploadedOn = uploadedOn;
        this.status = status;
    }

    public Cards(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(Date uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

    public Date getVerifiedOn() {
        return verifiedOn;
    }

    public void setVerifiedOn(Date verifiedOn) {
        this.verifiedOn = verifiedOn;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDeclinedImage() {
        return declinedImage;
    }

    public void setDeclinedImage(String declinedImage) {
        this.declinedImage = declinedImage;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
