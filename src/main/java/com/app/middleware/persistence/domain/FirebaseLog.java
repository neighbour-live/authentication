package com.app.middleware.persistence.domain;

import com.app.middleware.persistence.type.RequestStatus;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "firebase_logs", schema = "public")
public class FirebaseLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_type_id", nullable = false)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_notification_id", nullable = false)
    private UserNotification userNotification;

    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @Column(name = "payload", columnDefinition="TEXT")
    private String payload;

    @Column(name = "error_msg", columnDefinition="TEXT")
    private String errorMsg;

    @Column(name = "request_id")
    private String requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public FirebaseLog(UserNotification userNotification, NotificationType notificationType, String payload, String requestId, User user) {
        this.userNotification = userNotification;
        this.notificationType = notificationType;
        this.requestStatus = RequestStatus.PENDING;
        this.payload = payload;
        this.errorMsg = null;
        this.requestId = requestId;
        this.user = user;
    }
}
