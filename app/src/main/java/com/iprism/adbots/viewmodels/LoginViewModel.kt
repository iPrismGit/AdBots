package com.iprism.adbots.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.adbots.models.ViewAdsApiResponse
import com.iprism.adbots.models.login.LoginApiResponse
import com.iprism.adbots.models.login.LoginRequest
import com.iprism.adbots.repository.AdsRepository
import com.iprism.adbots.repository.AuthRepository
import com.iprism.adbots.utils.UiState
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<LoginApiResponse>>()
    val response: LiveData<UiState<LoginApiResponse>> = _response


    fun login(request : LoginRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.login(request)
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