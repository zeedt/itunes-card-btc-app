package com.zeed.itbit.repository;


import com.zeed.itbit.enums.Role;
import com.zeed.itbit.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by zeed on 11/13/17.
 */
@Repository
public interface UserRepositoy  extends PagingAndSortingRepository<User,Long>{

    User findByUsernameAndRole(String username, Role role);

    User findByUsername(String username);

}
