package com.user_service.users.ServiceImpl;

import com.user_service.users.Config.JwtUtil;
import com.user_service.users.Dto.LoginDTO;
import com.user_service.users.Dto.UserDTO;
import com.user_service.users.Entity.User;
import com.user_service.users.Repo.UserRepo;
import com.user_service.users.Service.OTPService;
import com.user_service.users.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepo userRepo;
    private final OTPService otpService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

//    public UserServiceImpl (UserRepo userRepo){
//        this.userRepo=userRepo;
//    }

    @Override
    public ResponseEntity<?> saveUserDetails(UserDTO userDTO) {

      /*  if (!otpService.isEmailVerified(userDTO.getUserEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(getResponse(false, "Email not verified. Please verify OTP before registering.", null));
        }*/

        User existingUser = userRepo.getExistingUser(userDTO.getUserEmail(), userDTO.getMobileNo());

        if (existingUser == null) {
            User user = new User();
            user.setUserEmail(userDTO.getUserEmail());
            user.setMobileNo(userDTO.getMobileNo());
            user.setUserName(userDTO.getUserName());
            user.setParentName(userDTO.getParentName());
            user.setGrade(userDTO.getGrade());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            userRepo.save(user);
            otpService.clearVerification(userDTO.getUserEmail()); // consume the verification, one-time use

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(getResponse(true, "user details registered successfully", user));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(getResponse(false, "user details already exist", existingUser));
        }

    }

    private Map<String, Object> getResponse(boolean success, String message, Object data) {

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("status", success ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("data", data);
        return response;
    }

    @Override
    public ResponseEntity<?> loginUser(LoginDTO loginDTO) {

        User user = userRepo.findByUserEmail(loginDTO.getUserEmail());

        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(getResponse(false, "Invalid email or password", null));
        }

        String token = jwtUtil.generateToken(user.getUserEmail());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userName", user.getUserName());
        data.put("userEmail", user.getUserEmail());

        return ResponseEntity.status(HttpStatus.OK)
                .body(getResponse(true, "Login successful", data));
    }
}
