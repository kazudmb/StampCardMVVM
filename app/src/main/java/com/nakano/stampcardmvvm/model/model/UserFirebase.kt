package com.nakano.stampcardmvvm.model.model

data class UserFirebase(
    var uid: String? = null,
    var name: String? = null,
    var email: String? = null,
    var numberOfVisits: String? = null,
    var rank: String? = null
)