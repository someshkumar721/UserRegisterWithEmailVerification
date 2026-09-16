package com.user_service.users.Repo;

import com.user_service.users.Dto.UserDTO;
import com.user_service.users.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    @Query(nativeQuery = true,value = "select * from edu_tech_users.edu_users where user_email=:email and mobile_no=:mobileNo;")
    User getExistingUser(String email, String mobileNo);
}
