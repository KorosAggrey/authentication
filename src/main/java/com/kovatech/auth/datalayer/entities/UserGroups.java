package com.kovatech.auth.datalayer.entities;

import com.kovatech.auth.datalayer.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("users_groups")
public class UserGroups extends BaseEntity implements Persistable<Integer> {

    @Id
    private int id;
    private int userId;
    private int groupId;

    @Transient
    private boolean newUserGroups;

    @Override
    public Integer getId() {
        return 0;
    }

    @Override
    public boolean isNew() {
        return this.newUserGroups;
    }

    public UserGroups setAsNew(){
        this.newUserGroups = true;
        return this;
    }
}
