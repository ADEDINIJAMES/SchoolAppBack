package com.tumtech.schoolApp.serviceImp;

import com.tumtech.schoolApp.dto.LoginRequest;
import com.tumtech.schoolApp.exception.UserNotFoundException;
import com.tumtech.schoolApp.model.Users;
import com.tumtech.schoolApp.repository.UserRepository;
import com.tumtech.schoolApp.response.ApiResponse;
import com.tumtech.schoolApp.service.AuthService;
import com.tumtech.schoolApp.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImp  implements AuthService {
private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
@Autowired
    public AuthServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ApiResponse login(LoginRequest loginRequest) {
        if(loginRequest!=null){
            String email =loginRequest.getUsername();
            String rawPassword = loginRequest.getPassword();
            Optional<Users> users = userRepository.findByEmail(email);
            if(users.isPresent()){
                String encodedPassword= users.get().getPassword();
                if(checkPassword(rawPassword,encodedPassword)){
                    String tokenGenerated= jwtUtil.createJwt.apply(users.get());
                    return getApiResponse(tokenGenerated);

                }
//                return ApiResponse.builder()
//                        .message("Password or Username incorrect")
//                        .statusCode(403)
//                        .data(null)
//                        .build();

                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setMessage("Password or Username incorrect");
                apiResponse.setData(null);
                apiResponse.setStatusCode(403);
                return apiResponse;

            }
//            return ApiResponse.builder()
//                    .message("User not found")
//                    .statusCode(404)
//                    .data(null)
//                    .build();

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("User not found");
            apiResponse.setData(null);
            apiResponse.setStatusCode(404);
            return apiResponse;
        }

//        return ApiResponse.builder()
//                .message("Login Details needed")
//                .statusCode(403)
//                .data(null)
//                .build();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Login Details needed");
        apiResponse.setData(null);
        apiResponse.setStatusCode(403);
        return apiResponse;


    }

    private static ApiResponse getApiResponse(String tokenGenerated) {
        Map<String, Object> myData= new HashMap<>();
        myData.put("AccessToken", tokenGenerated);
//                    return ApiResponse.builder()
//                            .message("Login Successful")
//                            .statusCode(200)
//                            .data(myData)
//                            .build();

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Login Successful");
        apiResponse.setData(myData);
        apiResponse.setStatusCode(200);
        return apiResponse;
    }

    private boolean checkPassword (String rawPassword, String dBPassword ){
        return passwordEncoder.matches(rawPassword,dBPassword);
    }


    @Override
    public ApiResponse adminLogin(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public ApiResponse adminRegistration(MultipartFile file, Authentication authentication) {
        return null;
    }

    @Override
    public ApiResponse logout(Authentication authentication, HttpServletRequest request) {
        authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            SecurityContextHolder.clearContext();
            SecurityContextHolder.getContext().setAuthentication(null);
            request.getSession().invalidate();
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setData(null);
            apiResponse.setStatusCode(200);
            apiResponse.setMessage("Logout Successful");
            return apiResponse;
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(null);
        apiResponse.setStatusCode(403);
        apiResponse.setMessage("User not Logged In");
        return apiResponse;

}

//    @Override
//    public ApiResponse RegisterUser(UserDto userDto, Users user) {
//        return null;
//    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("user not found"));
    }
}
