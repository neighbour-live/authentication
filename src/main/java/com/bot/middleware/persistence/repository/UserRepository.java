package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

	User findByPublicId(Long publicId);

	User findByEmailVerificationToken(String emailVerificationToken);

	User findByPhoneVerificationToken(String phoneVerificationToken);

	@Query(value = "SELECT u FROM User u WHERE u.isDeleted = false AND u.email = :email")
	User findUserByEmail(String email);

	Optional<User> findByEmail(String email);

	User findByPhoneNumber(String phone);

	@Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.userPaymentCards upc WHERE upc.cardNumber = :card")
	User findByUserPaymentCards(Long card);

	@Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.userBankAccounts uba WHERE uba.accountNumber = :accountNumber")
	User findByUserBankAccounts(String accountNumber);

	User findByEmailIgnoreCase(String email);

	@Query( countQuery = "SELECT COUNT (u) FROM User u " +
			"WHERE u.isDeleted = false AND u.phoneVerified = true AND u.emailVerified = true AND u.cardVerified = true AND u.sterlingBackgroundVerified = true " +
			"AND lower(u.city) like lower(concat('%', :city,'%')) ",
			value =
			"SELECT u FROM User u " +
			"WHERE u.isDeleted = false AND u.phoneVerified = true AND u.emailVerified = true AND u.cardVerified = true AND u.sterlingBackgroundVerified = true " +
			"AND lower(u.city) like lower(concat('%', :city,'%')) "
	)
	Page<User> getAllFeaturedTaskers(String city, Pageable pageable);

	List<User> findByEmailVerified(Boolean emailVerified);

	List<User> findByCardVerified(Boolean cardVerified);

	List<User> findByPhoneVerified(Boolean phoneVerified);

	List<User> findBySterlingBackgroundVerified(Boolean sterlingBackgroundVerified);
}
