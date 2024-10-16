package com.bangkit.eventdicoding

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class FinishedViewModel : ViewModel() {
    private val _eventList = MutableLiveData<List<ListEventsItem>>()
    val eventlist: LiveData<List<ListEventsItem>> = _eventList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private companion object {
        const val TAG = "FinishedViewModel"
    }

    init {
        getEvents()
    }

    private fun getEvents() {
        _isLoading.value = true
        ApiConfig.getApiService().getEvents(0).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _eventList.value = response.body()?.listEvents
                } else {
                    handleError(response.message())
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                handleError(t)
            }
        })
    }

    private fun handleError(error: Any) {
        Log.e(TAG, "Error: $error")
        _errorMessage.value = when (error) {
            is String -> "Failed to retrieve data: $error"
            is Throwable -> when (error) {
                is SocketTimeoutException -> "Request timeout. Please try again."
                is IOException -> "Unable to connect to the server. Check your internet connection."
                else -> "An error occurred: ${error.message}"
            }
            else -> "An error occurred"
        }
    }
}
