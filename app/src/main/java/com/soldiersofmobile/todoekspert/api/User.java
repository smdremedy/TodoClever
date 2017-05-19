package com.soldiersofmobile.todoekspert.api;

/**
 * Created by madejs on 18.05.17.
 */

public class User {

    private String objectId;
    private String sessionToken;
    private String username;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "objectId='" + objectId + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
