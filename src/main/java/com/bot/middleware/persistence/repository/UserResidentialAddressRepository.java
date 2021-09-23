package com.bot.middleware.persistence.repository;

import com.bot.middleware.persistence.domain.User;
import com.bot.middleware.persistence.domain.UserAddress;
import com.bot.middleware.persistence.domain.UserCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserResidentialAddressRepository extends JpaRepository<UserAddress,Long> {

    @Query(value = "SELECT ua FROM UserAddress ua WHERE ua.user = :user AND ua.isDeleted = false AND ua.addressType ='PERMANENT'")
    UserAddress getByUser(User user);

    @Query(value = "SELECT ua FROM UserAddress ua WHERE ua.isDeleted = false AND ua.publicId = :publicId")
    UserAddress findByPublicId(Long publicId);

    @Query(value = "SELECT ua FROM UserAddress ua WHERE ua.isDeleted = false AND ua.user.publicId = :userPublicId")
    List<UserAddress> findAllByUserAndIsDeletedIsFalse(Long userPublicId);

    @Modifying
    @Query(value = "UPDATE UserAddress ua SET ua.isDeleted = true WHERE ua.publicId =:userAddressPublicId")
    Integer deleteUserAddress(Long userAddressPublicId);
}
