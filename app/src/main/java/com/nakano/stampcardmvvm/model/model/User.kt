package com.nakano.stampcardmvvm.model.model

// TODO: data classの初期値はどのような内容が推奨か調査して実装
data class User(
    var uid: String? = null,
    var name: String? = null,
    var email: String? = null,
    var numberOfVisits: String? = null, // TODO: High Intで保存したい
    var rank: String? = null
)