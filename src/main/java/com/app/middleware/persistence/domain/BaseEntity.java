package com.app.middleware.persistence.domain;

import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.*;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {

    @Column(name = "create_date_time")
    @CreationTimestamp
    private ZonedDateTime createDateTime;

    @Column(name = "update_date_time")
    @UpdateTimestamp
    private ZonedDateTime updateDateTime;
}
