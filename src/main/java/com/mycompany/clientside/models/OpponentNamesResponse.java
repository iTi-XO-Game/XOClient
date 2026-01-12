package com.mycompany.clientside.models;

import java.util.HashMap;
import java.util.Map;

public class UsernamesResponse {

    private Map<Integer, String> usersMap;

    public UsernamesResponse() {
        this.usersMap = new HashMap<>();
    }

    public UsernamesResponse(Map<Integer, String> usersMap) {
        this.usersMap = usersMap;
    }

    public Map<Integer, String> getUsersMap() {
        return usersMap;
    }

    public void setUsersMap(Map<Integer, String> usersMap) {
        this.usersMap = usersMap;
    }
}
