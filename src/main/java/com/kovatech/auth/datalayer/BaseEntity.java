package com.kovatech.auth.datalayer;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    private String createdOn;
    private String modifiedOn;
    private int createdBy;
    private int modifiedBy;
}
