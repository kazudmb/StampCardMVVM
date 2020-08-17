package com.nakano.stampcardmvvm.model.model

data class User(
    var uid: String,
    var name: String? = null,
    var email: String? = null,
    var numberOfVisits: Long = 0,
    var rank: String
)