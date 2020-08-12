package com.nakano.stampcardmvvm.model.model

import com.google.firebase.firestore.Exclude

data class UserFirebase(
    var uid: String? = null,
    var name: String? = null,
    var email: String? = null,
    var numberOfVisits: String? = null,
    var rank: String? = null,

    @Exclude
    var isAuthenticated: Boolean? = false,

    @Exclude
    var isCreated: Boolean? = false
)