package controllers;

import cache.entities.User;

class UserDTO {
    public final String name;

    public final String email;

    public final String ctn;

    public final String activationDate;

    public UserDTO(User user) {
        this.name = user.name;
        this.email = user.email;
        this.ctn = user.ctn;
        //TODO:
        this.activationDate = "";
    }

}
