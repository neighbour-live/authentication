package com.bot.middleware.persistence.type;

public enum NotificationAction {
    HOME,
    ADDED_BID_PAGE, // taskId, bidId
    EDITED_BID_PAGE, // taskId, bidId
    DELETED_BID_PAGE, // taskId, bidId
    ACCEPTED_BID_PAGE, // taskId, bidId
    REJECTED_BID_PAGE, // taskId, bidId
    EDITED_TASK_PAGE, //taskId
    ASSIGNED_TASK_PAGE, //taskId
    IN_PROGRESS_TASK_PAGE, //taskId
    COMPLETED_TASK_PAGE, //taskId
    FUNDED_TASK_PAGE, //taskId
    PAID_TASK_PAGE, //taskId, transactionId
    TASK_RESCHEDULE_REQUEST_PAGE, // taskId
    TASK_RESCHEDULE_ACCEPTED_PAGE, // taskId
    TASK_RESCHEDULE_REJECTED_PAGE, // taskId
    CONVERSATION_PAGE, // ConversationID, TaskID, ChatID
}
