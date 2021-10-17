package com.app.middleware.resources.services;

import com.app.middleware.persistence.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User save(User user);

    User findByPublicId(Long decodePublicId);

    User uploadIdentificationDocuments(User user, MultipartFile front, MultipartFile back) throws Exception;
}
