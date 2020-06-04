package com.nakano.stampcardmvvm.model.model

data class User(
    var uid: String,
    var mailAddress: String,
    var numberOfVisits: Int,
    var rank: String
)