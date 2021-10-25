package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.Award;
import com.app.middleware.persistence.domain.UserUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserUploadRepository extends JpaRepository<UserUpload,Long> {
    UserUpload findByPublicId(Long decodePublicId);

    UserUpload findByKeyName(String keyName);
}
