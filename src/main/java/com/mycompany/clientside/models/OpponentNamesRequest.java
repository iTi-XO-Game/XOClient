package com.mycompany.clientside.models;

import java.util.ArrayList;
import java.util.List;

public class UsernamesRequest {

    private List<Integer> usersIds;

    public UsernamesRequest() {
        this.usersIds = new ArrayList<>();
    }

    public UsernamesRequest(List<Integer> usersIds) {
        this.usersIds = usersIds;
    }

    public List<Integer> getUsersIds() {
        return usersIds;
    }

    public void setUsersIds(List<Integer> usersIds) {
        this.usersIds = usersIds;
    }
}
