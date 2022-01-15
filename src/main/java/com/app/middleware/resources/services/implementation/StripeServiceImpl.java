package com.app.middleware.resources.services.implementation;

import com.app.middleware.exceptions.error.UnauthorizedExceptionErrorType;
import com.app.middleware.exceptions.type.UnauthorizedException;
import com.app.middleware.persistence.domain.User;
import com.app.middleware.persistence.domain.UserBankAccount;
import com.app.middleware.persistence.domain.UserPaymentCard;
import com.app.middleware.persistence.repository.UserBankAccountRepository;
import com.app.middleware.persistence.repository.UserPaymentCardsRepository;
import com.app.middleware.persistence.repository.UserRepository;
import com.app.middleware.persistence.repository.UserTransactionsRepository;
import com.app.middleware.persistence.request.AddBankAccountRequest;
import com.app.middleware.persistence.request.AddPaymentCardRequest;
import com.app.middleware.persistence.request.CreateStripeConnectRequest;
import com.app.middleware.persistence.request.RedeemAmount;
import com.app.middleware.persistence.type.Intervals;
import com.app.middleware.resources.services.StripeService;
import com.app.middleware.utility.id.PublicIdGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.RequestOptions;
import com.stripe.param.PayoutCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StripeServiceImpl implements StripeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPaymentCardsRepository userPaymentCardsRepository;

    @Autowired
    private UserTransactionsRepository userTransactionsRepository;

    @Autowired
    private UserBankAccountRepository userBankAccountRepository;

    @Value("${stripe.keys.secret}")
    private String API_SECRET_KEY;

    @Value("${stripe.keys.client}")
    private String API_CLIENT_KEY;

    @Value("${service-fees-factor}")
    private float SERVICE_FEE_FACTOR;

    @Value("${currency-factor}")
    private float CURRENCY_FACTOR;

    @Override
    public Account createStripeCustomConnectAccount(CreateStripeConnectRequest stripeConnectRequest) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;
        //Transfer abilities
        Map<String, Object> cardPayments = new HashMap<>();
        cardPayments.put("requested", true);
        //Transfer abilities
        Map<String, Object> transfers = new HashMap<>();
        transfers.put("requested", true);

        //individual details
        //dob
        Map<String, Object> individualDOB = new HashMap<>();
        String[] dob = stripeConnectRequest.getDob().split("-");
        individualDOB.put("day", dob[2]);
        individualDOB.put("month", dob[1]);
        individualDOB.put("year", dob[0]);

        //address
        Map<String, Object> individualAddress = new HashMap<>();
        individualAddress.put("line1", stripeConnectRequest.getAddressLine());
        individualAddress.put("line2", "");
        individualAddress.put("postal_code", stripeConnectRequest.getPostalCode());
        individualAddress.put("city", stripeConnectRequest.getCity());
        individualAddress.put("state", stripeConnectRequest.getState());
        individualAddress.put("country", stripeConnectRequest.getCountry());

        Map<String, Object> individual = new HashMap<>();
        individual.put("first_name", stripeConnectRequest.getFirstName());
        individual.put("last_name", stripeConnectRequest.getLastName());
        individual.put("dob", individualDOB);
        individual.put("address", individualAddress);
        individual.put("email", stripeConnectRequest.getEmail());

        Map<String, Object> capabilities = new HashMap<>();
        capabilities.put("card_payments", cardPayments);
        capabilities.put("transfers", transfers);

        Map<String, Object> tosAcceptance = new HashMap<>();
        tosAcceptance.put("date",  new Date().getTime()/1000);
        tosAcceptance.put("ip", stripeConnectRequest.getIp());

        Map<String, Object> metaData = new HashMap<>();
        metaData.put("userPublicId", stripeConnectRequest.getUserPublicId());

        Map<String, Object> params = new HashMap<>();
        params.put("capabilities", capabilities);
        params.put("metadata", metaData);
        params.put("tos_acceptance", tosAcceptance);
        params.put("individual", individual);

        //For now only for CANADA
        params.put("type", "custom");
        params.put("country", "USA");
        params.put("business_type", "individual");
        params.put("default_currency", "usd");
        params.put("email", stripeConnectRequest.getEmail());

        return Account.create(params);
    }

    @Override
    public Account getConnectAccount(String connectId) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Account account = Account.retrieve(connectId);
        return account;
    }

    @Override
    public Account setDefaultCurrencyAccount(String userConnectId, String connectSourceId, boolean isCard, User user) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Account account = getConnectAccount(userConnectId);
        if(isCard){
            Card card = (Card) account.getExternalAccounts().retrieve(connectSourceId);
            Map<String, Object> params = new HashMap<>();
            params.put("default_for_currency", "true");
            card.update(params);

            List<UserPaymentCard> userPaymentCards = userPaymentCardsRepository.findAllByUser(user);
            userPaymentCards.forEach(paymentCard -> {
                if(!paymentCard.getConnectSourceId().equals(card.getId())){
                    paymentCard.setIsDefault(false);
                } else {
                    paymentCard.setIsDefault(true);
                }
            });
            userPaymentCardsRepository.saveAll(userPaymentCards);
            List<UserBankAccount> userBankAccounts = userBankAccountRepository.findAllByUser(user);
            userBankAccounts.forEach( bank -> bank.setIsDefault(false));
            userBankAccountRepository.saveAll(userBankAccounts);

        } else {
            BankAccount bank = (BankAccount) account.getExternalAccounts().retrieve(connectSourceId);
            Map<String, Object> params = new HashMap<>();
            params.put("default_for_currency", "true");
            bank.update(params);

            List<UserBankAccount> userBankAccounts = userBankAccountRepository.findAllByUser(user);
            userBankAccounts.forEach( bankAccount -> {
                if(!bankAccount.getStripeSourceId().equals(bank.getId())){
                    bankAccount.setIsDefault(false);
                } else {
                    bankAccount.setIsDefault(true);
                }
            });
            userBankAccountRepository.saveAll(userBankAccounts);

            List<UserPaymentCard> userPaymentCards = userPaymentCardsRepository.findAllByUser(user);
            userPaymentCards.forEach( paymentCard -> paymentCard.setIsDefault(false));
            userPaymentCardsRepository.saveAll(userPaymentCards);
        }

        return account;
    }

    @Override
    public Customer createStripeCustomer(User user) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Map<String, Object> customParameter = new HashMap<>();
        customParameter.put("name", user.getFirstName()+" "+user.getLastName());
        customParameter.put("email", user.getEmail());

        return Customer.create(customParameter);
    }

    @Override
    public Customer retrieveStripeCustomer(String stripeCustomerId) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Customer customer = Customer.retrieve(stripeCustomerId);
        return customer;
    }

    @Override
    public UserPaymentCard addPaymentCard(User user, AddPaymentCardRequest addPaymentCardRequest, String userConnectId) throws StripeException, UnauthorizedException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Integer countUserCards = userPaymentCardsRepository.countByUser(user);
        if(countUserCards > 0){
            System.out.println("\nCannot add more cards.\n");
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "Cannot add more cards");
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // For Stripe Customer;

        Account account = getConnectAccount(userConnectId);
        Customer customer = retrieveStripeCustomer(user.getStripeId());


        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("object", "card");
        cardParams.put("number", addPaymentCardRequest.getCardNumber());
        cardParams.put("exp_month", addPaymentCardRequest.getExpMonth());
        cardParams.put("exp_year", addPaymentCardRequest.getExpYear());
        cardParams.put("cvc", addPaymentCardRequest.getCvc());
        cardParams.put("currency", "usd");
        cardParams.put("default_for_currency", (addPaymentCardRequest.getDefaultUsage()) == 1 ? "true":"false");


        // For Connect Card
        Map<String, Object> tokenParams = new HashMap<>();
        tokenParams.put("card", cardParams);
        Token token = Token.create(tokenParams);

        //Creating source for stripe connect
        Map<String, Object> connectExternalAccountParams = new HashMap<>();
        connectExternalAccountParams.put("external_account", token.getId());
        if(addPaymentCardRequest.getDefaultUsage() == 1){
            connectExternalAccountParams.put("default_for_currency", (addPaymentCardRequest.getDefaultUsage()) == 1 ? "true":"false");
        }
        Card card = (Card) account.getExternalAccounts().create(connectExternalAccountParams);

        //Creating source for stripe customer
        Map<String, Object> metaData = new HashMap<>();

        //creating meta data to track the belonging of this card
        metaData.put("connectCardSourceId", card.getId());
        metaData.put("connectAccountId", user.getConnectId());
        metaData.put("stripeCustomerId", user.getStripeId());
        metaData.put("userPublicId", PublicIdGenerator.encodedPublicId(user.getPublicId()));

        cardParams.put("metadata", metaData);
        Map<String, Object> customerSourceParams = new HashMap<>();

        // Refreshing with new details
        tokenParams.put("card", cardParams);
        customerSourceParams.put("source", Token.create(tokenParams).getId());
        Card customerCard = (Card) customer.getSources().create(customerSourceParams);

        //Creating Card for User
        UserPaymentCard userPaymentCard = new UserPaymentCard();
        userPaymentCard.setUser(user);
        userPaymentCard.setIsActive(true);
        userPaymentCard.setIsDeleted(false);
        userPaymentCard.setCardVerified(true);
        userPaymentCard.setIsDefault(addPaymentCardRequest.getDefaultUsage() == 1 ? true: false);
        userPaymentCard.setCardBrand(customerCard.getBrand());
        userPaymentCard.setCardType(customerCard.getFunding());
        userPaymentCard.setStripeSourceId(customerCard.getId());
        userPaymentCard.setConnectSourceId(card.getId());
        userPaymentCard.setStripeSourceObject(gson.toJson(customerCard));
        userPaymentCard.setPublicId(PublicIdGenerator.generatePublicId());
        userPaymentCard.setCardholderName(user.getFirstName()+ " " +user.getLastName());

        userPaymentCard.setCardNumber(addPaymentCardRequest.getCardNumber().substring(addPaymentCardRequest.getCardNumber().length() - 4));
        userPaymentCard.setCardExpiryDate(addPaymentCardRequest.getExpMonth()+"/"+addPaymentCardRequest.getExpYear());
        userPaymentCard = userPaymentCardsRepository.save(userPaymentCard);


        if(addPaymentCardRequest.getDefaultUsage() == 1){
            List<UserPaymentCard> userPaymentCards = userPaymentCardsRepository.findAllByUser(user);
            UserPaymentCard finalUserPaymentCard = userPaymentCard;
            userPaymentCards.forEach(paymentCard -> {
                if(!paymentCard.getPublicId().equals(finalUserPaymentCard.getPublicId())){
                    paymentCard.setIsDefault(false);
                }
            });
            userPaymentCardsRepository.saveAll(userPaymentCards);


            List<UserBankAccount> userBankAccounts = userBankAccountRepository.findAllByUser(user);
            userBankAccounts.forEach( bank -> bank.setIsDefault(false));
            userBankAccountRepository.saveAll(userBankAccounts);
        }

        System.out.println(gson.toJson(customerCard));
        System.out.println(gson.toJson(card));

        //Updating User
        user.setCardVerified(true);
        userRepository.save(user);
        return userPaymentCard;
    }

    @Override
    public boolean deleteCard(User user, UserPaymentCard userPaymentCard) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Account account = getConnectAccount(user.getConnectId());
        Card card = (Card) account.getExternalAccounts().retrieve(userPaymentCard.getConnectSourceId());
        card.delete();


        Customer customer = Customer.retrieve(user.getStripeId());
        Card stripeCard = (Card) customer.getSources().retrieve(userPaymentCard.getStripeSourceId());
        stripeCard.delete();

        user.setCardVerified(false);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserBankAccount addBankAccount(User user, AddBankAccountRequest addBankAccountRequest, String connectId) throws StripeException, UnauthorizedException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Integer countUserBankAccounts = userBankAccountRepository.countByUser(user);
        if(countUserBankAccounts > 0){
            System.out.println("\nCannot add more bank accounts.\n");
            throw new UnauthorizedException(UnauthorizedExceptionErrorType.UNAUTHORIZED_ACTION, "Cannot add more bank accounts");
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Account account = getConnectAccount(connectId);

        Map<String, Object> tokenParams = new HashMap<>();
        Map<String, Object> bankAccountParams = new HashMap<>();
        bankAccountParams.put("country", "USA");
        bankAccountParams.put("currency", "usd");
        bankAccountParams.put("account_holder_type", "individual");
        bankAccountParams.put("account_holder_name", addBankAccountRequest.getAccountHolderName());
        bankAccountParams.put("account_number", addBankAccountRequest.getAccountNumber());
        //routing number = Transit Number(5 digits) - Financial Institute Number(3 Digits)
        bankAccountParams.put("routing_number", addBankAccountRequest.getTransitNumber() +"-"+ addBankAccountRequest.getFinancialInstitutionNumber());

        tokenParams.put("bank_account", bankAccountParams);
        Token token = Token.create(tokenParams);

        //Creating source for Connect
        Map<String, Object> params = new HashMap<>();
        params.put("external_account", token.getId());
        if(addBankAccountRequest.getDefaultUsage() == 1){
            params.put("default_for_currency", (addBankAccountRequest.getDefaultUsage()) == 1 ? "true":"false");
        }
        BankAccount bankAccount = (BankAccount) account.getExternalAccounts().create(params);

        //Creating BanK Account Object for  local User
        UserBankAccount userBankAccount = new UserBankAccount();
        userBankAccount.setUser(user);
        userBankAccount.setIsActive(true);
        userBankAccount.setIsDeleted(false);
        userBankAccount.setIsDefault(addBankAccountRequest.getDefaultUsage() == 1 ? true: false);
        userBankAccount.setIsVerified(false);
        userBankAccount.setStripeSourceId(bankAccount.getId());
        userBankAccount.setStripeSourceObject(gson.toJson(bankAccount));
        userBankAccount.setPublicId(PublicIdGenerator.generatePublicId());
        userBankAccount.setAccountHolderName(addBankAccountRequest.getAccountHolderName());
        userBankAccount.setBankName(addBankAccountRequest.getBankName());

        userBankAccount.setTransitNumber(addBankAccountRequest.getTransitNumber());
        userBankAccount.setFinancialInstitutionNumber(addBankAccountRequest.getFinancialInstitutionNumber());
        userBankAccount.setAccountNumber(addBankAccountRequest.getAccountNumber().substring(addBankAccountRequest.getAccountNumber().length() - 4));
        userBankAccount = userBankAccountRepository.save(userBankAccount);

        if(addBankAccountRequest.getDefaultUsage() == 1){
            List<UserBankAccount> userBankAccounts = userBankAccountRepository.findAllByUser(user);
            UserBankAccount finalUserBankAccount = userBankAccount;
            userBankAccounts.forEach(bank -> {
                if(!bank.getPublicId().equals(finalUserBankAccount.getPublicId())){
                    bank.setIsDefault(false);
                }
            });
            userBankAccountRepository.saveAll(userBankAccounts);

            List<UserPaymentCard> userPaymentCards = userPaymentCardsRepository.findAllByUser(user);
            userPaymentCards.forEach( paymentCard -> paymentCard.setIsDefault(false));
            userPaymentCardsRepository.saveAll(userPaymentCards);
        }

        return userBankAccount;
    }

    @Override
    public UserBankAccount verifyBankAccount(User user, String stripeId, String stripeBankId) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Customer customer = Customer.retrieve(stripeId);
        BankAccount bankAccount = (BankAccount) customer.getSources().retrieve(stripeBankId);

        List<Object> amounts = new ArrayList<>();
        amounts.add(32);
        amounts.add(45);
        Map<String, Object> params = new HashMap<>();
        params.put("amounts", amounts);
        bankAccount.verify(params);

        UserBankAccount userBankAccount = userBankAccountRepository.findByUser(user);
        userBankAccount.setIsVerified(true);
        userBankAccount.setStripeSourceObject(String.valueOf(bankAccount));
        userBankAccount = userBankAccountRepository.save(userBankAccount);
        return userBankAccount;
    }

    @Override
    public boolean deleteBankAccount(User user, UserBankAccount userBankAccount) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Account account = getConnectAccount(user.getConnectId());
        BankAccount bankAccount = (BankAccount) account.getExternalAccounts().retrieve(userBankAccount.getStripeSourceId());
        bankAccount.delete();

        return true;
    }

