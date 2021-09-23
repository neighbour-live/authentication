package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserTemporary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;

public interface UserTempRepository extends JpaRepository<UserTemporary,Long> {

	UserTemporary findByUser(User user);

	UserTemporary findByPhoneNumber(String phone);

	UserTemporary findByEmailIgnoreCase(String email);

    Collection<UserTemporary> findAllByUpdateDateTimeBefore(ZonedDateTime updateDateTime);

}
