package com.app.middleware.exceptions;

import com.app.middleware.exceptions.constants.ExceptionConstants;
import com.app.middleware.exceptions.constants.ForeignKeyExceptionConstants;
import com.app.middleware.exceptions.constants.NotNullExceptionConstants;
import com.app.middleware.exceptions.constants.UniqueKeyExceptionConstants;
import com.app.middleware.exceptions.error.ResourceAlreadyExistErrorType;
import com.app.middleware.exceptions.error.ResourceNotFoundErrorType;
import com.app.middleware.exceptions.type.*;
import com.app.middleware.persistence.domain.*;
import com.app.middleware.persistence.response.PageableResponseEntity;
import com.app.middleware.utility.id.PublicIdGenerator;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

public class ExceptionUtil {
    public static void handleDataIntegrityViolationException(DataIntegrityViolationException exception, BaseEntity baseEntity) throws SomethingUnexpectedException, ResourceAlreadyExistsException, ValidationException, ResourceNotFoundException {

        validateConstraintViolationException(exception);

        String constraintName = ((ConstraintViolationException) exception.getCause()).getConstraintName();

        if(constraintName.startsWith(ExceptionConstants.UNIQUE_KEY_EXCEPTION_CONSTANT)) {
            handleUniqueKeyException(constraintName, baseEntity);
        }
        else if(constraintName.startsWith(ExceptionConstants.FOREIGN_KEY_EXCEPTION_CONSTANT)) {
            handleForeignKeyException(constraintName, baseEntity);
        }
        else if(constraintName.endsWith(ExceptionConstants.CHECK_EXCEPTION_CONSTANT)) {
            handleCheckException(constraintName, baseEntity);
        }
        else {
            handleNotNullException(constraintName, baseEntity);
        }

        throw new SomethingUnexpectedException(exception);
    }

    private static void validateConstraintViolationException(DataIntegrityViolationException exception) throws SomethingUnexpectedException {
        if(!(exception.getCause() instanceof ConstraintViolationException)) {
            throw new SomethingUnexpectedException(exception);
        }
    }

