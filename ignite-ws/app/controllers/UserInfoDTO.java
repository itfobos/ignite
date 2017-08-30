package controllers;

import cache.entities.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@SuppressWarnings("WeakerAccess")
class UserInfoDTO {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public final String ctn;
    public final String name;
    public final String email;
    public final String activationDate;

    public UserInfoDTO(User user) {
        this.name = user.name;
        this.email = user.email;
        this.ctn = user.ctn;

        this.activationDate = user.getActivationDate() != null ? DATE_FORMAT.format(user.getActivationDate()) : "";
    }

}
