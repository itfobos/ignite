package cache.entities;

import play.data.validation.Constraints;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    public final String name;

    public final String email;

    /**
     * Phone number
     */
    public final String ctn;

    private Date activationDate = new Date();

    public String cellId;

    public User(String name, String email, String ctn) {
        this.name = name;
        this.email = email;
        this.ctn = ctn;
    }

    public User(String name, String email, String ctn, Date activationDate) {
        this(name, email, ctn);
        this.activationDate = activationDate;
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

    public Date getActivationDate() {
        return activationDate;
    }
}
