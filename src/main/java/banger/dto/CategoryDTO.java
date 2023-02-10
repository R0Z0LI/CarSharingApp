package banger.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class CategoryDTO {
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private double pricePerHour;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}
