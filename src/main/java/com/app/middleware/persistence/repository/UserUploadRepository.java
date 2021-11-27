package com.app.middleware.persistence.repository;

import com.app.middleware.persistence.domain.UserUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserUploadRepository extends JpaRepository<UserUpload,Long> {
    UserUpload findByPublicId(Long decodePublicId);

    UserUpload findByKeyName(String keyName);

    @Query("select uu from UserUpload uu where uu.publicId = :publicId")
    List<UserUpload> findAllByPublicId(Long publicId);
}
