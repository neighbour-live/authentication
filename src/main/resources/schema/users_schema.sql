-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Generation Time: Sep 26, 2021 at 04:02 PM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `users_schema`
--

-- --------------------------------------------------------

--
-- Table structure for table `awards`
--

CREATE TABLE `awards` (
                          `id` bigint(20) NOT NULL,
                          `create_date_time` datetime DEFAULT NULL,
                          `update_date_time` datetime DEFAULT NULL,
                          `award_icon` varchar(255) NOT NULL,
                          `award_type` ENUM(  'STARTER',
                              'EARLY_USER',
                              'SPENT_THOUSAND_DOLLARS',
                              'EARNED_THOUSAND_DOLLARS',
                              'SPENT_TEN_THOUSANDS_DOLLARS',
                              'EARNED_TEN_THOUSANDS_DOLLARS',
                              'SPENT_MILLION',
                              'EARNED_MILLION'),
                          `description` varchar(255) NOT NULL,
                          `public_id` bigint(20) NOT NULL,
                          `title` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `conversations`
--

CREATE TABLE `conversations` (
                                 `id` bigint(20) NOT NULL,
                                 `create_date_time` datetime DEFAULT NULL,
                                 `update_date_time` datetime DEFAULT NULL,
                                 `is_active` bit(1) NOT NULL,
                                 `is_deleted` bit(1) NOT NULL,
                                 `public_id` bigint(20) NOT NULL,
                                 `first_user_id` bigint(20) DEFAULT NULL,
                                 `second_user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `firebase_logs`
--

CREATE TABLE `firebase_logs` (
                                 `id` bigint(20) NOT NULL,
                                 `create_date_time` datetime DEFAULT NULL,
                                 `update_date_time` datetime DEFAULT NULL,
                                 `error_msg` text DEFAULT NULL,
                                 `payload` text DEFAULT NULL,
                                 `request_id` varchar(255) DEFAULT NULL,
                                 `request_status` ENUM('PENDING', 'SUCCESS', 'FAILED'),
                                 `request_type_id` bigint(20) NOT NULL,
                                 `user_id` bigint(20) DEFAULT NULL,
                                 `user_notification_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
    `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES (1);

-- --------------------------------------------------------

--
-- Table structure for table `logs`
--

CREATE TABLE `logs` (
                        `id` bigint(20) NOT NULL,
                        `create_date_time` datetime DEFAULT NULL,
                        `update_date_time` datetime DEFAULT NULL,
                        `ip_address` varchar(255) NOT NULL,
                        `is_deleted` bit(1) NOT NULL,
                        `request` varchar(255) DEFAULT NULL,
                        `response` varchar(255) DEFAULT NULL,
                        `user_public_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `notification_types`
--

CREATE TABLE `notification_types` (
                                      `id` bigint(20) NOT NULL,
                                      `create_date_time` datetime DEFAULT NULL,
                                      `update_date_time` datetime DEFAULT NULL,
                                      `description` varchar(255) DEFAULT NULL,
                                      `icon_path` varchar(255) DEFAULT NULL,
                                      `is_deleted` bit(1) DEFAULT NULL,
                                      `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `permission`
--

CREATE TABLE `permission` (
                              `id` bigint(20) NOT NULL,
                              `create_date_time` datetime DEFAULT NULL,
                              `update_date_time` datetime DEFAULT NULL,
                              `is_deleted` bit(1) NOT NULL,
                              `permission_code` varchar(255) NOT NULL,
                              `permission_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
                        `id` bigint(20) NOT NULL,
                        `create_date_time` datetime DEFAULT NULL,
                        `update_date_time` datetime DEFAULT NULL,
                        `is_deleted` bit(1) NOT NULL,
                        `role_name` varchar(255) NOT NULL,
                        `role_type` ENUM('USER', 'MODERATOR', 'ADMIN')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `role_permission`
--

CREATE TABLE `role_permission` (
                                   `id` bigint(20) NOT NULL,
                                   `is_deleted` bit(1) NOT NULL,
                                   `permission_id` bigint(20) DEFAULT NULL,
                                   `role_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
                         `id` bigint(20) NOT NULL,
                         `create_date_time` datetime DEFAULT NULL,
                         `update_date_time` datetime DEFAULT NULL,
                         `access_token` varchar(255) DEFAULT NULL,
                         `address_line` varchar(255) DEFAULT NULL,
                         `address_type` ENUM('RESIDENTIAL', 'WORKPLACE', 'TEMPORARY', 'PERMANENT'),
                         `apartment_address` varchar(255) DEFAULT NULL,
                         `city` varchar(255) DEFAULT NULL,
                         `country` varchar(255) DEFAULT NULL,
                         `country_short` varchar(255) DEFAULT NULL,
                         `credentials_try_count` int(11) DEFAULT NULL,
                         `currency` varchar(255) DEFAULT NULL,
                         `dob` varchar(255) DEFAULT NULL,
                         `email` varchar(255) DEFAULT NULL,
                         `email_token_creation_time` datetime DEFAULT NULL,
                         `email_verification_token` varchar(255) DEFAULT NULL,
                         `email_verified` bit(1) DEFAULT NULL,
                         `firebase_key` varchar(255) DEFAULT NULL,
                         `first_name` varchar(255) DEFAULT NULL,
                         `image_url` varchar(255) DEFAULT NULL,
                         `ip` varchar(255) DEFAULT NULL,
                         `is_blocked` bit(1) DEFAULT NULL,
                         `is_deleted` bit(1) DEFAULT NULL,
                         `is_suspended` bit(1) DEFAULT NULL,
                         `last_name` varchar(255) DEFAULT NULL,
                         `lat` float DEFAULT NULL,
                         `lng` float DEFAULT NULL,
                         `otp_creation_time` datetime DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `phone_number` varchar(255) DEFAULT NULL,
                         `phone_verification_otp` varchar(255) DEFAULT NULL,
                         `phone_verification_token` varchar(255) DEFAULT NULL,
                         `phone_verified` bit(1) DEFAULT NULL,
                         `postal_code` varchar(255) DEFAULT NULL,
                         `provider` varchar(255) DEFAULT NULL,
                         `provider_id` varchar(255) DEFAULT NULL,
                         `public_id` bigint(20) DEFAULT NULL,
                         `refresh_token` varchar(255) DEFAULT NULL,
                         `state` varchar(255) DEFAULT NULL,
                         `role_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user_addresses`
--

CREATE TABLE `user_addresses` (
                                  `id` bigint(20) NOT NULL,
                                  `create_date_time` datetime DEFAULT NULL,
                                  `update_date_time` datetime DEFAULT NULL,
                                  `address_line` varchar(255) NOT NULL,
                                  `address_type` ENUM('RESIDENTIAL', 'WORKPLACE', 'TEMPORARY', 'PERMANENT'),
                                  `apartment_address` varchar(255) NOT NULL,
                                  `city` varchar(255) NOT NULL,
                                  `country` varchar(255) NOT NULL,
                                  `is_deleted` bit(1) NOT NULL,
                                  `lat` float DEFAULT NULL,
                                  `lng` float DEFAULT NULL,
                                  `postal_code` varchar(255) NOT NULL,
                                  `public_id` bigint(20) NOT NULL,
                                  `state` varchar(255) NOT NULL,
                                  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user_awards`
--

CREATE TABLE `user_awards` (
                               `id` bigint(20) NOT NULL,
                               `create_date_time` datetime DEFAULT NULL,
                               `update_date_time` datetime DEFAULT NULL,
                               `is_active` bit(1) NOT NULL,
                               `is_unlocked` bit(1) NOT NULL,
                               `progress` int(11) NOT NULL,
                               `public_id` bigint(20) NOT NULL,
                               `award_id` bigint(20) DEFAULT NULL,
                               `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user_chats`
--

CREATE TABLE `user_chats` (
                              `id` bigint(20) NOT NULL,
                              `create_date_time` datetime DEFAULT NULL,
                              `update_date_time` datetime DEFAULT NULL,
                              `is_active` bit(1) NOT NULL,
                              `is_deleted` bit(1) NOT NULL,
                              `message` varchar(255) NOT NULL,
                              `message_attributes` varchar(255) NOT NULL,
                              `public_id` bigint(20) NOT NULL,
                              `conversation_public_id` bigint(20) DEFAULT NULL,
                              `receiver_public_id` bigint(20) DEFAULT NULL,
                              `sender_public_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user_notifications`
--

CREATE TABLE `user_notifications` (
                                      `id` bigint(20) NOT NULL,
                                      `create_date_time` datetime DEFAULT NULL,
                                      `update_date_time` datetime DEFAULT NULL,
                                      `actions` varchar(255) DEFAULT NULL,
                                      `actions_info` varchar(255) DEFAULT NULL,
                                      `is_archived` bit(1) DEFAULT NULL,
                                      `is_deleted` bit(1) DEFAULT NULL,
                                      `is_read` bit(1) DEFAULT NULL,
                                      `notification_image` varchar(255) DEFAULT NULL,
                                      `notification_source` varchar(255) DEFAULT NULL,
                                      `notification_text` varchar(255) NOT NULL,
                                      `notification_title` varchar(255) NOT NULL,
                                      `public_id` bigint(20) DEFAULT NULL,
                                      `notification_type_id` bigint(20) NOT NULL,
                                      `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user_temporary`
--

CREATE TABLE `user_temporary` (
                                  `id` bigint(20) NOT NULL,
                                  `create_date_time` datetime DEFAULT NULL,
                                  `update_date_time` datetime DEFAULT NULL,
                                  `email` varchar(255) DEFAULT NULL,
                                  `password` varchar(255) DEFAULT NULL,
                                  `phone_number` varchar(255) DEFAULT NULL,
                                  `user_public_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `awards`
--
ALTER TABLE `awards`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK_awards_public_id` (`public_id`) USING BTREE;

--
-- Indexes for table `conversations`
--
ALTER TABLE `conversations`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK_conversations_publid_id` (`public_id`) USING BTREE,
    ADD KEY `FK_conversations_second_user_id` (`second_user_id`) USING BTREE,
    ADD KEY `FK_conversations_first_user_id` (`first_user_id`) USING BTREE;

--
-- Indexes for table `firebase_logs`
--
ALTER TABLE `firebase_logs`
    ADD PRIMARY KEY (`id`),
    ADD KEY `FK_fire_logs_user_id` (`user_id`) USING BTREE,
    ADD KEY `FK_fire_logs_notification_type_id` (`request_type_id`) USING BTREE,
    ADD KEY `FK_fire_logs_user_notification_id` (`user_notification_id`) USING BTREE;

--
-- Indexes for table `logs`
--
ALTER TABLE `logs`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notification_types`
--
ALTER TABLE `notification_types`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `permission`
--
ALTER TABLE `permission`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `role_permission`
--
ALTER TABLE `role_permission`
    ADD PRIMARY KEY (`id`),
    ADD KEY `FK_rp_role_id` (`role_id`) USING BTREE,
    ADD KEY `FK_rp_permission_id` (`permission_id`) USING BTREE;

--
-- Indexes for table `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK_user_email` (`email`) USING BTREE,
    ADD UNIQUE KEY `UK_user_refresh_token` (`refresh_token`) USING BTREE,
    ADD UNIQUE KEY `UK_user_phone` (`phone_number`) USING BTREE,
    ADD UNIQUE KEY `UK_user_access_token` (`access_token`) USING BTREE,
    ADD UNIQUE KEY `UK_user_public_id` (`public_id`) USING BTREE,
    ADD UNIQUE KEY `UK_user_phone_verification_token` (`phone_verification_token`) USING BTREE,
    ADD UNIQUE KEY `UK_user_email_verification_token` (`email_verification_token`) USING BTREE,
    ADD KEY `FK_user_role_id` (`role_id`) USING BTREE;

--
-- Indexes for table `user_addresses`
--
ALTER TABLE `user_addresses`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK_user_address_public_id` (`public_id`) USING BTREE,
    ADD KEY `FK_user_address_user_id` (`user_id`) USING BTREE;

--
-- Indexes for table `user_awards`
--
ALTER TABLE `user_awards`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK_user_awards_publid_id` (`public_id`) USING BTREE,
    ADD KEY `FK_user_awards_award_id` (`award_id`) USING BTREE,
    ADD KEY `FK_user_awards_user_id` (`user_id`) USING BTREE;

--
-- Indexes for table `user_chats`
--
ALTER TABLE `user_chats`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK_user_chats_public_id` (`public_id`) USING BTREE,
    ADD KEY `FK_user_chats_sender_id` (`sender_public_id`) USING BTREE,
    ADD KEY `FK_user_chats_receiver_id` (`receiver_public_id`) USING BTREE,
    ADD KEY `FK_user_chats_conversation_id` (`conversation_public_id`) USING BTREE;

--
-- Indexes for table `user_notifications`
--
ALTER TABLE `user_notifications`
    ADD PRIMARY KEY (`id`),
    ADD KEY `FK_user_notifications_user_id` (`user_id`) USING BTREE,
    ADD KEY `FK_user_notifications_notification_type_id` (`notification_type_id`) USING BTREE;

--
-- Indexes for table `user_temporary`
--
ALTER TABLE `user_temporary`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK_user_temporary_phone` (`phone_number`) USING BTREE,
    ADD UNIQUE KEY `UK_user_temporary_email` (`email`) USING BTREE,
    ADD KEY `FK_user_temporary_user_id` (`user_public_id`) USING BTREE,
    ADD KEY `INDEX_user_temporary_password` (`password`) USING BTREE;

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `firebase_logs`
--
ALTER TABLE `firebase_logs`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notification_types`
--
ALTER TABLE `notification_types`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user_notifications`
--
ALTER TABLE `user_notifications`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `conversations`
--
ALTER TABLE `conversations`
    ADD CONSTRAINT `FK_conversations_first_user_id` FOREIGN KEY (`first_user_id`) REFERENCES `users` (`public_id`),
    ADD CONSTRAINT `FK_conversations_second_user_id` FOREIGN KEY (`second_user_id`) REFERENCES `users` (`public_id`);

--
-- Constraints for table `firebase_logs`
--
ALTER TABLE `firebase_logs`
    ADD CONSTRAINT `FK_conversations_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    ADD CONSTRAINT `FK_conversations_user_notification_id` FOREIGN KEY (`user_notification_id`) REFERENCES `user_notifications` (`id`),
    ADD CONSTRAINT `FK_conversations_request_type_id` FOREIGN KEY (`request_type_id`) REFERENCES `notification_types` (`id`);

--
-- Constraints for table `role_permission`
--
ALTER TABLE `role_permission`
    ADD CONSTRAINT `FK_role_permission_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
    ADD CONSTRAINT `FK_role_permission_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
    ADD CONSTRAINT `FK_users_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);

--
-- Constraints for table `user_addresses`
--
ALTER TABLE `user_addresses`
    ADD CONSTRAINT `FK_user_addresses_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`public_id`);

--
-- Constraints for table `user_awards`
--
ALTER TABLE `user_awards`
    ADD CONSTRAINT `FK_user_awards_award_id` FOREIGN KEY (`award_id`) REFERENCES `awards` (`public_id`),
    ADD CONSTRAINT `FK_user_awards_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`public_id`);

--
-- Constraints for table `user_chats`
--
ALTER TABLE `user_chats`
    ADD CONSTRAINT `FK_user_chats_conversation_public_id` FOREIGN KEY (`conversation_public_id`) REFERENCES `conversations` (`public_id`),
    ADD CONSTRAINT `FK_user_chats_receiver_public_id` FOREIGN KEY (`receiver_public_id`) REFERENCES `users` (`public_id`),
    ADD CONSTRAINT `FK_user_chats_sender_public_id` FOREIGN KEY (`sender_public_id`) REFERENCES `users` (`public_id`);

--
-- Constraints for table `user_notifications`
--
ALTER TABLE `user_notifications`
    ADD CONSTRAINT `FK_user_notifications_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    ADD CONSTRAINT `FK_user_notifications_notification_type_id` FOREIGN KEY (`notification_type_id`) REFERENCES `notification_types` (`id`);

--
-- Constraints for table `user_temporary`
--
ALTER TABLE `user_temporary`
    ADD CONSTRAINT `FK_user_temporary_user_public_id` FOREIGN KEY (`user_public_id`) REFERENCES `users` (`public_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;