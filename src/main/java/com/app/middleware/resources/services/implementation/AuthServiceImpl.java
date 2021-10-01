package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserAddress;
import com.app.middleware.persistence.domain.UserTemporary;
import com.app.middleware.persistence.dto.EmailNotificationDto;
import com.app.middleware.persistence.repository.RoleRepository;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.request.*;
import com.app.middleware.persistence.type.*;
import com.app.middleware.resources.services.*;
import com.app.middleware.security.TokenProvider;
import com.app.middleware.utility.AuthConstants;
import com.app.middleware.utility.Constants;
import com.app.middleware.utility.ObjectUtils;
import com.app.middleware.utility.Utility;
import com.app.middleware.utility.id.PublicIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private UserTemporaryService userTemporaryService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${email.from}")
    private String EMAIL_FROM;

    @Value("${email.redirection}")
    private String EMAIL_REDIRECTION_URL;

    @Value("${logo-url}")
    private String LOGO_URL;

    @Value("${otp-expiry-time}")
    private String OTP_EXPIRY_TIME;

    @Value("${email-token-expiry-time}")
    private String EMAIL_TOKEN_EXPIRY_TIME;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User login(LoginRequest loginRequest) throws Exception {
        User user = new User();
        if((loginRequest.isPhoneLogin() && loginRequest.isUserNameLogin()) || (loginRequest.isPhoneLogin() && loginRequest.isEmailLogin())
                || (loginRequest.isUserNameLogin() && loginRequest.isEmailLogin()))
        {
            throw new Exception("cannot login!");
        }

        if(loginRequest.isUserNameLogin()){
            user = userRepository.findByUserName(loginRequest.getUsername());
            if(user==null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, loginRequest.getUsername());
        } else if(loginRequest.isEmailLogin()){
            user = userRepository.findByEmailIgnoreCase(loginRequest.getEmail().toLowerCase());
            if(user==null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, loginRequest.getEmail());
        } else if(loginRequest.isPhoneLogin()){
            user = userRepository.findByPhoneNumber(loginRequest.getPhone());
            if(user==null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, loginRequest.getPhone());
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        loginRequest.getPassword()
                )
        );

        List<String> permissions = new ArrayList<>();
        String roleType = "";
        if(!ObjectUtils.isNull(user.getRole())){
            roleType = user.getRole().getRoleType().toString();
            user.getRole().getRolePermissions().forEach(rolePermission -> {
                permissions.add(rolePermission.getPermission().getPermissionCode());
            });
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication, permissions, roleType);
        if(!ObjectUtils.isNull(token)){
            user.setAccessToken(token);
            user.setRefreshToken(tokenProvider.createRefreshToken(user.getId(), permissions, roleType));
            user.setFirebaseKey(loginRequest.getFirebaseKey());
            user = userRepository.save(user);
            return user;
        }
        throw new Exception("cannot login!");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User register(SignUpRequest signUpRequest) throws Exception {

        User user = new User();

        UserTemporary userTemporary = userTemporaryService.findByPublicId(user.getPublicId());

        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail().toLowerCase());
        user.setUserName(signUpRequest.getUserName());
        user.setProvider(AuthProvider.valueOf(signUpRequest.getProvider()));
        user.setImageUrl(signUpRequest.getImageUrl());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setPublicId(PublicIdGenerator.generatePublicId());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setFirebaseKey(signUpRequest.getFirebaseKey());


        user.setAddressLine(signUpRequest.getAddressLine());
        user.setApartmentAddress(signUpRequest.getApartmentAddress());
        user.setIp(signUpRequest.getIp());
        user.setDob(signUpRequest.getDob());
        user.setPostalCode(signUpRequest.getPostalCode());
        user.setCity(signUpRequest.getCity());
        user.setState(signUpRequest.getState());
        user.setCountry(signUpRequest.getCountry());
        user.setAddressType(AddressType.PERMANENT.toString());
        user.setLat(signUpRequest.getLat());
        user.setLng(signUpRequest.getLng());


        //flags
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setIsSuspended(false);
        // if fb or gg id is present
        if(!signUpRequest.getFbId().equals(null) && !signUpRequest.getFbId().isEmpty() ) {
            user.setFbId(signUpRequest.getFbId());
            user.setEmail(signUpRequest.getFbId() + "@fb.com");

        } else if (!signUpRequest.getGgId().equals(null) && !signUpRequest.getGgId().isEmpty())
        {
            user.setGgId(signUpRequest.getGgId());
            user.setEmail(signUpRequest.getGgId());
        }

        user.setEmailVerified(signUpRequest.isEmailVerified());
        user.setPhoneVerified(signUpRequest.isPhoneVerified());
        user.setRole(roleRepository.findByRoleType(RoleType.USER));

        UserAddress userAddress = UserAddress.builder()
                .publicId(PublicIdGenerator.generatePublicId())
                .addressType(AddressType.PERMANENT.toString())
                .apartmentAddress(user.getApartmentAddress())
                .addressLine(user.getAddressLine())
                .lat(user.getLat())
                .lng(user.getLng())
                .country(user.getCountry())
                .state(user.getState())
                .postalCode(user.getPostalCode())
                .city(user.getCity())
                .user(user)
                .build();


        user = userRepository.save(user);
        userAddress = userAddressService.saveAddress(userAddress);

        //sending Welcome Email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Welcome to Neighbour Live! ");
        mailMessage.setFrom(EMAIL_FROM);
        mailMessage.setText("Welcome " + user.getFirstName() + " " + user.getLastName() + "\n"
                +"We really Appreciate the valuable addition of you into our growing community. \n");

        emailService.sendEmail(mailMessage);
