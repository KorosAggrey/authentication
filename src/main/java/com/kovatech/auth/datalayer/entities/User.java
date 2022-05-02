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
@Table("users")
public class User extends BaseEntity implements Persistable<Integer> {

    @Id
    private int id;
    @Column("public_id")
    private String publicId;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String idNumber;
    private int isActive;
    private int active;
    private String expiryTime;
    private String activationCode;
    private String forgottenPasswordCode;
    private String forgottenPasswordTime;

    @Transient
    private boolean newUser;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return this.newUser;
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
