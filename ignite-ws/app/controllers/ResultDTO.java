package controllers;

import java.util.List;

@SuppressWarnings("WeakerAccess")
class ResultDTO {
    public int total;
    public List<UserDTO> results;

    public ResultDTO(List<UserDTO> results) {
        this.total = results.size();
        this.results = results;
    }
}
