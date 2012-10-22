package com.aimprosoft.glossary.common.model.impl;

import com.aimprosoft.glossary.common.model.BusinessModel;
import com.aimprosoft.glossary.common.model.UserRole;
import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "glossary_user")
public class User extends BusinessModel {

    //validation
    @NotNull(message = "sample.error.not.null")
    @NotEmpty(message = "sample.error.not.empty")
    @Email(message = "sample.error.wrong.email")
    //hibernate
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    //validation
    @NotNull
    @NotEmpty
    //hibernate
    @Column(name = "password", nullable = false, unique = true)
    private String password;

    //validation
    @NotNull(message = "sample.error.not.null")
    @NotEmpty(message = "sample.error.not.empty")
    //hibernate
    @Column(name = "name", nullable = false)
    private String name;

    //validation
    @NotNull(message = "sample.error.not.null")
    //hibernate
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}