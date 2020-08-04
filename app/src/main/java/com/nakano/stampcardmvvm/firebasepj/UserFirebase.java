package com.nakano.stampcardmvvm.firebasepj;

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
    boolean isNew, isCreated;

    public UserFirebase() {}

    UserFirebase(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }
}