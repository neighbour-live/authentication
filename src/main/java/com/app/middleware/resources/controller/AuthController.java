package com.app.middleware.resources.controller;

import com.app.middleware.exceptions.ExceptionUtil;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.dto.StatusMessageDTO;
import com.app.middleware.persistence.dto.UserDTO;
import com.app.middleware.persistence.mapper.UserMapper;
import com.app.middleware.persistence.request.LoginRequest;
import com.app.middleware.persistence.request.RefreshToken;
import com.app.middleware.persistence.request.SignUpRequest;
import com.app.middleware.persistence.response.GenericResponseEntity;
import com.app.middleware.resources.services.AuthService;
import com.app.middleware.resources.services.LoggingService;
import com.app.middleware.utility.AuthConstants;
import com.app.middleware.utility.StatusCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private LoggingService loggingService;

    @PostMapping("/login")
    @ApiOperation(value = "This operation login the User into application", response = UserDTO.class)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        //create null User for Logging;ß∫
        User user = null;
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        List<String> request = new ArrayList<>();
        request.add(loginRequest.getEmail());
        request.add(req.getRemoteAddr());

        try {
            //Do Login
            user = authService.login(loginRequest);
            //Send Response and save Log
            ResponseEntity response = GenericResponseEntity.create(StatusCode.SUCCESS, UserMapper.createUserDTOLazy(user), HttpStatus.OK);
            loggingService.createLog(user, req.getRemoteAddr(), response, loginRequest);
            return response;

        } catch (Exception e) {
            //Send Response and save Log
            loggingService.createLog(user, req.getRemoteAddr(), e, loginRequest);
            return ExceptionUtil.handleException(e);
        }
    }

    @PostMapping("/refresh-token")
    @ApiOperation(value = "This operation provide Access token using Refresh Token when Access Token was expired.", response = RefreshToken.class)
    public ResponseEntity<?> getAccessTokenByRefreshToken(@RequestBody RefreshToken refreshToken, HttpServletResponse httpServletResponse) throws Exception {
        try {
            //Do Login
            refreshToken = authService.getAccessTokenByRefreshToken(refreshToken, httpServletResponse);
            //Send Response and save Log
            ResponseEntity response = GenericResponseEntity.create(StatusCode.SUCCESS, refreshToken, HttpStatus.OK);
            return response;

        } catch (Exception e) {
            return ExceptionUtil.handleException(e);
        }
    }

    @PostMapping("/register")
    @ApiOperation(value = "This operation register the User into Application.")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws Exception {
        //create null User for Logging;
        User user = null;
        try {
            user = authService.register(signUpRequest);

            //Send Response and save Log
            ResponseEntity response = GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message(AuthConstants.REGISTRATION_SUCCESSFUL)
                    .status(0)
                    .build(), HttpStatus.CREATED);

            loggingService.createLog(user, signUpRequest.getIp(), response, signUpRequest);
            return response;

        } catch (Exception e) {
            //Send Response and save Log
            loggingService.createLog(null, signUpRequest.getIp(), e, signUpRequest);
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/forgot-password")
    @ApiOperation(value = "This operation is used when User forgets his password.")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) throws Exception {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        List<String> request = new ArrayList<>();
        request.add(email);
        request.add(req.getRemoteAddr());



        try {
            authService.forgotPasswordRequest(email);

            ResponseEntity response = GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message(AuthConstants.FORGOT_PASSWORD_VERIFICATION)
                    .status(0)
                    .build(), HttpStatus.CREATED);
            loggingService.createLog(null, req.getRemoteAddr(), email, response);
            return response;

        } catch (Exception e) {
            //Send Response and save Log
            loggingService.createLog(null, req.getRemoteAddr(), request, e);
            return ExceptionUtil.handleException(e);
        }
    }

    @PatchMapping("/change-password")
    @ApiOperation(value = "This operation is used to change Password.")
    public ResponseEntity<?> changePassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword,
                                            @RequestParam("otp") BigInteger otp) throws Exception {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        List<String> request = new ArrayList<>();
        request.add(email);
        request.add(otp.toString());
        request.add(req.getRemoteAddr());
        request.add(String.valueOf(newPassword.hashCode()));

        try {
            authService.changePassword(email, newPassword, otp);
            ResponseEntity response = GenericResponseEntity.create(StatusMessageDTO.builder()
                    .message(AuthConstants.CHANGE_PASSWORD_SUCCESS)
                    .status(0)
                    .build(), HttpStatus.CREATED);
            loggingService.createLog(null, req.getRemoteAddr(), request, response);
            return response;

        } catch (Exception e) {
            //Send Response and save Log
            loggingService.createLog(null, req.getRemoteAddr(), request, e);
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/confirm-email")
    @ApiOperation(value = "This operation is used to confirm User Email.")
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) throws Exception {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        List<String> request = new ArrayList<>();
        request.add(token);
        request.add(req.getRemoteAddr());

        try {
            User user = authService.confirmEmail(token);
            ResponseEntity response = GenericResponseEntity.create(StatusCode.SUCCESS, UserMapper.createUserDTOLazy(user), HttpStatus.OK);
            loggingService.createLog(null, req.getRemoteAddr(), request, response);
            return response;
        } catch (Exception e) {
            //Send Response and save Log
            loggingService.createLog(null, req.getRemoteAddr(), request, e);
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/check-email")
    @ApiOperation(value = "This operation is used to check User Email either it's existing or new.")
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email) throws Exception {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        List<String> request = new ArrayList<>();
        request.add(email);
        request.add(req.getRemoteAddr());

        try {
            Boolean checker = authService.checkEmailExist(email);
            ResponseEntity response = GenericResponseEntity.create(StatusCode.SUCCESS, checker, HttpStatus.OK);
            loggingService.createLog(null, req.getRemoteAddr(), request, response);
            return response;

        } catch (Exception e) {
            //Send Response and save Log
            loggingService.createLog(null, req.getRemoteAddr(), request, e);
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/check-phone")
    @ApiOperation(value = "This operation is used to check User Phone Number either it's existing or new.")
    public ResponseEntity<?> checkPhone(@RequestParam("phone") String phone) throws Exception {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        List<String> request = new ArrayList<>();
        request.add(phone);
        request.add(req.getRemoteAddr());

        try {
            Boolean checker = authService.checkPhoneExist(phone);

            ResponseEntity response = GenericResponseEntity.create(StatusCode.SUCCESS, checker, HttpStatus.OK);
            loggingService.createLog(null, req.getRemoteAddr(), request, response);
            return response;

        } catch (Exception e) {
            //Send Response and save Log
            loggingService.createLog(null, req.getRemoteAddr(), request, e);
            return ExceptionUtil.handleException(e);
        }
    }

    @GetMapping("/check-username")
    @ApiOperation(value = "This operation is used to check username either it's existing or new.")
    public ResponseEntity<?> checkUserName(@RequestParam("username") String username) throws Exception {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        List<String> request = new ArrayList<>();
        request.add(username);
        request.add(req.getRemoteAddr());

        try {
            Boolean checker = authService.checkUserNameExist(username);

            ResponseEntity response = GenericResponseEntity.create(StatusCode.SUCCESS, checker, HttpStatus.OK);
            loggingService.createLog(null, req.getRemoteAddr(), request, response);
            return response;

        } catch (Exception e) {
            //Send Response and save Log
            loggingService.createLog(null, req.getRemoteAddr(), request, e);
            return ExceptionUtil.handleException(e);
        }
    }
}
