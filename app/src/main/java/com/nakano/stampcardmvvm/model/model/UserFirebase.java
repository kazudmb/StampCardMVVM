package com.nakano.stampcardmvvm.model.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class UserFirebase implements Serializable {
    public String uid;
    public String name;
    @SuppressWarnings("WeakerAccess")
    public String email;
    @Exclude
    public boolean isAuthenticated;
    @Exclude
    public boolean isNew;
    @Exclude
    public boolean isCreated;

    public UserFirebase() {
    }

    public UserFirebase(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }

    @Override
    public String toString() {
        return "UserFirebase{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                ", isNew=" + isNew +
                ", isCreated=" + isCreated +
                '}';
    }
}