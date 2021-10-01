package com.app.middleware.resources.services;


import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserTemporary;
import com.app.middleware.persistence.request.EditProfileRequest;
import com.app.middleware.persistence.request.LoginRequest;
import com.app.middleware.persistence.request.RefreshToken;
import com.app.middleware.persistence.request.SignUpRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

public interface AuthService {

    User login(LoginRequest loginRequest) throws Exception;

    User register(SignUpRequest signUpRequest) throws Exception;

    User confirmEmail(String emailVerificationToken) throws Exception;

    boolean sendEmailVerification(String email, User user) throws ResourceNotFoundException, IOException;

    UserTemporary sendEmailCodePreRegister(String email, User user) throws ResourceNotFoundException, IOException;

    UserTemporary confirmEmailPreRegister(String email, String emailToken, String emailCode) throws Exception;

    String sendPhoneVerificationOTP(String phoneNumber, User user) throws Exception;

    UserTemporary sendPhoneCodePreRegister(String phoneNumber, User user) throws Exception;

    User confirmPhoneNumber(String token, String otp) throws Exception;

    UserTemporary confirmPhonePreRegister(String phoneNumber, String token, String otp) throws Exception;

    HashMap<Object, Boolean> getUserVerificationStatus(String publicId) throws ResourceNotFoundException;

    Boolean checkEmailExist(String email);

    Boolean checkPhoneExist(String phone);

    String editProfile(EditProfileRequest editProfileRequest, User user) throws Exception;

    boolean forgotPasswordRequest(String email) throws Exception;

    boolean changePassword(String email, String newPassword, BigInteger otp) throws Exception;

    int calculateProfileCompletionPercentage(User user) throws ResourceNotFoundException;

    boolean logout(User user);

    RefreshToken getAccessTokenByRefreshToken(RefreshToken refreshToken, HttpServletResponse httpServletResponse) throws Exception;

    Boolean checkUserNameExist(String userName);

    UserTemporary confirmUserNamePreRegister(String userName, String publicId) throws Exception;
}
