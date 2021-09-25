package com.app.middleware.persistence.domain;

import com.app.middleware.persistence.type.NotificationEnum;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notification_types", schema = "public")
public class NotificationType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name")
    @Builder.Default
    private String name = NotificationEnum.DEFAULT.name();

    @Column(name = "description")
    private String desc;

    @Column(name = "icon_path")
    private String iconPath;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

}
