package com.example.factorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.math.BigInteger


class MainViewModel : ViewModel() {

    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("My coroutine scope"))

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun calculate(value: String?) {
        _state.value = Progress
        if (value?.isNotBlank() == false) {
            _state.value = Error
            return
        }
        coroutineScope.launch {
            val number = value?.toLong() ?: 0
            withContext(Dispatchers.Default) {
                val result = factorial(number)
                _state.postValue(Result(result))
            }
        }
    }

    private fun factorial(number: Long): String {
        var result = BigInteger.ONE
        for (i in 1..number) {
            result = result.multiply(BigInteger.valueOf(i))
        }
        return result.toString()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}