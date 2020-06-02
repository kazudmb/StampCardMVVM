package com.nakano.stampcardmvvm.viewModel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.nakano.stampcardmvvm.model.model.User
import com.nakano.stampcardmvvm.model.repository.StampCardRepository

class StampCardViewModel : BaseObservable(){

//    val stampCardLiveData: MutableLiveData<User> = MutableLiveData()

    @Bindable
    var numberOfVisits: String = "0"
        set(value) {
            field = value
        }

    @Bindable
    var rank: String = "会員ランク：メンバー"
        set(value) {
            field = value
        }

    // TODO: 来店回数に応じて、スタンプを押下するような処理が必要
//    @get:Bindable
//    val messageLength: String
//        get() = "${messageEditText.length}"

//    fun fetchText() {
//        stampCardLiveData.value = StampCardRepository().fetchText()
//    }
}
