package com.zeed.itbit.apimodels;


import com.zeed.itbit.enums.CardsSearchFilter;
import com.zeed.itbit.enums.Status;

public class CardRequestModel {


    private Integer pageNo = 0;

    private Integer pageSize = 15;

    private CardsSearchFilter cardsSearchFilter;

    private Long from;

    private Long to;

    private Status status;

    private String username;

    private Long id;

    private String declineReason;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public CardsSearchFilter getCardsSearchFilter() {
        return cardsSearchFilter;
    }

    public void setCardsSearchFilter(CardsSearchFilter cardsSearchFilter) {
        this.cardsSearchFilter = cardsSearchFilter;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }
}
