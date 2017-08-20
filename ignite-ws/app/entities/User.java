package entities;

import java.util.Date;

public class User {
    public final String name;
    public final String email;

    public final Date activationDate; //TODO: Use calendar for timezone

    /**
     * Phone number
     */
    public final String ctn;

    public String cellId;

    public User(String name, String email, Date activationDate, String ctn) {
        this.name = name;
        this.email = email;
        this.activationDate = activationDate;
        this.ctn = ctn;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", ctn='" + ctn + '\'' +
                '}';
    }
}
