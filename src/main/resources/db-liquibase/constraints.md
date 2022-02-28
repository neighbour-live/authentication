# Unique Key Constraint

----------------------------------------------
- UK_user_email
- UK_user_user_name
- UK_user_refresh_token
- UK_user_phone
- UK_user_access_token
- UK_user_public_id
- UK_user_phone_verification_token
- UK_user_email_verification_token
---------------------------------------------
- UK_user_address_public_id
---------------------------------------------
- UK_user_awards_publid_id
---------------------------------------------
- UK_awards_public_id
---------------------------------------------
- UK_user_chats_public_id
---------------------------------------------
- UK_conversations_publid_id
---------------------------------------------
- UK_user_temporary_phone
- UK_user_temporary_email
- UK_user_temporary_user_name
---------------------------------------------
- UK_user_upload_public_id
---------------------------------------------


# Foreign Key Constraint
---------------------------------------------
- FK_fire_logs_user_id
- FK_fire_logs_notification_type_id
- FK_fire_logs_user_notification_id
---------------------------------------------
- FK_rp_role_id
---------------------------------------------
- FK_rp_permission_id
---------------------------------------------
- FK_user_role_id
---------------------------------------------
- FK_user_address_user_id
---------------------------------------------
- FK_user_awards_award_id
- FK_user_awards_user_id
---------------------------------------------
- FK_user_chats_sender_id
- FK_user_chats_receiver_id
- FK_user_chats_conversation_id
---------------------------------------------
- FK_user_notifications_user_id
- FK_user_notifications_notification_type_id
---------------------------------------------
- FK_user_temporary_user_id
- FK_user_temporary_user_public_id
---------------------------------------------
- FK_conversations_first_user_id
- FK_conversations_second_user_id
- FK_conversations_user_id
- FK_conversations_user_notification_id
- FK_conversations_request_type_id
---------------------------------------------
- FK_role_permission_role_id
- FK_role_permission_permission_id
---------------------------------------------
- FK_users_role_id
---------------------------------------------
- FK_user_addresses_user_id
---------------------------------------------
- FK_user_chats_conversation_public_id
- FK_user_chats_receiver_public_id
- FK_user_chats_sender_public_id
---------------------------------------------
- FK_user_uploads_public_id
---------------------------------------------



# TO BE ADDED 
Hibernate: alter table awards drop index UK_553xyershkjrsjdgx510w6kf7
Hibernate: alter table awards add constraint UK_553xyershkjrsjdgx510w6kf7 unique (public_id)
Hibernate: alter table conversations drop index UK_q07914pcv9kk9toltnas25mrh
Hibernate: alter table conversations add constraint UK_q07914pcv9kk9toltnas25mrh unique (public_id)
Hibernate: alter table user_temporary drop index UK_m4buelcm4waa5cpybal3rxvvr
Hibernate: alter table user_temporary add constraint UK_m4buelcm4waa5cpybal3rxvvr unique (public_id)
Hibernate: alter table users drop index UK_s24bux761rbgowsl7a4b386ba
Hibernate: alter table users add constraint UK_s24bux761rbgowsl7a4b386ba unique (public_id)
