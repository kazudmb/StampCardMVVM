package com.nakano.stampcardmvvm.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nakano.stampcardmvvm.model.repository.StampCardRepository

@Suppress("UNCHECKED_CAST")
class StampCardViewModelFactory(
    private val repository: StampCardRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StampCardViewModel(repository) as T
    }
}