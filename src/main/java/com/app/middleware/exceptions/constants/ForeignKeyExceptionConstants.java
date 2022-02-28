package com.app.middleware.exceptions.constants;

public class ForeignKeyExceptionConstants {


    public static final String FK_user_awards_award_id = "FK_user_awards_award_id";
    public static final String FK_user_awards_user_id = "FK_user_awards_user_id";

    public static final String FK_user_notifications_user_id = "FK_user_notifications_user_id";
    public static final String FK_user_notifications_notification_type_id = "FK_user_notifications_notification_type_id";

    public static final String FK_user_temporary_user_public_id = "FK_user_temporary_user_public_id";

    public static final String FK_conversations_first_user_id = "FK_conversations_first_user_id";
    public static final String FK_conversations_second_user_id = "FK_conversations_second_user_id";
    public static final String FK_conversations_user_id = "FK_conversations_user_id";
    public static final String FK_conversations_user_notification_id = "FK_conversations_user_notification_id";
    public static final String FK_conversations_request_type_id = "FK_conversations_request_type_id";

    public static final String FK_role_permission_role_id = "FK_role_permission_role_id";
    public static final String FK_role_permission_permission_id = "FK_role_permission_permission_id";

    public static final String FK_users_role_id = "FK_users_role_id";

    public static final String FK_user_addresses_user_id = "FK_user_addresses_user_id";

    public static final String FK_user_chats_conversation_public_id = "FK_user_chats_conversation_public_id";
    public static final String FK_user_chats_receiver_public_id = "FK_user_chats_receiver_public_id";
    public static final String FK_user_chats_sender_public_id = "FK_user_chats_sender_public_id";

    public static final String FK_user_uploads_public_id = "FK_user_uploads_public_id";
}
