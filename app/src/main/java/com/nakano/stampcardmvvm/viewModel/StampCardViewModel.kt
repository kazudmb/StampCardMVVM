package com.nakano.stampcardmvvm.viewModel

import androidx.lifecycle.ViewModel
import com.nakano.stampcardmvvm.model.repository.StampCardRepository

class StampCardViewModel(
    repository: StampCardRepository
) : ViewModel() {

    val stampCard = repository.getStampCard()

    // TODO: 来店回数に応じて、スタンプを押下するような処理が必要
}
