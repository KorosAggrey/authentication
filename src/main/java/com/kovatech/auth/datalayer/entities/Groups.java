package com.kovatech.auth.datalayer.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("groups")
public class Groups implements Persistable<Integer> {

    @Id
    private int id;
    private String name;

    @Transient
    private boolean newGroups;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
