package com.elevate.app.takeaway.model;

import javax.validation.constraints.NotEmpty;

public class CredentialModel {
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
