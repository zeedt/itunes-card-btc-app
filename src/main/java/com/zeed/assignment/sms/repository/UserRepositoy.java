package com.zeed.assignment.sms.repository;


import com.zeed.assignment.sms.enums.Role;
import com.zeed.assignment.sms.models.User;
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
