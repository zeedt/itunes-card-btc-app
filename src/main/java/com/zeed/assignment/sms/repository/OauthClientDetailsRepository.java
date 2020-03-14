package com.zeed.assignment.sms.repository;

import com.zeed.assignment.sms.models.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails,Long> {

    OauthClientDetails findOneByClientId(String clientId);
}
