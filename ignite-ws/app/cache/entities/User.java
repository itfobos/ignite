package cache.entities;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

//TODO:Remove an annotations
public class User implements Serializable {
    @QuerySqlField
    public final String name;

    @QuerySqlField
    public final String email;

    /**
     * Phone number
     */
    @QuerySqlField(index = true)
    public final String ctn;

    //TODO: Turn on
//    @QuerySqlField
//    public Calendar activationDate;

    @QuerySqlField(index = true)
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
