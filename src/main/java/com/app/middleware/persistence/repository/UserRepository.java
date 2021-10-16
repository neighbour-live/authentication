package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.User;
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

	User findByUserName(String userName);

	User findByEmailIgnoreCase(String email);

	List<User> findByEmailVerified(Boolean emailVerified);

	List<User> findByPhoneVerified(Boolean phoneVerified);

}
