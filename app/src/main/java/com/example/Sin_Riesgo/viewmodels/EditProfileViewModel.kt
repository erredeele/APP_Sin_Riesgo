package com.example.Sin_Riesgo.viewmodels

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.Sin_Riesgo.data.repositories.AuthRepository
import com.example.Sin_Riesgo.domain.use_cases.auth.UpdateUserProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)
    private val updateUserProfileUseCase = UpdateUserProfileUseCase(authRepository)

    // Estados para la UI de edición
    var name by mutableStateOf("")
        private set
    var profileImageUri by mutableStateOf<Uri?>(null) // URI de la nueva imagen seleccionada
        private set
    var isLoading by mutableStateOf(false)
        private set

    // Eventos para la UI
    private val _editProfileEvent = MutableSharedFlow<EditProfileEvent>()
    val editProfileEvent = _editProfileEvent.asSharedFlow()

    init {
        // Cargar los datos actuales del usuario al iniciar
        viewModelScope.launch {
            val currentUserInfo = authRepository.authInfo.first() // Espera el primer valor no nulo
            currentUserInfo?.let {
                name = it.name ?: ""
                // Aquí, en una app real, si tu AuthInfo tuviera la URL de la imagen de perfil,
                // la cargarías y la convertirías a un URI si es necesario para mostrarla.
                // profileImageUri = Uri.parse(it.profileImageUrl) // si ya tuvieras una URL
            }
        }
    }

    fun onNameChange(newName: String) {
        name = newName
    }

    fun onImageSelected(uri: Uri?) {
        profileImageUri = uri
    }

    fun saveProfile() {
        if (name.isBlank()) {
            viewModelScope.launch { _editProfileEvent.emit(EditProfileEvent.Error("El nombre no puede estar vacío.")) }
            return
        }

        isLoading = true
        viewModelScope.launch {
            val result = updateUserProfileUseCase(name, profileImageUri)
            isLoading = false
            when {
                result.isSuccess -> {
                    _editProfileEvent.emit(EditProfileEvent.Success("Perfil actualizado con éxito."))
                }
                result.isFailure -> {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Error al actualizar el perfil."
                    _editProfileEvent.emit(EditProfileEvent.Error(errorMessage))
                }
            }
        }
    }

    sealed class EditProfileEvent {
        data class Success(val message: String) : EditProfileEvent()
        data class Error(val message: String) : EditProfileEvent()
    }
}