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
@Table(name = "logs", schema = "public")
public class Log extends BaseEntity  implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, nullable = false, name ="ip_address")
    private String ipAddress = "127.0.0.1";

    @Column(name ="request")
    private String request;

    @Column(name ="response")
    private String response;

    @Column(nullable = false, name ="is_deleted")
    private boolean isDeleted = false;

    @Column(nullable = false, name ="user_public_id")
    private Long userPublicId;

    public Log() {}
}
