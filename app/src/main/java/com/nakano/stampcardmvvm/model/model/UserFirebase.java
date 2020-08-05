package com.nakano.stampcardmvvm.model.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class UserFirebase implements Serializable {
    public String uid;
    public String name;
    @SuppressWarnings("WeakerAccess")
    public String email;
    public String numberOfVisits;
    @Exclude
    public boolean isAuthenticated;
    @Exclude
    public boolean isNew;
    @Exclude
    public boolean isCreated;

    public UserFirebase() {
    }

    public UserFirebase(String uid, String name, String email, String numberOfVisits) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.numberOfVisits = numberOfVisits;
    }

    @Override
    public String toString() {
        return "UserFirebase{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", numberOfVisits=" + numberOfVisits +
                ", isAuthenticated=" + isAuthenticated +
                ", isNew=" + isNew +
                ", isCreated=" + isCreated +
                '}';
    }
}