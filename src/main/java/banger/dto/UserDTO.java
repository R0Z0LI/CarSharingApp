package banger.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserDTO {
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    @Email
    private String email; //todo kell majd valami email validacio
    @NotNull
    @NotEmpty
    private String phone;
    @NotNull
    @NotEmpty
    private String licenseNumber;
    @NotNull
    @NotEmpty
    private String password; //todo kell majd valami pw validacio
    private String adminKey;

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {return phone;}

    public String getLicenseNumber() {return licenseNumber;}

    public String getAdminKey() {return adminKey;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdminKey(String adminKey) {
        this.adminKey = adminKey;
    }
}
