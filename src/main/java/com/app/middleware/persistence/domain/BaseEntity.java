package com.app.middleware.persistence.domain;

import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {

    @Column(name = "create_date_time")
    @CreationTimestamp
    private ZonedDateTime createDateTime = ZonedDateTime.now();

    @Column(name = "update_date_time")
    @UpdateTimestamp
    private ZonedDateTime updateDateTime = ZonedDateTime.now();
}
