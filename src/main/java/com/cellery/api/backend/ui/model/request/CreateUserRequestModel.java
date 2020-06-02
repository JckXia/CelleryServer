package com.cellery.api.backend.ui.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {

    @NotNull(message = "First name of user cannot be null")
    @Size(min = 2, message = "First name cant be less than two characters", max = 255)
    private String firstName;

    @NotNull(message = "Last name of user cannot be null")
    @Size(min = 2, message = "Last name cant be less than two characters", max = 255)
    private String lastName;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
