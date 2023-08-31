package com.imams.boardminton.ui.screen.create.player

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imams.boardminton.domain.impl.CreatePlayerUseCase
import com.imams.boardminton.ui.screen.create.FileNamingExt
import com.imams.boardminton.ui.screen.create.PhotoUriManager
import com.imams.boardminton.ui.utils.underScore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlayerVM @Inject constructor(
    private val useCase: CreatePlayerUseCase,
    private val photoUriManager: PhotoUriManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePlayerState())
    val uiState: StateFlow<CreatePlayerState> = _uiState.asStateFlow()

    private val _savePlayers = mutableStateListOf<CreatePlayerState>()
    private val _savePlayersFlow = MutableStateFlow(_savePlayers)
    val savePlayers: StateFlow<List<CreatePlayerState>> = _savePlayersFlow

    init {
        checkSavedPlayers()
    }

    fun execute(event: CreatePlayerEvent) {
        when (event) {
            is CreatePlayerEvent.FirstName -> _uiState.update { it.copy(firstName = event.name) }
            is CreatePlayerEvent.LastName -> _uiState.update { it.copy(lastName = event.name) }
            is CreatePlayerEvent.HandPlay -> _uiState.update { it.copy(handPlay = event.value) }
            is CreatePlayerEvent.Gender -> _uiState.update { it.copy(gender = event.value) }
            is CreatePlayerEvent.Height -> _uiState.update { it.copy(height = event.value) }
            is CreatePlayerEvent.Weight -> _uiState.update { it.copy(weight = event.value) }
            is CreatePlayerEvent.DOB -> _uiState.update { it.copy(dob = event.value) }
            is CreatePlayerEvent.GenerateSelfie -> { onSelfieResponse(event.uri) }
            is CreatePlayerEvent.ImportContact -> _uiState.update { it.copy(phoneNumber = event.value) }
            is CreatePlayerEvent.Clear -> _uiState.update { CreatePlayerState() }
            else -> {}
        }
    }

    private var isInit = false
    fun setupWith(id: Int) {
        // todo should use savedInstance state
        if (isInit) return
        viewModelScope.launch {
            useCase.getPlayer(id).collectLatest { player ->
                isInit = true
                _uiState.update { player }
                onSelfieResponse(Uri.parse(player.photoProfileUri))
            }
        }
    }

    fun savePlayer(callback: (() -> Unit)? = null, data: CreatePlayerState = uiState.value) {
        saveSelfie { uri, fileName ->
            viewModelScope.launch {
                useCase.createPlayer(data.copy(photoProfileUri = uri))
                delay(500)
                callback?.invoke()
                _uiState.update { CreatePlayerState() }
                _tempSelfieUri.update { it.copy(uri = Uri.parse(uri), fileName = fileName) }
            }
        }
    }

    fun updatePlayer(callback: (() -> Unit)? = null, data: CreatePlayerState = uiState.value) {
        if (_tempSelfieUri.value.uri.toString() == data.photoProfileUri) {
            viewModelScope.launch { useCase.updatePlayer(data) }
        } else {
            saveSelfie(data.id,
                callback = { uri, fileName ->
                    viewModelScope.launch {
                        useCase.updatePlayer(data.copy(photoProfileUri = uri))
                        delay(500)
                        callback?.invoke()
                        _uiState.update { CreatePlayerState() }
                        _tempSelfieUri.update { it.copy(uri = Uri.parse(uri), fileName = fileName) }
                    }
                }
            )
        }
    }

    private fun checkSavedPlayers() {
        viewModelScope.launch {
            useCase.getAllPlayers().collectLatest {
                _savePlayers.clear()
                _savePlayers.addAll(it)
            }
        }
    }

    private val _tempSelfieUri = MutableStateFlow(SelfieFieldState())
    val tempSelfieUri = _tempSelfieUri.asStateFlow()
    fun getNewSelfieUri() = photoUriManager.buildNewUri()

    private fun onSelfieResponse(uri: Uri) {
        log("onSelfieResponse -> ${uri.path} Uri $uri")
        _tempSelfieUri.update {
            it.copy(uri = uri, fileName = photoUriManager.checkFileName(uri))
        }
    }

    private fun saveSelfie(id: Int? = null, callback: ((String, String) -> Unit)? = null) {
        viewModelScope.launch {
            _tempSelfieUri.value.uri?.let {
                log("saveSelfie -> id: $id path: ${it.path} Uri: $it")
                photoUriManager.savePlayerImage(
                    uri = it,
                    name = FileNamingExt.generatePhotoPlayer(
                        id ?: 0,
                        _uiState.value.fullName.underScore()
                    ),
                    callback = { path, fileName ->
                        callback?.invoke(path, fileName)
                        log("saveSelfie callback -> path: $path fileName: $fileName")
                    }
                )
            }
        }
    }
    private fun log(m: String) = println("CreatePlayerVM: $m")

}

data class SelfieFieldState(
    val uri: Uri? = null,
    val fileName: String? = null
)

data class CreatePlayerState(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val handPlay: String = "",
    val gender: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val dob: Long = 0,
    val phoneNumber: String = "",
    var photoProfileUri: String = "",
) {
    val fullName = "$firstName $lastName"
}

sealed class CreatePlayerEvent {
    data class FirstName(val name: String) : CreatePlayerEvent()
    data class LastName(val name: String) : CreatePlayerEvent()
    data class Height(val value: Int) : CreatePlayerEvent()
    data class Weight(val value: Int) : CreatePlayerEvent()
    data class DOB(val value: Long) : CreatePlayerEvent()
    data class HandPlay(val value: String) : CreatePlayerEvent()
    data class Gender(val value: String) : CreatePlayerEvent()
    data class ImportContact(val value: String): CreatePlayerEvent()
    object Clear : CreatePlayerEvent()
    data class GenerateSelfie(val uri: Uri) : CreatePlayerEvent()
}