    private static void handleUniqueKeyException(String constraintName, BaseEntity baseEntity) throws ResourceAlreadyExistsException, SomethingUnexpectedException {
        switch (constraintName) {

            // User
            case UniqueKeyExceptionConstants.UK_public_id_users: {
                ResourceAlreadyExistErrorType resourceAlreadyExistErrorType = ResourceAlreadyExistErrorType.USER_ALREADY_EXIST_WITH_PUBLIC_ID;
                if (baseEntity instanceof User)
                    resourceAlreadyExistErrorType.setValue(PublicIdGenerator.encodedPublicId(((User) baseEntity).getPublicId()));
                throw new ResourceAlreadyExistsException(resourceAlreadyExistErrorType);
            }

            case UniqueKeyExceptionConstants.UK_stripe_id_users: {
                ResourceAlreadyExistErrorType resourceAlreadyExistErrorType = ResourceAlreadyExistErrorType.USER_ALREADY_EXIST_WITH_STRIPE_ID;
                if (baseEntity instanceof User)
                    resourceAlreadyExistErrorType.setValue(PublicIdGenerator.encodedPublicId(((User) baseEntity).getPublicId()));
                throw new ResourceAlreadyExistsException(resourceAlreadyExistErrorType);
            }

            case UniqueKeyExceptionConstants.UK_connect_id_users: {
                ResourceAlreadyExistErrorType resourceAlreadyExistErrorType = ResourceAlreadyExistErrorType.USER_ALREADY_EXIST_WITH_CONNECT_ID;
                if (baseEntity instanceof User)
                    resourceAlreadyExistErrorType.setValue(PublicIdGenerator.encodedPublicId(((User) baseEntity).getPublicId()));
                throw new ResourceAlreadyExistsException(resourceAlreadyExistErrorType);
            }

            case UniqueKeyExceptionConstants.UK_access_token_users: {
                ResourceAlreadyExistErrorType resourceAlreadyExistErrorType = ResourceAlreadyExistErrorType.USER_ALREADY_EXIST_WITH_ACCESS_TOKEN;
                if (baseEntity instanceof User)
                    resourceAlreadyExistErrorType.setValue(((User) baseEntity).getPhoneNumber());
                throw new ResourceAlreadyExistsException(resourceAlreadyExistErrorType);
            }

            case UniqueKeyExceptionConstants.UK_phone_verification_token_users: {
                ResourceAlreadyExistErrorType resourceAlreadyExistErrorType = ResourceAlreadyExistErrorType.USER_ALREADY_EXIST_WITH_PHONE_VERIFICATION_TOKEN;
                if (baseEntity instanceof User)
                    resourceAlreadyExistErrorType.setValue(((User) baseEntity).getEmail());
                throw new ResourceAlreadyExistsException(resourceAlreadyExistErrorType);
            }

            case UniqueKeyExceptionConstants.UK_email_verification_token_users: {
                ResourceAlreadyExistErrorType resourceAlreadyExistErrorType = ResourceAlreadyExistErrorType.USER_ALREADY_EXIST_WITH_EMAIL_VERIFICATION_TOKEN;
                if (baseEntity instanceof User)
                    resourceAlreadyExistErrorType.setValue(((User) baseEntity).getAccessToken());
                throw new ResourceAlreadyExistsException(resourceAlreadyExistErrorType);
            }

            case UniqueKeyExceptionConstants.UK_email_users: {
                ResourceAlreadyExistErrorType resourceAlreadyExistErrorType = ResourceAlreadyExistErrorType.USER_ALREADY_EXIST_WITH_EMAIL;
                if (baseEntity instanceof User)
                    resourceAlreadyExistErrorType.setValue(PublicIdGenerator.encodedPublicId(((User) baseEntity).getPublicId()));
                throw new ResourceAlreadyExistsException(resourceAlreadyExistErrorType);
            }

            case UniqueKeyExceptionConstants.UK_phone_number_users: {
                ResourceAlreadyExistErrorType resourceAlreadyExistErrorType = ResourceAlreadyExistErrorType.USER_ALREADY_EXIST_WITH_PHONE_NUMBER;
                if (baseEntity instanceof User)
                    resourceAlreadyExistErrorType.setValue(((User) baseEntity).getPhoneNumber());
                throw new ResourceAlreadyExistsException(resourceAlreadyExistErrorType);
            }


            case UniqueKeyExceptionConstants.UK_public_id_awards: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.AWARD_ALREADY_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_user_awards: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.USER_AWARD_ALREADY_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_user_skills: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.SKILL_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_skills: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.USER_SKILL_ALREADY_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_user_payment_cards: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.PAYMENT_CARD_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_card_number_user_payment_cards: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.PAYMENT_CARD_NUMBER_ALREADY_EXIST);
            }

            case UniqueKeyExceptionConstants.UK_public_id_user_addresses: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.USER_ADDRESS_ALREADY_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_reviews: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.REVIEW_AWARD_ALREADY_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_tasks: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.TASK_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_task_categories: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.TASK_CATEGORY_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_user_bids: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.BID_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_user_bank_account: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.USER_BANK_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_account_user_bank_account: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.USER_BANK_EXIST_WITH_ACCOUNT_NUMBER);
            }

            case UniqueKeyExceptionConstants.UK_public_id_user_transactions: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.USER_TRANSACTION_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_payment_id_user_transactions: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.USER_TRANSACTION_EXIST_WITH_PAYMENT_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_user_wallet: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.USER_WALLET_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_support_user: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.SUPPORT_TICKET_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_public_id_report_user: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.REPORT_TICKET_EXIST_WITH_PUBLIC_ID);
            }

            case UniqueKeyExceptionConstants.UK_reporter_and_reported_user_cannot_be_same: {
                throw new ResourceAlreadyExistsException(ResourceAlreadyExistErrorType.REPORTER_AND_REPORTED_USER_CANNOT_BE_SAME);
            }

