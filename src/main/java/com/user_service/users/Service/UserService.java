package com.user_service.users.Service;

import com.user_service.users.Dto.LoginDTO;
import com.user_service.users.Dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ResponseEntity<?> saveUserDetails(UserDTO userDTO);

    ResponseEntity<?> loginUser(LoginDTO loginDTO);
}
