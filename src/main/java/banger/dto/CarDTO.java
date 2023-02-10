package banger.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CarDTO {
    @NotNull
    @NotEmpty
    @Size(min=6, max=6)
    @Pattern(regexp="[A-Z]{3}[0-9]{3}")
    private String licensePlate;
    @NotNull
    @NotEmpty
    private String model;
    @NotNull
    @NotEmpty
    private String manufacturer;
    @NotNull
    @Range(min=1900, max=2100)
    private int yearProduced;
    @NotNull
    @Range(min=2, max=200)
    private int space;
    @NotNull
    @NotEmpty
    private String categoryId;
    @NotNull
    @NotEmpty
    private String siteId;


    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getYearProduced() {
        return yearProduced;
    }

    public void setYearProduced(int yearProduced) {
        this.yearProduced = yearProduced;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
