package com.zeed.itbit.repository;

import com.zeed.itbit.models.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthClientDetailsRepository extends JpaRepository<OauthClientDetails,Long> {

    OauthClientDetails findOneByClientId(String clientId);
}
