package com.iprism.adbots.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.adbots.models.ViewAdsApiResponse
import com.iprism.adbots.repository.AdsRepository
import com.iprism.adbots.utils.UiState
import kotlinx.coroutines.launch

class WalletViewModel(private val repository: AdsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<ViewAdsApiResponse>>()
    val response: LiveData<UiState<ViewAdsApiResponse>> = _response


    fun fetchAds() {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchAds()
                if (response.status) {
                    _response.value = UiState.Success(response)
                } else {
                    _response.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _response.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}