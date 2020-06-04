package com.nakano.stampcardmvvm.viewModel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.nakano.stampcardmvvm.model.model.User

class StampCardViewModel : BaseObservable() {

    val stampCard: MutableLiveData<User> = MutableLiveData()

    @Bindable
    var numberOfVisits: String = "0"

    @Bindable
    var rank: String = "会員ランク：メンバー"

    // TODO: 来店回数に応じて、スタンプを押下するような処理が必要
//    @get:Bindable
//    val messageLength: String
//        get() = "${messageEditText.length}"

//    fun fetchText() {
//        stampCardLiveData.value = StampCardRepository().fetchText()
//    }

    // TODO: タップ時にfragmentに通知して画面遷移したい、現状はfragmentにonClickListenerを設置して対応
    fun onButtonClicked(){
        // do anything
    }
}
