package com.app.middleware.persistence.domain;

import com.app.middleware.persistence.type.NotificationSource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_notifications", schema = "public")
public class UserNotification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "public_id")
    private Long publicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_type_id", nullable = false)
    private NotificationType notificationType;

    @OneToMany(mappedBy = "userNotification", fetch = FetchType.LAZY)
    @Builder.Default
    private List<FirebaseLog> firebaseLogs = new ArrayList<>();

    @Column(name = "notification_text", nullable = false)
    private String notificationText;

    @Column(name = "notification_title", nullable = false)
    private String notificationTitle;

    @Column(name = "notification_image")
    private String notificationImage;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "is_archived")
    @Builder.Default
    private Boolean isArchived = false;

    @Column(name = "actions")
    private String actions;

    @Column(name = "actions_info")
    private String actionsInfo;

    @Column(name = "notification_source")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private NotificationSource notificationSource = NotificationSource.DEFAULT;
}
