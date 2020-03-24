package com.zeed.itbit.repository;

import com.zeed.itbit.models.BitCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by zeed on 11/22/17.
 */
@Repository
public interface BitCoinRepository extends JpaRepository<BitCoin,Long>, JpaSpecificationExecutor {

    BitCoin findTopByIdAndUser_Username(Long id, String username);

}
