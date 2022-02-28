package com.app.middleware.persistence.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableResponseEntity<T> extends ResponseEntity<T> {
    private Long totalRecords;
    private Integer pageSize;
    private Integer totalPages;
    private Integer unreadCount;

    public PageableResponseEntity(Integer status, String message, T data, Long totalRecords, Integer pageSize, Integer totalPages, Integer unreadCount) {
        super(status, message, data);
        this.totalRecords = totalRecords;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.unreadCount = unreadCount;
    }

    public PageableResponseEntity(Integer status, String message, T data, Long totalRecords, Integer pageSize, Integer totalPages) {
        super(status, message, data);
        this.totalRecords = totalRecords;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }
}
