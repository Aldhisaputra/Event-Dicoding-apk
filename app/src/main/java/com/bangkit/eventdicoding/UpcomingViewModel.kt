package com.bangkit.eventdicoding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class UpcomingViewModel : ViewModel() {

    private val _eventList = MutableLiveData<List<ListEventsItem>>()
    val eventList: LiveData<List<ListEventsItem>> get() = _eventList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        getEvents()
    }

    private fun getEvents() {
        _isLoading.value = true
        ApiConfig.getApiService().getEvents(1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _eventList.value = response.body()?.listEvents
                } else {
                    _errorMessage.value = "Gagal mengambil data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = when (t) {
                    is SocketTimeoutException -> "Request timeout. Please try again."
                    is IOException -> "Unable to connect to the server. Please check your internet connection."
                    else -> "An error occurred: ${t.message}"
                }
            }
        })
    }
}
