package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.UserTemporary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.Collection;

public interface UserTempRepository extends JpaRepository<UserTemporary,Long> {

	UserTemporary findByPhoneNumber(String phone);

	UserTemporary findByEmailIgnoreCase(String email);

    Collection<UserTemporary> findAllByUpdateDateTimeBefore(ZonedDateTime updateDateTime);

    UserTemporary findByPublicId(Long publicId);
}
