package com.example.projectpartner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth
    private val _loginUi = MutableStateFlow<LoginUIController>(LoginUIController.empty)

    val loginUI : StateFlow<LoginUIController> = _loginUi


    sealed class LoginUIController{

        object empty : LoginUIController()
        object loading : LoginUIController()
        data class success(val data : String) : LoginUIController()
        data class error (val msg : String) : LoginUIController()

    }

    fun login(username : String, password: String) = viewModelScope.launch{

        auth = FirebaseAuth.getInstance()
        _loginUi.value = LoginUIController.loading
        delay(2000)

        auth.signInWithEmailAndPassword(username,password).addOnCompleteListener{
            if(it.isSuccessful){
                _loginUi.value = LoginUIController.success("Authenticated")
            }else{
                _loginUi.value = LoginUIController.error("Credential doesn't match")
            }
        }.addOnFailureListener {
            _loginUi.value = LoginUIController.error(it.message.toString())
        }
    }

}