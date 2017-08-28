package cache.entities;

import play.data.validation.Constraints;

import java.io.Serializable;

public class User implements Serializable {
    @Constraints.MinLength(value = 2, message = "User name should be loner then 2 chars")
    public final String name;

    @Constraints.Email
    public final String email;

    /**
     * Phone number
     */
    @Constraints.MinLength(value = 11, message = "User phone number should be loner then 11 chars")
    public final String ctn;

    //TODO: Turn on
//    @QuerySqlField
//    public Calendar activationDate;

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
}
