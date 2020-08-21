package com.nakano.stampcardmvvm.util

object Constant {
    // for firebase
    const val COLLECTION_PATH = "users"
    const val FIELD_NAME_UID = "uid"
    const val FIELD_NAME_NAME = "name"
    const val FIELD_NAME_EMAIL = "email"
    const val FIELD_NAME_NUMBER_OF_VISITS = "numberOfVisits"

    // for option menu
    const val ALL = "all"
    const val EMAIL_CHANGE_AND_LOGOUT = "email_and_change_and_logout"
    const val ONLY_LOGOUT = "only_logout"

    // for other
    const val DEFAULT_NUMBER_OF_VISITS = 0.toLong()
}