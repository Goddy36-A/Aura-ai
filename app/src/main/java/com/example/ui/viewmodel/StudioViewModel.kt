package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.models.*
import com.example.data.remote.CoFounderAiService
import com.example.data.repository.StudioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class UiState {
    object Idle : UiState()
    data class Generating(val step: String) : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudioRepository
    val allProjects: StateFlow<List<DesignProject>>
    val allMessages: StateFlow<List<CoFounderMessage>>

    private val _selectedCategory = MutableStateFlow<String?>("ALL") // "ALL" or category.name
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _activePersona = MutableStateFlow(PartnerPersona.CCO)
    val activePersona = _activePersona.asStateFlow()

    private val _companyContext = MutableStateFlow("Vanguard AI")
    val companyContext = _companyContext.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _selectedProject = MutableStateFlow<DesignProject?>(null)
    val selectedProject = _selectedProject.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = StudioRepository(db.designDao(), db.chatDao(), CoFounderAiService())
        allProjects = repository.allProjects.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        allMessages = repository.allMessages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        viewModelScope.launch {
            repository.ensureInitialDemoProjects()
        }
    }

    fun selectCategory(cat: String?) {
        _selectedCategory.value = cat
    }

    fun setPersona(persona: PartnerPersona) {
        _activePersona.value = persona
    }

    fun updateCompanyContext(newContext: String) {
        _companyContext.value = newContext
    }

    fun selectProject(project: DesignProject?) {
        _selectedProject.value = project
    }

    fun generateNewProject(
        category: DesignCategory,
        companyName: String,
        industry: String,
        vibe: String,
        notes: String,
        onSuccess: (Int) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Generating("AI Co-Founder drafting ${category.displayName}...")
                _companyContext.value = companyName
                val newId = repository.createProjectWithAi(
                    title = "$companyName - ${category.displayName}",
                    category = category,
                    companyName = companyName,
                    industry = industry,
                    vibe = vibe,
                    extraNotes = notes
                )
                _uiState.value = UiState.Success("Created ${category.displayName} successfully!")
                onSuccess(newId)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Generation failed: ${e.message}")
            }
        }
    }

    fun toggleFavorite(project: DesignProject) {
        viewModelScope.launch {
            repository.toggleFavorite(project.id, project.isFavorite)
        }
    }

    fun deleteProject(project: DesignProject) {
        viewModelScope.launch {
            if (_selectedProject.value?.id == project.id) {
                _selectedProject.value = null
            }
            repository.deleteProject(project.id)
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.sendChatMessage(
                userText = text.trim(),
                persona = _activePersona.value,
                companyContext = _companyContext.value
            )
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    fun clearUiState() {
        _uiState.value = UiState.Idle
    }
}
