package com.nakano.stampcardmvvm.model.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity
data class User(
    var uid: String = "test",
    var mailAddress: String,
    var numberOfVisits: String, // TODO: Intだとエラーになるため、調査が必要
    var rank: String
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_USER_ID
}