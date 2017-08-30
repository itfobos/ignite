package controllers;

import play.data.validation.Constraints;

// Public access modifier and getters/setters are required for form data binding.
@SuppressWarnings("WeakerAccess")
public class UserProfileDTO {
    @Constraints.MinLength(value = 11, message = "User phone number should be equal or loner then 11 chars")
    @Constraints.MaxLength(value = 12, message = "User phone number shouldn't be loner then 12 chars")
    private String ctn;

    @Constraints.MinLength(value = 2, message = "User name should be loner then 2 chars")
    private String name;

    @Constraints.Email(message = "Invalid email template")
    private String email;

    @Override
    public String toString() {
        return "UserProfileDTO{" +
                "ctn='" + ctn + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getCtn() {
        return ctn;
    }

    public void setCtn(String ctn) {
        this.ctn = ctn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
