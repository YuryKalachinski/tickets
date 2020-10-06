package com.kalachinski.tickets.domains;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {

    private Long id;

    private String name;

    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDateTime;
}
