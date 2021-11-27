package com.app.middleware.resources.services;

import com.app.middleware.persistence.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    User save(User user);

    User findByPublicId(Long decodePublicId);

    User uploadIdentificationDocuments(User user, MultipartFile front, MultipartFile back) throws Exception;

    Optional<User> findById(Long id);
}