//            EmailNotificationDto emailNotificationDto = EmailNotificationDto.builder()
//                    .to(user.getEmail())
//                    .template(Constants.EmailTemplate.WELCOME_TEMPLATE.value())
//                    .build();
//            emailService.sendEmailFromExternalApi(emailNotificationDto);
//
//            sendEmailVerification(user.getEmail(), user);

//            Map<String,String> actionsInfo =  new HashMap<>();
//            actionsInfo.put("userPublicId", PublicIdGenerator.encodedPublicId(user.getPublicId()));
//
//            UserNotificationRequest userNotificationRequest = UserNotificationRequest.builder()
//                    .notificationType(NotificationEnum.VERIFICATION.toString())
//                    .target("INDIVIDUAL")
//                    .title("Email Verification!")
//                    .body("Verify your email and be noticeable to the Neighbour community.!")
//                    .imageUrl(LOGO_URL)
//                    .actions(NotificationAction.HOME.name())
//                    .actionsInfo(actionsInfo)
//                    .firebaseKey(user.getFirebaseKey())
//                    .build();
//
//            notificationService.postUserNotification(userNotificationRequest, user);
//
//            userNotificationRequest = UserNotificationRequest.builder()
//                    .notificationType(NotificationEnum.VERIFICATION.toString())
//                    .target("INDIVIDUAL")
//                    .title("Phone Verification!")
//                    .body("Verify your phone number and be noticeable to the Neighbour community.!")
//                    .imageUrl(LOGO_URL)
//                    .actions(NotificationAction.HOME.name())
//                    .actionsInfo(actionsInfo)
//                    .firebaseKey(user.getFirebaseKey())
//                    .build();
//
//            notificationService.postUserNotification(userNotificationRequest, user);
//
//            userNotificationRequest = UserNotificationRequest.builder()
//                    .notificationType(NotificationEnum.VERIFICATION.toString())
//                    .target("INDIVIDUAL")
//                    .title("Wallet Verification!")
//                    .body("Verify your Wallet information and be noticeable to the Neighbour community.!")
//                    .imageUrl(LOGO_URL)
//                    .actions(NotificationAction.HOME.name())
//                    .actionsInfo(actionsInfo)
//                    .firebaseKey(user.getFirebaseKey())
//                    .build();
//
//            notificationService.postUserNotification(userNotificationRequest, user);
//
//            userNotificationRequest = UserNotificationRequest.builder()
//                    .notificationType(NotificationEnum.VERIFICATION.toString())
//                    .target("INDIVIDUAL")
//                    .title("Background Verification!")
//                    .body("Get a background check by Sterling, our 3rd Party Partner to stand out among other users.!")
//                    .imageUrl(LOGO_URL)
//                    .actions(NotificationAction.HOME.name())
//                    .actionsInfo(actionsInfo)
//                    .firebaseKey(user.getFirebaseKey())
//                    .build();
//
//            notificationService.postUserNotification(userNotificationRequest, user);


        return user;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserTemporary sendEmailCodePreRegister(String email, User user) throws ResourceNotFoundException, IOException {

        UserTemporary userTemporary = userTemporaryService.findByUser(user);
        if(userTemporary == null) {
            userTemporary = userTemporaryService.createTempUser(user);
        }

        String token = Utility.generateSafeToken() + UUID.randomUUID();
        String otp = "0000";
        userTemporary.setEmail(email);
        userTemporary.setEmailCode(otp);
        userTemporary.setEmailToken(token);
        userTemporary = userTemporaryService.save(userTemporary);

        //sending Welcome Email

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Neighbour Live | Email Verification! ");
        mailMessage.setFrom(EMAIL_FROM);
        mailMessage.setText("Welcome " +user.getFirstName()+" "+user.getLastName()+ "\n"
                +"To verify your email, please use the following code: "+ otp);

        return userTemporary;
    }

    @Override
    public UserTemporary confirmEmailPreRegister(String email, String emailToken, String emailCode) throws Exception {
        UserTemporary userTemporary = userTemporaryService.findByEmailIgnoreCase(email);
        if(userTemporary == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, email);
        if(userTemporary.getEmailToken().equals(emailToken) && userTemporary.getEmailCode().equals(emailCode))
        {
            //check if OTP is expired
            ZonedDateTime timeNow = ZonedDateTime.now();
            if(userTemporary.getUpdateDateTime().plusSeconds(Long.parseLong(OTP_EXPIRY_TIME)).isBefore(timeNow)){
                throw new Exception("Email Code expired, please try again.");
            }

            userTemporary.setEmailToken(null);
            userTemporary.setEmailCode(null);
            userTemporary.setEmailVerified(true);
            userTemporary = userTemporaryService.save(userTemporary);
            return userTemporary;
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean sendEmailVerification(String email, User user) throws ResourceNotFoundException, IOException {

        UserTemporary userTemporary = userTemporaryService.findByUser(user);
        if(userTemporary == null) {
            userTemporary = userTemporaryService.createTempUser(user);
        }

        userTemporary.setEmail(user.getEmail());
        userTemporary = userTemporaryService.save(userTemporary);

        String token = Utility.generateSafeToken() + UUID.randomUUID();

        token = "0000";
        user.setEmailVerificationToken(token);
        user = userRepository.save(user);

        //sending Welcome Email

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Neighbour Live | Email Verification! ");
        mailMessage.setFrom(EMAIL_FROM);
        mailMessage.setText("Welcome " +user.getFirstName()+" "+user.getLastName()+ "\n"
                +"To verify your email, please use the following code: "+ token);
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User confirmEmail(String token) throws Exception {
        User user = userRepository.findByEmailVerificationToken(token);
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, token);
        if(user.getEmailVerificationToken().equals(token))
        {
            //check if OTP is expired
            ZonedDateTime timeNow = ZonedDateTime.now();
            if(user.getEmailTokenCreationTime().plusSeconds(Long.parseLong(EMAIL_TOKEN_EXPIRY_TIME)).isBefore(timeNow)){
                throw new Exception("Token expired, please try again.");
            }

            user.setEmailVerificationToken(null);
            user.setEmailVerified(true);
            userRepository.save(user);

            UserTemporary userTemporary =  userTemporaryService.findByEmailIgnoreCase(user.getEmail());
            userTemporary.setEmail(null);
            userTemporaryService.save(userTemporary);

            return user;
        }

        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserTemporary sendPhoneCodePreRegister(String phoneNumber, User user) throws Exception {

        UserTemporary userTemporary = userTemporaryService.findByUser(user);
        if(userTemporary == null) {
            userTemporary = userTemporaryService.createTempUser(user);
        }
        userTemporary.setPhoneNumber(phoneNumber);

        String otp = Utility.generateOTP();
        otp = "0000"; //temporary
        String token = Utility.generateSafeToken() + UUID.randomUUID();
        userTemporary.setPhoneCode(otp);
        userTemporary.setPhoneToken(token);
        userTemporary = userTemporaryService.save(userTemporary);
        smsService.sendOTPMessage(otp, phoneNumber);
        return userTemporary;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public String sendPhoneVerificationOTP(String phoneNumber, User user) throws Exception {

        UserTemporary userTemporary = userTemporaryService.findByUser(user);
        if(userTemporary == null) {
            userTemporary = userTemporaryService.createTempUser(user);
        }
        userTemporary.setPhoneNumber(user.getPhoneNumber());
        userTemporary = userTemporaryService.save(userTemporary);

        if(user.getPhoneNumber().equals(phoneNumber)){
            String otp = Utility.generateOTP();
            otp = "0000"; //temporary
            String token = Utility.generateSafeToken() + UUID.randomUUID();
            user.setPhoneVerificationToken(token);
            user.setPhoneVerificationOTP(otp);
            user = userRepository.save(user);
            smsService.sendOTPMessage(otp, phoneNumber);
            return user.getPhoneVerificationToken();
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserTemporary confirmPhonePreRegister(String phoneNumber, String token, String otp) throws Exception {
        UserTemporary userTemporary = userTemporaryService.findByPhoneNumber(phoneNumber);
        if(userTemporary == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, phoneNumber);
        if(userTemporary.getPhoneToken().equals(token) && userTemporary.getPhoneCode().equals(otp))
        {
            //check if OTP is expired
            ZonedDateTime timeNow = ZonedDateTime.now();
            if(userTemporary.getUpdateDateTime().plusSeconds(Long.parseLong(OTP_EXPIRY_TIME)).isBefore(timeNow)){
                throw new Exception("Phone Code expired, please try again.");
            }

            userTemporary.setPhoneToken(null);
            userTemporary.setPhoneCode(null);
            userTemporary.setPhoneVerified(true);
            userTemporary = userTemporaryService.save(userTemporary);
            return userTemporary;
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User confirmPhoneNumber(String token, String otp) throws Exception {
        User user = userRepository.findByPhoneVerificationToken(token);
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, otp);
        if(user.getPhoneVerificationToken().equals(token) && user.getPhoneVerificationOTP().equals(otp))
        {
            //check if OTP is expired
            ZonedDateTime timeNow = ZonedDateTime.now();
            if(user.getOtpCreationTime().plusSeconds(Long.parseLong(OTP_EXPIRY_TIME)).isBefore(timeNow)){
                throw new Exception("OTP expired, please try again.");
            }

            user.setPhoneVerificationOTP(null);
            user.setPhoneVerificationToken(null);
            user.setPhoneVerified(true);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    @Override
    public HashMap<Object, Boolean> getUserVerificationStatus(String publicId) throws ResourceNotFoundException {
        User user = userRepository.findByPublicId(PublicIdGenerator.decodePublicId(publicId));
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID, publicId);

        HashMap<Object, Boolean> verificationMap = new HashMap<Object, Boolean>();
        verificationMap.put("emailVerification",user.getEmailVerified());
        verificationMap.put("phoneVerification", user.getPhoneVerified());
        return verificationMap;
    }

    @Override
    public Boolean checkEmailExist(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent() == false){
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkPhoneExist(String phone) {
        User user = userRepository.findByPhoneNumber(phone);
        if(user == null){
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkUserNameExist(String userName) {
        User user = userRepository.findByUserName(userName);
        if(user == null){
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public String editProfile(EditProfileRequest editProfileRequest, User user) throws Exception {
        UserTemporary userTemporary = userTemporaryService.findByUser(user);
        if(userTemporary == null) {
            userTemporary = userTemporaryService.createTempUser(user);
        }
        //Only update personal details if requested
        if(editProfileRequest.getUpdatePersonalDetails() == 1){
            user.setFirstName(editProfileRequest.getFirstName());
            user.setLastName(editProfileRequest.getLastName());
            user.setImageUrl(editProfileRequest.getImageUrl());
            user.setDob(editProfileRequest.getDob());
        }

        //Only update Phone number if requested
        if(editProfileRequest.getUpdatePhoneNumber() == 1 && !user.getPhoneNumber().equals(editProfileRequest.getPhoneNumber())){
            User u = userRepository.findByPhoneNumber(editProfileRequest.getPhoneNumber());
            if(u == null){
                user.setPhoneVerified(false);
                user.setPhoneVerificationToken(null);
                user.setPhoneVerificationOTP(null);
                user.setPhoneNumber(editProfileRequest.getPhoneNumber());
            } else throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_ALREADY_EXIST_WITH_PHONE);

            String response = AuthConstants.VERIFICATION_OTP_SENT + sendPhoneVerificationOTP(user.getPhoneNumber(), user);
            return response;
        }

        //Only Update email if requested
        if(editProfileRequest.getUpdateEmail() == 1 && user.getEmail().equals(editProfileRequest.getEmail())){
            Optional<User> u = userRepository.findByEmail(editProfileRequest.getEmail());
            if(u.isPresent() == false){
                user.setEmailVerificationToken(null);
                user.setEmailVerified(false);
                user.setEmail(editProfileRequest.getEmail());
            } else throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_ALREADY_EXIST_WITH_EMAIL);

            sendEmailVerification(user.getEmail(), user);

            String response = AuthConstants.VERIFICATION_EMAIL_SENT;
            return response;
        }

        //Only Update address if requested
        if(editProfileRequest.getUpdateAddressDetails() == 1 &&  (!user.getApartmentAddress().equals(editProfileRequest.getApartmentAddress()) || !user.getAddressLine().equals(editProfileRequest.getAddressLine()))){

            if(editProfileRequest.getAddressLine() == null || editProfileRequest.getApartmentAddress() == null || editProfileRequest.getIp() == null || editProfileRequest.getDob() == null
                || editProfileRequest.getPostalCode() == null || editProfileRequest.getCity() == null || editProfileRequest.getState() == null || editProfileRequest.getCountry() == null
                    || editProfileRequest.getLat() <= 0.0 || editProfileRequest.getLng() <= 0.0){
                throw new Exception("Cannot add user address with provided details");
            }

            user.setAddressLine(editProfileRequest.getAddressLine());
            user.setApartmentAddress(editProfileRequest.getApartmentAddress());
            user.setIp(editProfileRequest.getIp());
            user.setDob(editProfileRequest.getDob());
            user.setPostalCode(editProfileRequest.getPostalCode());
            user.setCity(editProfileRequest.getCity());
            user.setState(editProfileRequest.getState());
            user.setCountry(editProfileRequest.getCountry());
            user.setAddressType(AddressType.PERMANENT.toString());
            user.setLat(editProfileRequest.getLat());
            user.setLng(editProfileRequest.getLng());

            UserAddress userAddress = userAddressService.getByUser(user);
            if(userAddress == null) {
                UserAddress newUserAddress = UserAddress.builder()
                        .publicId(PublicIdGenerator.generatePublicId())
                        .addressType(AddressType.PERMANENT.toString())
                        .apartmentAddress(user.getApartmentAddress())
                        .addressLine(user.getAddressLine())
                        .lat(user.getLat())
                        .lng(user.getLng())
                        .country(user.getCountry())
                        .state(user.getState())
                        .postalCode(user.getPostalCode())
                        .city(user.getCity())
                        .user(user)
                        .build();
                userAddressService.saveAddress(newUserAddress);
            } else {
                userAddress.setAddressLine(editProfileRequest.getAddressLine());
                userAddress.setAddressType(AddressType.PERMANENT.toString());
                userAddress.setApartmentAddress(editProfileRequest.getApartmentAddress());
                userAddress.setIsDeleted(false);
                userAddress.setLat(editProfileRequest.getLat());
                userAddress.setLng(editProfileRequest.getLng());
                userAddress.setUser(user);
                userAddressService.saveAddress(userAddress);
            }
        }


        //Only Update password if requested
        if(editProfileRequest.getUpdatePassword() == 1 && !editProfileRequest.getPassword().equals(null)){
            if(user.getProvider().equals(AuthProvider.LOCAL)){
                if(passwordEncoder.encode(editProfileRequest.getPassword()).equals(user.getPassword())){
                    throw new Exception("New password is similar to last password, try something else for change");
                }

                userTemporary.setPassword(user.getPassword());
                user.setPassword(passwordEncoder.encode(editProfileRequest.getPassword()));

            } else{
                userTemporary.setPassword(user.getPassword());
                user.setPassword(passwordEncoder.encode(editProfileRequest.getPassword()));
                user.setProvider(AuthProvider.LOCAL);
                user.setProviderId(null);
            }

            String response = AuthConstants.CHANGE_PASSWORD_SUCCESS;
            return response;
        }

        //saving user
        user = userRepository.save(user);

        userTemporary = userTemporaryService.save(userTemporary);
        //saving user's default address
        userRepository.save(user);

        String response = AuthConstants.EDIT_PROFILE_SUCCESSFUL;
        return response;
    }

    @Override
    public boolean forgotPasswordRequest(String email) throws Exception {
        if(checkEmailExist(email)){
            User user  = userRepository.findUserByEmail(email);
            if(!user.getProvider().equals(AuthProvider.LOCAL)){
                throw new Exception("Cannot change your "+user.getProvider().toString()+" password using Neighbour.");
            }
            String otp = Utility.generateOTP();

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Neighbour | Forgot Password Verification!");
            mailMessage.setFrom(EMAIL_FROM);
            mailMessage.setText("Hi " +user.getFirstName()+" "+user.getLastName()+ "\n"
                    +"To change your password, please send us this following code with your change password request: \n"
                    +"One Time Secret: " + otp + "\n\n\n\n Regards,\nTeam Neighbour");

            user.setPhoneVerificationOTP(otp);
            userRepository.save(user);
            emailService.sendEmail(mailMessage);
            smsService.sendOTPMessage(otp, user.getPhoneNumber());
            return true;
        } else throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH);
    }

    @Override
    public boolean changePassword(String email, String newPassword, BigInteger otp) throws Exception {
        if(checkEmailExist(email)){
            User user  = userRepository.findUserByEmail(email);

            if(!user.getProvider().equals(AuthProvider.LOCAL)){
                throw new Exception("Cannot change your "+user.getProvider().toString()+" password using Neighbour.");
            }

            if(passwordEncoder.matches(newPassword, user.getPassword())){
                throw new Exception("New password must be different then older password");
            }

            if(user.getPhoneVerificationOTP().equals(String.valueOf(otp)) && !passwordEncoder.matches(newPassword, user.getPassword())){
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Neighbour | Change Password Successfully!");
                mailMessage.setFrom(EMAIL_FROM);
                mailMessage.setText("Hi " +user.getFirstName()+" "+user.getLastName()+ "\n"
                        +"Your password has been changed successfully."
                        +"\n\n\n\n Regards,\nTeam Neighbour");

                user.setPhoneVerificationOTP(null);
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                emailService.sendEmail(mailMessage);
                return true;
            } else {
                throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_OTP);
            }


        } else throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH);
    }

    @Override
    public int calculateProfileCompletionPercentage(User user) throws ResourceNotFoundException {
        int profileCompletionPercentage = 0;

        //If Valid user - score +20
        if(user.getProvider().equals(AuthProvider.GOOGLE) || user.getProvider().equals(AuthProvider.LOCAL) || user.getProvider().equals(AuthProvider.FACEBOOK)){
            profileCompletionPercentage += 40;
        }

        //If Email Verified - score +20
        if(user.getEmailVerified()){
            profileCompletionPercentage += 20;
        }

        //If Phone Verified - score +15
        if(user.getPhoneVerified()){
            profileCompletionPercentage += 20;
        }

        //If address added - score +10
        if(!user.getAddressLine().equals(null) && !user.getApartmentAddress().equals(null)){
            profileCompletionPercentage += 20;
        }
        return profileCompletionPercentage;
    }

    @Override
    public boolean logout(User user) {
        user.setAccessToken(null);
        user.setRefreshToken(null);
        userRepository.save(user);
        return true;
    }

    @Override
    public RefreshToken getAccessTokenByRefreshToken(RefreshToken refreshToken, HttpServletResponse httpServletResponse) throws Exception {
        RefreshToken refreshTokenDto = null;
        if(!ObjectUtils.isNull(refreshToken) && tokenProvider.validateToken(refreshToken.getRefreshToken())){
            refreshTokenDto = new RefreshToken();
            List<String> permissions = new ArrayList<>();
            String roleType = "";
            Long userId = tokenProvider.getUserIdFromToken(refreshToken.getRefreshToken());
            Optional<User> user = userRepository.findById(userId);
            if(user.isPresent() && !ObjectUtils.isNull(user.get().getRole())){
                roleType = user.get().getRole().getRoleType().toString();
                user.get().getRole().getRolePermissions().forEach(rolePermission -> {
                    permissions.add(rolePermission.getPermission().getPermissionCode());
                });
            }

            refreshTokenDto.setToken(tokenProvider.createAccessTokenByRefreshToken(userId, permissions, roleType));
        } else{
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized");
        }
        return refreshTokenDto;
    }


}
