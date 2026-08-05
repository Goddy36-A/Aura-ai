package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.DesignCategory
import com.example.ui.components.ProfessionalBottomNavBar
import com.example.ui.components.ProfessionalTopBar
import com.example.ui.screens.*
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SlateBackground
import com.example.ui.viewmodel.StudioViewModel
import com.example.ui.viewmodel.UiState

class MainActivity : ComponentActivity() {

    private val viewModel: StudioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: StudioViewModel) {
    var activeTab by remember { mutableStateOf("STUDIO") }
    var showNewDialog by remember { mutableStateOf(false) }
    var dialogCategory by remember { mutableStateOf(DesignCategory.BRAND_KIT) }

    val projects by viewModel.allProjects.collectAsStateWithLifecycle()
    val chatMessages by viewModel.allMessages.collectAsStateWithLifecycle()
    val selectedCategoryFilter by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val activePersona by viewModel.activePersona.collectAsStateWithLifecycle()
    val companyContext by viewModel.companyContext.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedProject by viewModel.selectedProject.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is UiState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.clearUiState()
            }
            is UiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.clearUiState()
            }
            else -> {}
        }
    }

    if (selectedProject != null) {
        ArtifactDetailScreen(
            project = selectedProject!!,
            onBack = { viewModel.selectProject(null) },
            onFavoriteToggle = { viewModel.toggleFavorite(selectedProject!!) },
            onDelete = { viewModel.deleteProject(selectedProject!!) }
        )
    } else {
        Scaffold(
            topBar = {
                ProfessionalTopBar(
                    title = "Aura AI",
                    subtitle = "CO-FOUNDER & CREATIVE STUDIO",
                    onAvatarClick = { activeTab = "ADVISORS" }
                )
            },
            bottomBar = {
                ProfessionalBottomNavBar(
                    selectedTab = activeTab,
                    onTabSelected = { activeTab = it }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = SlateBackground
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (activeTab) {
                    "STUDIO" -> {
                        StudioHomeScreen(
                            projects = projects,
                            companyContext = companyContext,
                            onCategoryClick = { category ->
                                dialogCategory = category
                                showNewDialog = true
                            },
                            onProjectClick = { project ->
                                viewModel.selectProject(project)
                            },
                            onStartNewProjectClick = {
                                dialogCategory = DesignCategory.SLIDE_DECK
                                showNewDialog = true
                            }
                        )
                    }
                    "LIBRARY" -> {
                        LibraryScreen(
                            projects = projects,
                            selectedCategory = selectedCategoryFilter,
                            onSelectCategoryFilter = { viewModel.selectCategory(it) },
                            onProjectClick = { viewModel.selectProject(it) },
                            onFavoriteToggle = { viewModel.toggleFavorite(it) },
                            onDeleteProject = { viewModel.deleteProject(it) }
                        )
                    }
                    "CHAT" -> {
                        ChatScreen(
                            messages = chatMessages,
                            activePersona = activePersona,
                            companyContext = companyContext,
                            onPersonaChange = { viewModel.setPersona(it) },
                            onSendMessage = { viewModel.sendChatMessage(it) },
                            onClearChat = { viewModel.clearChat() },
                            onCreateSuggestedArtifact = { categoryName ->
                                val cat = try {
                                    DesignCategory.valueOf(categoryName)
                                } catch (e: Exception) {
                                    DesignCategory.BRAND_KIT
                                }
                                dialogCategory = cat
                                showNewDialog = true
                            }
                        )
                    }
                    "ADVISORS" -> {
                        AdvisorsScreen(
                            activePersona = activePersona,
                            companyContext = companyContext,
                            onPersonaSelected = { viewModel.setPersona(it) },
                            onCompanyContextChange = { viewModel.updateCompanyContext(it) }
                        )
                    }
                }

                // Loading overlay when generating
                if (uiState is UiState.Generating) {
                    val genState = uiState as UiState.Generating
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Black.copy(alpha = 0.5f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(28.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    CircularProgressIndicator(color = IndigoPrimary)
                                    Text(
                                        text = genState.step,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    )
                                    Text(
                                        text = "Crafting bespoke color psychology & strategic structure...",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNewDialog) {
        NewProjectDialogScreen(
            initialCategory = dialogCategory,
            defaultCompanyName = companyContext,
            onDismiss = { showNewDialog = false },
            onGenerate = { category, name, industry, vibe, notes ->
                showNewDialog = false
                viewModel.generateNewProject(category, name, industry, vibe, notes) { newId ->
                    val created = projects.firstOrNull { it.id == newId }
                    if (created != null) {
                        viewModel.selectProject(created)
                    }
                }
            }
        )
    }
}

