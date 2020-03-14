package com.zeed.assignment.sms.repository;

import com.zeed.assignment.sms.enums.Status;
import com.zeed.assignment.sms.models.Cards;
import com.zeed.assignment.sms.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zeed on 11/22/17.
 */
@Repository
public interface CardsRepository extends JpaRepository<Cards,Long>, JpaSpecificationExecutor {

    List<Cards> findCardsByUserOrderByUploadedOnDesc(User user);

    List<Cards> findCardsByStatus(Status status);

    Cards findCardsById(Long id);

    Cards findCardsByIdAndUser_Username(Long id, String username);

    List<Cards> findCardsByIdGreaterThan(Long last);

    List<Cards> findAllByUser_UsernameAndStatus(String username, Status status);

    List<Cards> findAllByUser_Username(String username, Pageable pageable);

}
