package cache.entities;

import play.data.validation.Constraints;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    @Constraints.MinLength(value = 2, message = "User name should be loner then 2 chars")
    public final String name;

    @Constraints.Email
    public final String email;

    /**
     * Phone number
     */
    @Constraints.MinLength(value = 11, message = "User phone number should be equal or loner then 11 chars")
    @Constraints.MaxLength(value = 12, message = "User phone number shouldn't be loner then 12 chars")
    public final String ctn;

    public Date activationDate;

    public String cellId;

    public User(String name, String email, String ctn) {
        this.name = name;
        this.email = email;
        this.ctn = ctn;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", ctn='" + ctn + '\'' +
                '}';
    }

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String CTN = "ctn";
    public static final String ACTIVATION_DATE = "activationDate";
    public static final String CELL_ID = "cellId";
}
