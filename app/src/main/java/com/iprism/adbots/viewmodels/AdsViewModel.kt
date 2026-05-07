package com.iprism.adbots.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.adbots.models.ViewAdsApiResponse
import com.iprism.adbots.models.ViewAdsRequest
import com.iprism.adbots.models.updateadreputation.UpdateAdReputationApiResponse
import com.iprism.adbots.models.updateadreputation.UpdateAdReputationRequest
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusApiResponse
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusRequest
import com.iprism.adbots.repository.AdsRepository
import com.iprism.adbots.utils.UiState
import kotlinx.coroutines.launch

class AdsViewModel(private val repository: AdsRepository) : ViewModel() {

    private val _response = MutableLiveData<UiState<ViewAdsApiResponse>>()
    val response: LiveData<UiState<ViewAdsApiResponse>> = _response

    private val _updateDeviceStatusResponse = MutableLiveData<UiState<UpdateDeviceStatusApiResponse>>()
    val updateDeviceStatusResponse: LiveData<UiState<UpdateDeviceStatusApiResponse>> = _updateDeviceStatusResponse

    private val _updateAdReputationResponse = MutableLiveData<UiState<UpdateAdReputationApiResponse>>()
    val updateAdReputationResponse: LiveData<UiState<UpdateAdReputationApiResponse>> = _updateAdReputationResponse


    fun updateDeviceStatus(request: UpdateDeviceStatusRequest) {
        viewModelScope.launch {
            _updateDeviceStatusResponse.value = UiState.Loading
            try {
                val response = repository.updateDevoiceStatus(request)
                if (response.status) {
                    _updateDeviceStatusResponse.value = UiState.Success(response)
                } else {
                    _updateDeviceStatusResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _updateDeviceStatusResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun updateAdReputation(request: UpdateAdReputationRequest) {
        viewModelScope.launch {
            _updateAdReputationResponse.value = UiState.Loading
            try {
                val response = repository.updateAdReputation(request)
                if (response.status) {
                    _updateAdReputationResponse.value = UiState.Success(response)
                } else {
                    _updateAdReputationResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _updateAdReputationResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun fetchAds(req : ViewAdsRequest) {
        viewModelScope.launch {
            _response.value = UiState.Loading
            try {
                val response = repository.fetchAds(req)
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