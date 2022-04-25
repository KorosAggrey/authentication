package com.kovatech.auth.datalayer.entities;

import com.kovatech.auth.datalayer.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("users")
public class User extends BaseEntity implements Persistable<String> {

    @Id
    private int id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String idNumber;
    private boolean isActive;


    @Transient
    private boolean newUser;

    @Override
    public String getId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean isNew() {
        return this.newUser || id == 0;
    }

    public User setAsNew(){
        this.newUser = true;
        return this;
    }

    public User setAsUpdate(){
        this.newUser = false;
        return this;
    }

}
