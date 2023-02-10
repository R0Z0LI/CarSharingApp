package banger.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SiteDTO {
    @NotNull
    @NotEmpty
    private String address;
    @NotNull
    @NotEmpty
    private String phone;
    @NotNull
    @NotEmpty
    @Email
    private String email;
    private List<String> availableCars;

    public String getAddress() {return address;}

    public String getPhone() {return phone;}

    public String getEmail() {return email;}

    public List<String> getAvailableCars() {return availableCars;}

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvailableCars(List<String> availableCars) {
        this.availableCars = availableCars;
    }
}
