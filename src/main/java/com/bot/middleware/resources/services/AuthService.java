package com.bot.middleware.resources.services;


import com.bot.middleware.exceptions.type.ResourceNotFoundException;
import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.request.EditProfileRequest;
import com.bot.middleware.persistence.request.LoginRequest;
import com.bot.middleware.persistence.request.RefreshToken;
import com.bot.middleware.persistence.request.SignUpRequest;
import com.stripe.exception.StripeException;
import org.apache.tomcat.util.json.ParseException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

public interface AuthService {

    User login(LoginRequest loginRequest) throws Exception;

    User register(SignUpRequest signUpRequest) throws StripeException, ParseException;

    User confirmEmail(String emailVerificationToken) throws Exception;

    boolean sendEmailVerification(String email, User user) throws ResourceNotFoundException, IOException;

    String sendPhoneVerificationOTP(String phoneNumber, User user) throws Exception;

    User confirmPhoneNumber(String token, String otp) throws Exception;

    HashMap<Object, Boolean> getUserVerificationStatus(String publicId) throws ResourceNotFoundException;

    Boolean checkEmailExist(String email);

    Boolean checkPhoneExist(String phone);

    String editProfile(EditProfileRequest editProfileRequest, User user) throws Exception;

    boolean forgotPasswordRequest(String email) throws Exception;

    boolean changePassword(String email, String newPassword, BigInteger otp) throws Exception;

    int calculateProfileCompletionPercentage(User user) throws ResourceNotFoundException;

    boolean logout(User user);

    RefreshToken getAccessTokenByRefreshToken(RefreshToken refreshToken, HttpServletResponse httpServletResponse) throws Exception;
}
