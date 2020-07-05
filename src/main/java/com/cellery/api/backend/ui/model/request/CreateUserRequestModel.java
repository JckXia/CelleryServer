package com.cellery.api.backend.ui.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {


    @NotNull(message = "User name cannot be null!")
    @Size(min = 5, message = "UserName can't be less than 5 characters", max = 255)
    private String userName;

    @NotNull(message = "Password can't be null!")
    private String password;

    @NotNull(message = "Email field can't be null!")
    @Email
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
