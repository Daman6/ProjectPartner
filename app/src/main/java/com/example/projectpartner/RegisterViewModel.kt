package com.example.projectpartner

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterController>(RegisterController.Empty)

    val state : StateFlow<RegisterController> = _registerState

    sealed class RegisterController{

        object Loading: RegisterController()
        object Empty: RegisterController()
        data class Error(val message: String): RegisterController()
        data class Success(val data: String): RegisterController()
    }

    fun register(firstName: String, email: String ,pass : String){

    }
}