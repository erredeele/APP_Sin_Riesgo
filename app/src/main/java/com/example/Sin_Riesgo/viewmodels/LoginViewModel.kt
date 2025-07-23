package com.example.Sin_Riesgo.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.Sin_Riesgo.data.repositories.AuthRepository
import com.example.Sin_Riesgo.domain.models.AuthInfo
import com.example.Sin_Riesgo.domain.use_cases.auth.LoginUserUseCase
import com.example.Sin_Riesgo.domain.use_cases.auth.SignInWithGoogleUseCase
import com.example.Sin_Riesgo.domain.use_cases.auth.SignInWithPhoneNumberUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)
    private val loginUserUseCase = LoginUserUseCase(authRepository)
    private val signInWithGoogleUseCase = SignInWithGoogleUseCase(authRepository)
    private val signInWithPhoneNumberUseCase = SignInWithPhoneNumberUseCase(authRepository)

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var phoneNumber by mutableStateOf("")
        private set
    var otpCode by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set
    var showPassword by mutableStateOf(false)
        private set
    var showPhoneOtpDialog by mutableStateOf(false)
        private set

    private val _currentUserInfo = MutableStateFlow<AuthInfo?>(null)
    val currentUserInfo = _currentUserInfo.asStateFlow()

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            authRepository.authInfo.collect { authInfo ->
                _currentUserInfo.value = authInfo
                if (authInfo != null) {
                    _loginEvent.emit(LoginEvent.Success)
                } else {
                    val lastEmail = authRepository.userEmail.firstOrNull()
                    lastEmail?.let { email = it }
                }
            }
        }
    }

    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading

    val isPhoneNumberValid: Boolean
        get() = phoneNumber.length >= 7

    val isOtpCodeValid: Boolean
        get() = otpCode.length == 6

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onPhoneNumberChange(newNumber: String) {
        phoneNumber = newNumber
    }

    fun onOtpCodeChange(newCode: String) {
        otpCode = newCode
    }

    fun toggleShowPassword() {
        showPassword = !showPassword
    }

    fun login() {
        if (!isLoginEnabled) return

        isLoading = true
        viewModelScope.launch {
            val result = loginUserUseCase(email, password)
            isLoading = false
            when {
                result.isSuccess -> {
                    email = ""
                    password = ""
                }
                result.isFailure -> {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"
                    _loginEvent.emit(LoginEvent.Error(errorMessage))
                }
            }
        }
    }

    fun signInWithGoogle(idToken: String, userEmail: String?, userName: String?) {
        isLoading = true
        viewModelScope.launch {
            val result = signInWithGoogleUseCase(idToken, userEmail, userName)
            isLoading = false
            when {
                result.isSuccess -> { /* Handled by authInfo collect */ }
                result.isFailure -> {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Error al iniciar sesión con Google"
                    _loginEvent.emit(LoginEvent.Error(errorMessage))
                }
            }
        }
    }

    fun requestPhoneOtp() {
        if (!isPhoneNumberValid) {
            viewModelScope.launch { _loginEvent.emit(LoginEvent.Error("Por favor, ingrese un número de teléfono válido.")) }
            return
        }
        isLoading = true
        viewModelScope.launch {
            val result = signInWithPhoneNumberUseCase.requestOtp(phoneNumber)
            isLoading = false
            when {
                result.isSuccess -> {
                    showPhoneOtpDialog = true
                    _loginEvent.emit(LoginEvent.Message("Código OTP enviado a $phoneNumber"))
                }
                result.isFailure -> {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Error al solicitar OTP"
                    _loginEvent.emit(LoginEvent.Error(errorMessage))
                }
            }
        }
    }

    fun verifyPhoneOtp() {
        if (!isOtpCodeValid) {
            viewModelScope.launch { _loginEvent.emit(LoginEvent.Error("Por favor, ingrese un código OTP válido.")) }
            return
        }
        isLoading = true
        viewModelScope.launch {
            val result = signInWithPhoneNumberUseCase.verifyOtp(phoneNumber, otpCode)
            isLoading = false
            when {
                result.isSuccess -> {
                    showPhoneOtpDialog = false
                    phoneNumber = ""
                    otpCode = ""
                }
                result.isFailure -> {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Error al verificar OTP"
                    _loginEvent.emit(LoginEvent.Error(errorMessage))
                }
            }
        }
    }

    fun dismissPhoneOtpDialog() {
        showPhoneOtpDialog = false
        otpCode = ""
    }

    fun logout() {
        isLoading = true
        viewModelScope.launch {
            authRepository.clearUserSession()
            isLoading = false
            _loginEvent.emit(LoginEvent.LoggedOut)
        }
    }

    sealed class LoginEvent {
        object Success : LoginEvent()
        data class Error(val message: String) : LoginEvent()
        data class Message(val message: String) : LoginEvent()
        object LoggedOut : LoginEvent()
    }
}