//    @Override
//    public UserTransactions chargeMoneyFromTaskPoster(Task task) throws StripeException {
//        Stripe.apiKey = API_SECRET_KEY;
//        Stripe.clientId = API_CLIENT_KEY;
//
//        Customer poster = retrieveStripeCustomer(task.getPoster().getStripeId());
//        List<UserPaymentCard> userPaymentCards = userPaymentCardsRepository.findAllByUser(task.getPoster());
//        Card posterCard = (Card) poster.getSources().retrieve(userPaymentCards.get(0).getStripeSourceId());
//
//        Long amount = Long.valueOf(Math.round(task.getBudget().floatValue() * CURRENCY_FACTOR));
//
//        // `source` is obtained with Stripe.js; see https://stripe.com/docs/payments/accept-a-payment-charges#web-create-token
//        // Charging from a payment "source"
//        // "source" to be charged. This can be the ID of a card (i.e., credit or debit card), a bank account,
//        // a source, a token, or a connected account. For certain sources—namely, cards, bank accounts, and attached
//        // sources—you must also pass the ID of the associated customer.
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("amount", amount);
//        params.put("currency", "usd");
//        params.put("source", posterCard.getId());
//
//        params.put( "description", "Reserved Payment to BidOnTask for: \nTask: " +task.getTitle()+"\n" +
//                    "Task description: "+task.getDescription()+"\n"+
//                    "Task budget: "+task.getBudget()
//        );
//
//        //Optional
//        params.put("customer", poster.getId());
//        params.put("receipt_email", task.getPoster().getEmail());
//        params.put("capture", "true");
//
//        Charge charge = Charge.create(params);
//
//        //Creating Receipt
//        UserTransactions userTransaction = new UserTransactions();
//        userTransaction.setPublicId(PublicIdGenerator.generatePublicId());
//        userTransaction.setUser(task.getPoster());
//
//        userTransaction.setPurpose(task.getTaskStatus());
//        userTransaction.setDescription(task.getDescription());
//        userTransaction.setCategory(task.getTaskCategory().getName());
//
//        userTransaction.setAmount(BigDecimal.valueOf(charge.getAmount()/CURRENCY_FACTOR));
//        userTransaction.setPaymentId(charge.getId());
//        userTransaction.setTransactionType(TransactionType.DEBIT.toString());
//        userTransaction.setTask(task);
//        userTransaction = userTransactionsRepository.save(userTransaction);
//        //task.setUserTransactions(userTransaction);
//        taskRepository.save(task);
//        return userTransaction;
//    }
//
//    @Override
//    public UserTransactions transferMoneyFromPlatformToTasker(Task task) throws StripeException {
//
//        Long amount = Long.valueOf(Math.round(task.getBudget().floatValue() * CURRENCY_FACTOR));
//        int serviceFee = Math.round(task.getBudget().floatValue() *  SERVICE_FEE_FACTOR * CURRENCY_FACTOR);
//        String taskerConnectId = task.getTasker().getConnectId();
//        //Destination Charges
//        ArrayList paymentMethodTypes = new ArrayList();
//        paymentMethodTypes.add("card");
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("payment_method_types", paymentMethodTypes);
//        params.put("amount", amount);
//        params.put("currency", "usd");
//        params.put("application_fee_amount", serviceFee);
//        Map<String, Object> transferDataParams = new HashMap<>();
//        transferDataParams.put("destination", taskerConnectId);
//        params.put("transfer_data", transferDataParams);
//        PaymentIntent paymentIntent = PaymentIntent.create(params);
//
//        //Creating Receipt -- For Tasker
//        UserTransactions taskerTransaction = new UserTransactions();
//        taskerTransaction.setPublicId(PublicIdGenerator.generatePublicId());
//        taskerTransaction.setUser(task.getTasker());
//        taskerTransaction.setPaymentId(paymentIntent.getId());
//
//        taskerTransaction.setPurpose(task.getTaskStatus());
//        taskerTransaction.setDescription(task.getDescription());
//        taskerTransaction.setCategory(task.getTaskCategory().getName());
//
//        taskerTransaction.setAmount(BigDecimal.valueOf(paymentIntent.getAmount()/ CURRENCY_FACTOR));
//        taskerTransaction.setTransactionType(TransactionType.CREDIT.toString());
//        taskerTransaction.setTask(task);
//        return userTransactionsRepository.save(taskerTransaction);
//    }

    @Override
    public Map<String, Object> retrieveBalance(User user) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Account account = getConnectAccount(user.getConnectId());
        account.getSettings().getPayouts().getSchedule();
        RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(user.getConnectId()).build();
        Balance balance =  Balance.retrieve(requestOptions);

        Map<String, Object> response = new HashMap<>();
        response.put("balance", balance);
        response.put("paymentFrequency", account.getSettings().getPayouts().getSchedule());

        return response;
    }

    @Override
    public Account setPaymentFrequency(User user, String interval, int weeklyAnchor, int monthlyAnchor, int delayDays) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;
        RequestOptions requestOptions = RequestOptions.builder().setStripeAccount(user.getConnectId()).build();
        Account account = Account.retrieve(requestOptions);

        Map<String, Object> updateParams = new HashMap<>();
        Map<String, Object> settings = new HashMap<>();
        Map<String, Object> payouts = new HashMap<>();
        Map<String, Object> schedule = new HashMap<>();

        //Daily Settings
        if(interval.equals(Intervals.DAILY.toString())){
            if(delayDays > 0 && delayDays <= 14){
                schedule.put("delay_days", Long.valueOf(delayDays));
            } else {
                schedule.put("delay_days", Long.valueOf(14));
            }

            schedule.put("interval", "daily");
        }

        //Weekly Settings
        if(interval.equals(Intervals.WEEKLY.toString())){
            schedule.put("interval", "weekly");
            switch (weeklyAnchor){
                case 1:
                    schedule.put("weekly_anchor", "monday");
                    break;
                case 2:
                    schedule.put("weekly_anchor", "tuesday");
                    break;
                case 3:
                    schedule.put("weekly_anchor", "wednesday");
                    break;
                case 4:
                    schedule.put("weekly_anchor", "thursday");
                    break;
                case 5:
                    schedule.put("weekly_anchor", "friday");
                    break;
                case 6:
                    schedule.put("weekly_anchor", "saturday");
                    break;
                case 7:
                    schedule.put("weekly_anchor", "sunday");
                    break;
                default:
                    schedule.put("weekly_anchor", "wednesday");
            }

        }

        //Monthly setting
        if(interval.equals(Intervals.MONTHLY.toString())){
            if(monthlyAnchor > 0 && monthlyAnchor <= 31){
                schedule.put("monthly_anchor", Long.valueOf(monthlyAnchor));
            } else {
                schedule.put("monthly_anchor", Long.valueOf(27));
            }

            schedule.put("interval", "monthly");
        }

        //Setting changes
        payouts.put("schedule", schedule);
        settings.put("payouts", payouts);
        updateParams.put("settings", settings);
        Account updatedAccount = account.update(updateParams);

        return updatedAccount;
    }

    @Override
    public BalanceTransaction getBalanceTransactionDebit(String paymentId) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;
        Charge charge = Charge.retrieve(paymentId);
        BalanceTransaction balanceTransaction = BalanceTransaction.retrieve(charge.getBalanceTransaction());
        return balanceTransaction;
    }

    @Override
    public BalanceTransaction getBalanceTransactionCredit(String paymentId) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;
        Charge charge = Charge.retrieve(paymentId);
        BalanceTransaction balanceTransaction = BalanceTransaction.retrieve(charge.getBalanceTransaction());
        return balanceTransaction;
    }

    @Override
    public Card editCard(User user, UserPaymentCard userPaymentCard, AddPaymentCardRequest addPaymentCardRequest) throws StripeException {

        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Account account = getConnectAccount(user.getConnectId());
        Card card = (Card) account.getExternalAccounts().retrieve(userPaymentCard.getConnectSourceId());

        Customer customer = Customer.retrieve(user.getStripeId());
        Card stripeCard = (Card) customer.getSources().retrieve(userPaymentCard.getStripeSourceId());

        Map<String, Object> params = new HashMap<>();
        params.put("exp_month", addPaymentCardRequest.getExpMonth());
        params.put("exp_year", addPaymentCardRequest.getExpYear());

        Card updatedCard = (Card) card.update(params);
        Card updatedStripeCard = (Card) stripeCard.update(params);

        return updatedStripeCard;
    }

    @Override
    public BankAccount editBankAccount(User user, UserBankAccount userBankAccount, AddBankAccountRequest addBankAccountRequest) throws StripeException {

        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        Account account = getConnectAccount(user.getConnectId());
        BankAccount bankAccount = (BankAccount) account.getExternalAccounts().retrieve(userBankAccount.getStripeSourceId());

        Map<String, Object> params = new HashMap<>();
        params.put("account_holder_name", addBankAccountRequest.getAccountHolderName());
//        params.put("account_number", addBankAccountRequest.getAccountNumber());
        //routing number = Transit Number(5 digits) - Financial Institute Number(3 Digits)
//        params.put("routing_number", addBankAccountRequest.getTransitNumber() +"-"+ addBankAccountRequest.getFinancialInstitutionNumber());

        BankAccount updatedBankAccount = (BankAccount) bankAccount.update(params);
        return bankAccount;
    }

    @Override
    public Payout subtractMoneyAndSendItToUserPaymentOption(RedeemAmount redeemAmount, User user) throws StripeException {
        Stripe.apiKey = API_SECRET_KEY;
        Stripe.clientId = API_CLIENT_KEY;

        PayoutCreateParams params =
                PayoutCreateParams.builder()
                        .setAmount(redeemAmount.getAmount())
                        .setCurrency("usd")
                        .build();

        RequestOptions requestOptions =
                RequestOptions.builder()
                        .setStripeAccount(user.getConnectId())
                        .build();

        Payout payout = Payout.create(params, requestOptions);

        return payout;
    }
}
