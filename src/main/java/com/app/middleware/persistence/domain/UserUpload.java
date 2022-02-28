package com.app.middleware.persistence.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "user_uploads", schema = "public")
public class UserUpload extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name ="key_name")
    private String keyName;

    @Column(name ="public_url")
    private String publicUrl;

    @Column(nullable = false, name ="is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(nullable = false, name ="service_type")
    private String serviceType;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="user_id", referencedColumnName="public_id")
    private User user;

    public UserUpload(){

    }
}
