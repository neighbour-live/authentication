package com.app.middleware.persistence.domain;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "user_chats", schema = "public")
public class UserChat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name ="is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(nullable = false, name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, name ="message")
    private String message;

    @Column(nullable = false, name ="message_attributes")
    private String messageAttributes;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name = "sender_public_id", referencedColumnName = "public_id")
    private User sender;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name = "receiver_public_id", referencedColumnName = "public_id")
    private User receiver;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="conversation_public_id",  referencedColumnName="public_id")
    private Conversation conversation;

    public UserChat() {}
}