//            case UniqueKeyExceptionConstants.UK_public_id_user_payment_cards: {
//                throw new SomethingUnexpectedException(SomethingUnexpectedErrorType.DUPLICATE_PUBLIC_KEY);
//            }

            default:
                break;
        }
    }

    private static void handleNotNullException(String constraintName, BaseEntity baseEntity) throws ValidationException {
        switch (constraintName) {
            // Generic
            case NotNullExceptionConstants.ID :
                throw new ValidationException(new FieldError("id","id","Id can not be null"));

            case NotNullExceptionConstants.DESCRIPTION :
                throw new ValidationException(new FieldError("description","description","description can not be null"));

            case NotNullExceptionConstants.PUBLIC_ID :
                throw new ValidationException(new FieldError("public_id","public_id","public_id can not be null"));

            case NotNullExceptionConstants.CREATE_DATE_TIME :
                throw new ValidationException(new FieldError("create_date_time","create_date_time","create_date_time can not be null"));

            case NotNullExceptionConstants.TITLE :
                throw new ValidationException(new FieldError("title","title","title can not be null"));

            case NotNullExceptionConstants.IS_APPROVED :
                throw new ValidationException(new FieldError("is_approved","is_approved","is_approved can not be null"));

            case NotNullExceptionConstants.IS_DELETED :
                throw new ValidationException(new FieldError("is_deleted","is_deleted","is_deleted can not be null"));

            case NotNullExceptionConstants.IS_ACTIVE :
                throw new ValidationException(new FieldError("is_active","is_active","is_active Time can not be null"));

            //Separate
            case NotNullExceptionConstants.AWARD_ICON :
                throw new ValidationException(new FieldError("award_icon","award_icon","award_icon can not be null"));

            case NotNullExceptionConstants.AWARD_TYPE :
                throw new ValidationException(new FieldError("award_type","award_type","award_type can not be null"));

            // Reviews
            case NotNullExceptionConstants.IS_DELETED_BY_POSTER :
                throw new ValidationException(new FieldError("is_deleted_by_poster","is_deleted_by_poster","is_deleted_by_poster can not be null"));

            case NotNullExceptionConstants.IS_DELETED_BY_TASKER :
                throw new ValidationException(new FieldError("is_deleted_by_tasker","is_deleted_by_tasker","is_deleted_by_tasker can not be null"));

            case NotNullExceptionConstants.IS_REMOVED:
                throw new ValidationException(new FieldError("is_removed","is_removed","is_removed can not be null"));

            case NotNullExceptionConstants.IS_REPORTED_BY_POSTER:
                throw new ValidationException(new FieldError("is_reported_by_poster","is_reported_by_poster","is_reported_by_poster can not be null"));

            case NotNullExceptionConstants.IS_REPORTED_BY_TASKER:
                throw new ValidationException(new FieldError("is_reported_by_tasker","is_reported_by_tasker","is_reported_by_tasker can not be null"));

            case NotNullExceptionConstants.IS_REVIEWED_POSTER:
                throw new ValidationException(new FieldError("is_reviewed_poster","is_reviewed_poster","is_reviewed_poster can not be null"));

            case NotNullExceptionConstants.IS_REVIEWED_TASKER:
                throw new ValidationException(new FieldError("is_reviewed_tasker","is_reviewed_tasker","is_reviewed_tasker can not be null"));

            case NotNullExceptionConstants.POSTER_NAME:
                throw new ValidationException(new FieldError("poster_name","poster_name","poster_name can not be null"));

            case NotNullExceptionConstants.TASKER_IMAGE:
                throw new ValidationException(new FieldError("tasker_image","tasker_image","tasker_image can not be null"));

            case NotNullExceptionConstants.TASKER_NAME:
                throw new ValidationException(new FieldError("tasker_name","tasker_name","tasker_name can not be null"));

            //Skills
            case NotNullExceptionConstants.NAME:
                throw new ValidationException(new FieldError("name","name","name can not be null"));

            //Tasks
            case NotNullExceptionConstants.IS_ASSIGNED:
                throw new ValidationException(new FieldError("is_assigned","is_assigned","is_assigned can not be null"));

            case NotNullExceptionConstants.IS_COMPLETED:
                throw new ValidationException(new FieldError("is_completed","is_completed","is_completed can not be null"));

            case NotNullExceptionConstants.IS_PENDING:
                throw new ValidationException(new FieldError("is_pending","is_pending","is_pending can not be null"));

            //User_addresses
            case NotNullExceptionConstants.ADDRESS_TYPE:
                throw new ValidationException(new FieldError("address_type","address_type","address_type can not be null"));

            case NotNullExceptionConstants.ADDRESS_LINE:
                throw new ValidationException(new FieldError("address_line","address_line","address_line can not be null"));

            //User_awards
            case NotNullExceptionConstants.IS_UNLOCKED:
                throw new ValidationException(new FieldError("is_unlocked","is_unlocked","is_unlocked can not be null"));

            case NotNullExceptionConstants.PROGRESS:
                throw new ValidationException(new FieldError("progress","progress","progress can not be null"));

            //User_payment_cards
            case NotNullExceptionConstants.CARD_EXPIRY_DATE:
                throw new ValidationException(new FieldError("card_expiry_date","card_expiry_date","card_expiry_date can not be null"));

            case NotNullExceptionConstants.CARD_NUMBER:
                throw new ValidationException(new FieldError("card_number","card_number","card_number can not be null"));

            case NotNullExceptionConstants.CARD_SECRET_PIN:
                throw new ValidationException(new FieldError("card_secret_pin","card_secret_pin","card_secret_pin can not be null"));

            case NotNullExceptionConstants.CARD_VERIFIED:
                throw new ValidationException(new FieldError("card_verified","card_verified","card_verified can not be null"));

            case NotNullExceptionConstants.CARDHOLDER_NAME:
                throw new ValidationException(new FieldError("cardholder_name","cardholder_name","cardholder_name can not be null"));

            case NotNullExceptionConstants.ISSUING_DATE:
                throw new ValidationException(new FieldError("issuing_date","issuing_date","issuing_date can not be null"));

            case NotNullExceptionConstants.ISSUING_INSTITUTION:
                throw new ValidationException(new FieldError("issuing_institution","issuing_institution","issuing_institution can not be null"));

            case NotNullExceptionConstants.SKILL_PROFICIENCY:
                throw new ValidationException(new FieldError("skill_proficiency","skill_proficiency","skill_proficiency can not be null"));



            case NotNullExceptionConstants.STRIPE_SOURCE_ID:
                throw new ValidationException(new FieldError("stripe_source_id","stripe_source_id","stripe_source_id can not be null"));

            case NotNullExceptionConstants.STRIPE_SOURCE_OBJECT:
                throw new ValidationException(new FieldError("stripe_source_object","stripe_source_object","stripe_source_object can not be null"));

            case NotNullExceptionConstants.AMOUNT:
                throw new ValidationException(new FieldError("amount","amount","amount can not be null"));

            case NotNullExceptionConstants.IS_RESOLVED:
                throw new ValidationException(new FieldError("is_resolved","is_resolved","is_resolved can not be null"));

            case NotNullExceptionConstants.PAYMENT_TYPE:
                throw new ValidationException(new FieldError("payment_type","payment_type","payment_type can not be null"));



            case NotNullExceptionConstants.BUDGET:
                throw new ValidationException(new FieldError("budget","budget","budget can not be null"));

            case NotNullExceptionConstants.START_DATE_TIME:
                throw new ValidationException(new FieldError("start_date_time","start_date_time","start_date_time can not be null"));

            case NotNullExceptionConstants.TRANSACTION_TYPE:
                throw new ValidationException(new FieldError("transaction_type","transaction_type","transaction_type can not be null"));

            case NotNullExceptionConstants.PAYMENT_ID:
                throw new ValidationException(new FieldError("payment_id","payment_id","payment_id can not be null"));

            case NotNullExceptionConstants.ACCOUNT_HOLDER_NAME:
                throw new ValidationException(new FieldError("account_holder_name","account_holder_name","account_holder_name can not be null"));


            case NotNullExceptionConstants.BANK_NAME:
                throw new ValidationException(new FieldError("bank_name","bank_name","bank_name can not be null"));

            case NotNullExceptionConstants.SWIFT_CODE:
                throw new ValidationException(new FieldError("swift_code","swift_code","swift_code can not be null"));

            case NotNullExceptionConstants.IBAN:
                throw new ValidationException(new FieldError("iban","iban","iban can not be null"));

            case NotNullExceptionConstants.IS_VERIFIED:
                throw new ValidationException(new FieldError("is_verified","is_verified","is_verified can not be null"));

            case NotNullExceptionConstants.CURRENCY:
                throw new ValidationException(new FieldError("currency","currency","currency can not be null"));


            case NotNullExceptionConstants.SUBJECT:
                throw new ValidationException(new FieldError("subject","subject","subject can not be null"));

            case NotNullExceptionConstants.REPORTER_PUBLIC_ID:
                throw new ValidationException(new FieldError("reporter_public_id","reporter_public_id","reporter_public_id can not be null"));

            case NotNullExceptionConstants.REPORTED_PUBLIC_ID:
                throw new ValidationException(new FieldError("reported_public_id","reported_public_id","reported_public_id can not be null"));

            case NotNullExceptionConstants.RELATED_TO:
                throw new ValidationException(new FieldError("related_to","related_to","related_to can not be null"));

            case NotNullExceptionConstants.ICON_URL:
                throw new ValidationException(new FieldError("icon_url","icon_url","icon_url can not be null"));



            case NotNullExceptionConstants.HOURS:
                throw new ValidationException(new FieldError("hours","hours","hours can not be null"));

            case NotNullExceptionConstants.HOURLY_RATE:
                throw new ValidationException(new FieldError("hourly_rate","hourly_rate","hourly_rate can not be null"));

        }
    }

    private static void handleCheckException(String constraintName, BaseEntity baseEntity) throws ValidationException {
        switch (constraintName) {

//            case CheckExceptionConstants.CIRCLE_MEMBERS_GENDER_CHECK :
//                throw new ValidationException(new FieldError("gender", "gender", "Gender must be M, F or O"));

            default:
                break;
        }
    }

    private static void handleForeignKeyException (String constraintName, BaseEntity baseEntity) throws ResourceNotFoundException {
        switch (constraintName) {
            case ForeignKeyExceptionConstants.FK_user_id_user_addresses: {
                ResourceNotFoundErrorType resourceNotFoundErrorType = ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_ADDRESS;
                if (baseEntity instanceof UserAddress)
                    resourceNotFoundErrorType.setValue(((UserAddress) baseEntity).getPublicId() != null ? ((UserAddress) baseEntity).getUser().getPublicId().toString() : null);
                throw new ResourceNotFoundException(resourceNotFoundErrorType);
            }

            case ForeignKeyExceptionConstants.FK_user_id_user_awards: {
                ResourceNotFoundErrorType resourceNotFoundErrorType = ResourceNotFoundErrorType.USER_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_AWARD;
                if (baseEntity instanceof UserAward)
                    resourceNotFoundErrorType.setValue(((UserAward) baseEntity).getPublicId() != null ? ((UserAward) baseEntity).getUser().getPublicId().toString() : null);
                throw new ResourceNotFoundException(resourceNotFoundErrorType);
            }



            case ForeignKeyExceptionConstants.FK_award_id_user_awards: {
                ResourceNotFoundErrorType resourceNotFoundErrorType = ResourceNotFoundErrorType.AWARD_NOT_FOUND_WITH_PUBLIC_ID_IN_USER_AWARD;
                if (baseEntity instanceof UserAward)
                    resourceNotFoundErrorType.setValue(((UserAward) baseEntity).getPublicId() != null ? ((UserAward) baseEntity).getUser().getPublicId().toString() : null);
                throw new ResourceNotFoundException(resourceNotFoundErrorType);
            }

//            case ForeignKeyExceptionConstants.FK_task_transaction_tasks: {
//                ResourceNotFoundErrorType resourceNotFoundErrorType = ResourceNotFoundErrorType.TASK_TRANSACTION_NOT_FOUND_WITH_PUBLIC_ID_IN_TASK;
//                if (baseEntity instanceof Task)
//                    resourceNotFoundErrorType.setValue(((Task) baseEntity).getPublicId() != null ? ((Task) baseEntity).getUserTransactions().getPublicId().toString() : null);
//                throw new ResourceNotFoundException(resourceNotFoundErrorType);
//            }

            default:
                break;
        }
    }

    public static PageableResponseEntity<Object> handlePaginatedException(Exception e) throws Exception {

        if(e instanceof BaseException) {
            throw e;
        }

        if(e instanceof DataIntegrityViolationException) {
            handleDataIntegrityViolationException((DataIntegrityViolationException) e, null);
        }

        throw new Exception(e.getMessage());
//        throw new SomethingUnexpectedException(e, SomethingUnexpectedErrorType.SOMETHING_UNEXPECTED_ERROR);
    }

    public static ResponseEntity<?> handleException(Exception e) throws Exception {

        if(e instanceof BaseException) {
            throw e;
        }

        if(e instanceof DataIntegrityViolationException) {
            handleDataIntegrityViolationException((DataIntegrityViolationException) e, null);
        }

        throw new Exception(e.getMessage());
//        throw new SomethingUnexpectedException(e, SomethingUnexpectedErrorType.SOMETHING_UNEXPECTED_ERROR);
    }
}
