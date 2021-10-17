package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.ResourceNotFoundException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserAddress;
import com.app.middleware.persistence.domain.UserTemporary;
import com.app.middleware.persistence.repository.RoleRepository;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.request.*;
import com.app.middleware.persistence.type.*;
import com.app.middleware.resources.services.*;
import com.app.middleware.security.TokenProvider;
import com.app.middleware.utility.AuthConstants;
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
        UserTemporary userTemporary = userTemporaryService.findByPublicId(PublicIdGenerator.decodePublicId(signUpRequest.getPublicId()));

        user.setPublicId(userTemporary.getPublicId());
        user.setPhoneNumber(userTemporary.getPhoneNumber());
        user.setUserName(userTemporary.getUserName());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        if(AuthProvider.valueOf(signUpRequest.getProvider()).equals(AuthProvider.LOCAL)){

            user.setProvider(AuthProvider.LOCAL);
            if(!userTemporary.getEmail().isEmpty() && userTemporary.getEmail() !=null){
                user.setEmail(userTemporary.getEmail());
                user.setFbId("");
                user.setGgId("");
            } else throw new Exception("For LOCAL Provider a valid email is required");

            if(userTemporary.getEmailVerified() && userTemporary.getPhoneVerified()){
                user.setPhoneVerified(userTemporary.getPhoneVerified());
                user.setEmailVerified(userTemporary.getEmailVerified());
            } else throw new Exception("For LOCAL Provider a verified email and phone number is required");

        } else if(AuthProvider.valueOf(signUpRequest.getProvider()).equals(AuthProvider.FACEBOOK)) {

            user.setProvider(AuthProvider.FACEBOOK);
            user.setGgId("");
            if(!signUpRequest.getFbId().isEmpty() && signUpRequest.getFbId() !=null){
                user.setFbId(signUpRequest.getFbId());
                user.setEmail(signUpRequest.getFbId() + "@fb.com");
            } else throw new Exception("For FACEBOOK Provider fbId is required");

            if(userTemporary.getPhoneVerified()){
                user.setPhoneVerified(userTemporary.getPhoneVerified());
                user.setEmailVerified(true);
            } else throw new Exception("For FACEBOOK Provider a verified phone number is required");

        } else if(AuthProvider.valueOf(signUpRequest.getProvider()).equals(AuthProvider.GOOGLE)) {

            user.setProvider(AuthProvider.GOOGLE);
            user.setFbId("");
            if(!signUpRequest.getGgId().isEmpty() && signUpRequest.getGgId() != null){
                user.setGgId(signUpRequest.getGgId());
                user.setEmail(signUpRequest.getGgId());
            } else throw new Exception("For GOOGLE Provider fbId is required");

            if(userTemporary.getPhoneVerified()){
                user.setPhoneVerified(userTemporary.getPhoneVerified());
                user.setEmailVerified(true);
            } else throw new Exception("For GOOGLE Provider a verified phone number is required");

        } else {
            throw new Exception("Provider can only be LOCAL, FACEBOOK or GOOGLE");
        }

        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setImageUrl(signUpRequest.getImageUrl());
        user.setFirebaseKey(signUpRequest.getFirebaseKey());
        user.setIp(signUpRequest.getIp());
        user.setDob(signUpRequest.getDob());
        if(signUpRequest.getGender().equals(Gender.M.name()) || signUpRequest.getGender().equals(Gender.F.name())){
            user.setGender(Gender.valueOf(signUpRequest.getGender()));
        } else {
            user.setGender(Gender.O);
        }

        user.setAddressLine(signUpRequest.getAddressLine());
        user.setApartmentAddress(signUpRequest.getApartmentAddress());
        user.setPostalCode(signUpRequest.getPostalCode());
        user.setCity(signUpRequest.getCity());
        user.setState(signUpRequest.getState());
        user.setCountry(signUpRequest.getCountry());
        user.setAddressType(AddressType.PERMANENT.toString());
        user.setLat(signUpRequest.getLat());
        user.setLng(signUpRequest.getLng());
        user.setIdDocFrontUrl("");
        user.setIdDocBackUrl("");
        user.setIdentificationVerified(false);
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setIsSuspended(false);

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
        userTemporaryService.delete(userTemporary);

        //sending Welcome Email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Welcome to Neighbour Live! ");
        mailMessage.setFrom(EMAIL_FROM);
        mailMessage.setText("Welcome " + user.getFirstName() + " " + user.getLastName() + "\n" +"We really Appreciate the valuable addition of you into our growing community. \n");
        emailService.sendEmail(mailMessage);

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
        String otp = Utility.generateOTP();
        userTemporary.setEmail(email);
        userTemporary.setEmailCode(otp);
        userTemporary.setEmailToken(token);
        userTemporary = userTemporaryService.save(userTemporary);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Neighbour Live | Email Verification! ");
        mailMessage.setFrom(EMAIL_FROM);
        mailMessage.setText("Welcome " +user.getFirstName()+" "+user.getLastName()+ "\n" +"To verify your email, please use the following code: "+ otp);

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
    public boolean sendEmailVerification(String email, User user) throws ResourceNotFoundException {

        UserTemporary userTemporary = userTemporaryService.findByUser(user);
        if(userTemporary == null) {
            userTemporary = userTemporaryService.createTempUser(user);
        }

        userTemporary.setEmail(user.getEmail());
        userTemporary = userTemporaryService.save(userTemporary);

        String otp = Utility.generateSafeToken() + UUID.randomUUID();
        otp = "0000";
        user.setEmailVerificationToken(otp);
        user = userRepository.save(user);

        //sending Welcome Email

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Neighbour Live | Email Verification! ");
        mailMessage.setFrom(EMAIL_FROM);
        mailMessage.setText("Welcome " +user.getFirstName()+" "+user.getLastName()+ "\n" +"To verify your email, please use the following code: "+ otp);
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User confirmEmail(String email, String emailCode) throws Exception {
        User user = userRepository.findByEmailIgnoreCase(email);
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, email);
        if(user.getEmailVerificationToken().equals(emailCode))
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
            user.setPhoneVerificationToken(otp);
            user.setPhoneVerificationOTP(otp);
            user = userRepository.save(user);
            smsService.sendOTPMessage(otp, phoneNumber);
            return user.getPhoneVerificationToken();
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public User confirmPhoneNumber(String phoneNumber, String otp) throws Exception {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if(user == null) throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, otp);
        if(user.getPhoneVerificationOTP().equals(otp))
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
    public UserTemporary confirmUserNamePreRegister(String userName, String publicId) throws Exception {
        if(checkUserNameExist(userName)){
            UserTemporary userTemporary = userTemporaryService.findByPublicId(PublicIdGenerator.decodePublicId(publicId));
            userTemporary.setUserName(userName);
            userTemporary = userTemporaryService.save(userTemporary);
            return userTemporary;
        } else {
            throw new Exception("UserName does not exist, please try again");
        }
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
        User user = userRepository.findByEmailIgnoreCase(email);
        if(user == null){
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkPhoneExist(String phone) {
        User user = userRepository.findByPhoneNumber(phone);
        if(user == null){
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkUserNameExist(String userName) {
        User user = userRepository.findByUserName(userName);
        if(user == null){
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public String editProfile(EditProfileRequest editProfileRequest, User user) throws Exception {
        UserTemporary userTemporary = userTemporaryService.findByUser(user);
        if(userTemporary == null) {
            userTemporary = userTemporaryService.createTempUser(user);
        }

        if(editProfileRequest.getUpdateUserName() && (!editProfileRequest.getUserName().isEmpty())){
            if(checkUserNameExist(editProfileRequest.getUserName())){
                user.setUserName(editProfileRequest.getUserName());
            }
        }
        //Only update personal details if requested
        if(editProfileRequest.getUpdatePersonalDetails()){
            user.setFirstName(editProfileRequest.getFirstName() == null ? user.getFirstName() : editProfileRequest.getFirstName());
            user.setLastName(editProfileRequest.getLastName() == null ? user.getLastName() : editProfileRequest.getLastName());
            user.setImageUrl(editProfileRequest.getImageUrl() == null ? user.getImageUrl() : editProfileRequest.getImageUrl());
            user.setDob(editProfileRequest.getDob() == null ? user.getDob() : editProfileRequest.getDob());
            if(editProfileRequest.getGender().equals(Gender.M.name()) || editProfileRequest.getGender().equals(Gender.F.name())){
                user.setGender(Gender.valueOf(editProfileRequest.getGender()));
            } else {
                user.setGender(Gender.O);
            }
        }

        //Only update Phone number if requested
        if(editProfileRequest.getUpdatePhoneNumber() && !user.getPhoneNumber().equals(editProfileRequest.getPhoneNumber())){
            User u = userRepository.findByPhoneNumber(editProfileRequest.getPhoneNumber());
            if(u == null){
                user.setPhoneVerified(false);
                user.setPhoneVerificationToken(null);
                user.setPhoneVerificationOTP(null);
                user.setPhoneNumber(editProfileRequest.getPhoneNumber());
            } else throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_ALREADY_EXIST_WITH_PHONE);

            String response = AuthConstants.VERIFICATION_OTP_SENT;
            return response;
        }

        //Only Update email if requested
        if(editProfileRequest.getUpdateEmail() && !user.getEmail().equals(editProfileRequest.getEmail())){
            Optional<User> u = userRepository.findByEmail(editProfileRequest.getEmail());
            if(u.isPresent() == false){
                user.setEmailVerificationToken(null);
                user.setEmailVerified(false);
                user.setEmail(editProfileRequest.getEmail());
            } else throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_ALREADY_EXIST_WITH_EMAIL);

            sendEmailVerification(user.getEmail(), user);
            String response = AuthConstants.VERIFICATION_EMAIL_SENT + editProfileRequest.getEmail();
            return response;
        }

        //Only Update address if requested
        if(editProfileRequest.getUpdateAddressDetails()  &&  (!user.getApartmentAddress().equals(editProfileRequest.getApartmentAddress()) || !user.getAddressLine().equals(editProfileRequest.getAddressLine()))){

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
        if(editProfileRequest.getUpdatePassword() && !editProfileRequest.getPassword().equals(null)){
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
        if(!checkEmailExist(email)){
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
        } else throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, email);
    }

    @Override
    public boolean changePassword(String email, String newPassword, BigInteger otp) throws Exception {
        if(!checkEmailExist(email)){
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

        } else throw new ResourceNotFoundException(ResourceNotFoundErrorType.USER_NOT_FOUND_WITH, email);
    }

    @Override
    public int calculateProfileCompletionPercentage(User user) throws ResourceNotFoundException {
        int profileCompletionPercentage = 0;

        //If Valid user - score +20
        if(user.getProvider().equals(AuthProvider.GOOGLE) || user.getProvider().equals(AuthProvider.LOCAL) || user.getProvider().equals(AuthProvider.FACEBOOK)){
            profileCompletionPercentage += 20;
        }

        //If Email Verified - score +20
        if(user.getEmailVerified()){
            profileCompletionPercentage += 20;
        }

        //If Phone Verified - score +15
        if(user.getPhoneVerified()){
            profileCompletionPercentage += 20;
        }

        //If Phone Verified - score +15
        if(user.getIdentificationVerified()){
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
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
        }
        return refreshTokenDto;
    }

}
