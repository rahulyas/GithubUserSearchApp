package com.rahul.githubusersearchapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rahul.githubusersearchapp.R
import com.rahul.githubusersearchapp.data.model.Repository
import com.rahul.githubusersearchapp.presentation.state.RepositoryDetailState
import com.rahul.githubusersearchapp.presentation.viewmodel.RepositoryDetailViewModel
import com.rahul.githubusersearchapp.ui.components.InfoRow
import com.rahul.githubusersearchapp.ui.components.formatDate
import com.rahul.githubusersearchapp.ui.utility.formatNumber
import com.rahul.githubusersearchapp.ui.utility.formatRelativeTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailScreen(
    owner: String,
    repo: String,
    navController: NavHostController,
    viewModel: RepositoryDetailViewModel = hiltViewModel()
) {
    val repositoryState by viewModel.repositoryState.collectAsState()

    LaunchedEffect(owner, repo) {
        viewModel.loadRepository(owner, repo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(repo) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = repositoryState) {
            is RepositoryDetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is RepositoryDetailState.Success -> {
                RepositoryDetailContent(
                    repository = state.repository,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is RepositoryDetailState.Error -> {
                ErrorMessage(
                    message = state.message,
                    onRetry = { viewModel.loadRepository(owner, repo) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is RepositoryDetailState.Idle -> {}
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RepositoryDetailContent(
    repository: Repository,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Repository name and owner
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = repository.owner.avatar_url,
                            contentDescription = "Owner avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = repository.owner.login,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = repository.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (!repository.description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = repository.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Repository stats
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        StatColumn(
                            count = repository.stargazers_count,
                            label = "Stars",
                            icon = painterResource(R.drawable.baseline_star_24)
                        )
                        StatColumn(
                            count = repository.watchers_count,
                            label = "Watchers",
                            icon = painterResource(R.drawable.baseline_remove_red_eye_24)
                        )
                        StatColumn(
                            count = repository.forks_count,
                            label = "Forks",
                            icon = painterResource(R.drawable.call_split)
                        )
                    }
                }
            }
        }

        // Repository info
        item {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Repository Info",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (repository.language != null) {
                        InfoRow(
                            icon = painterResource(R.drawable.baseline_code_24),
                            text = repository.language
                        )
                    }
                    InfoRow(
                        icon = painterResource(R.drawable.baseline_storage_24),
                        text = "${repository.size} KB"
                    )
                    InfoRow(
                        icon = painterResource(R.drawable.baseline_calendar_month_24),
                        text = "Created ${formatDate(repository.created_at)}"
                    )
                    InfoRow(
                        icon = painterResource(R.drawable.baseline_update_24),
                        text = "Updated ${formatRelativeTime(repository.updated_at)}"
                    )
                    InfoRow(
                        icon = painterResource(R.drawable.baseline_cloud_upload_24),
                        text = "Last push ${formatRelativeTime(repository.pushed_at)}"
                    )
                }
            }
        }
    }
}

@Composable
fun StatColumn(
    count: Int,
    label: String,
    icon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = formatNumber(count),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}