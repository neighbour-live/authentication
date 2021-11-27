package com.app.middleware.persistence.mapper;

import com.app.middleware.persistence.domain.UserUpload;
import com.app.middleware.persistence.dto.UserUploadDTO;
import com.app.middleware.persistence.type.ServiceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserUploadMapper {

    public static UserUploadDTO createUserUploadDTOLazy(UserUpload userUpload) {
        UserUploadDTO userUploadDTO = UserUploadDTO.builder()
                .publicId(userUpload.getPublicId())
                .keyName(userUpload.getKeyName())
                .serviceType(String.valueOf(ServiceType.valueOf(userUpload.getServiceType())))
                .publicUrl(userUpload.getPublicUrl())
                .build();

        return userUploadDTO;
    }

    public static List<UserUploadDTO> createUserUploadDTOListLazy(Collection<UserUpload> userUploads) {
        List<UserUploadDTO> userUploadDTOS = new ArrayList<>();
        userUploads.forEach(award -> userUploadDTOS.add(createUserUploadDTOLazy(award)));
        return userUploadDTOS;
    }
}
