package com.kovatech.auth.datalayer;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private int createdBy;
    private int modifiedBy;
}
