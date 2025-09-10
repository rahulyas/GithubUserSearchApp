package com.rahul.githubusersearchapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rahul.githubusersearchapp.data.model.Repository
import com.rahul.githubusersearchapp.data.model.User
import com.rahul.githubusersearchapp.presentation.navigation.Screen
import com.rahul.githubusersearchapp.presentation.state.PopularReposState
import com.rahul.githubusersearchapp.presentation.state.UserState
import com.rahul.githubusersearchapp.presentation.viewmodel.ProfileViewModel
import com.rahul.githubusersearchapp.ui.components.PopularRepositoryCard
import com.rahul.githubusersearchapp.ui.components.ProfileAdditionalInfo
import com.rahul.githubusersearchapp.ui.utility.formatNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    username: String,
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userState by viewModel.userState.collectAsState()
    val repositoriesState by viewModel.repositoriesState.collectAsState()
    val popularReposState by viewModel.popularReposState.collectAsState()

    LaunchedEffect(username) {
        viewModel.loadUserProfile(username)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(username) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = userState) {
            is UserState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UserState.Success -> {
                ProfileContent(
                    user = state.user,
                    popularReposState = popularReposState,
                    onRepositoriesClick = {
                        navController.navigate(Screen.Repositories.createRoute(username))
                    },
                    onRepositoryClick = { repo ->
                        // Navigate to repository details
                        navController.navigate(Screen.RepositoryDetail.createRoute(repo.owner.login, repo.name))
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is UserState.Error -> {
                ErrorMessage(
                    message = state.message,
                    onRetry = { viewModel.loadUserProfile(username) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is UserState.Idle -> {}
        }
    }
}

@Composable
fun ProfileContent(
    user: User,
    popularReposState: PopularReposState,
    onRepositoriesClick: () -> Unit,
    onRepositoryClick: (Repository) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header
        item {
            ProfileHeader(user = user)
        }

        // Stats Section
        item {
            ProfileStats(
                user = user,
                onRepositoriesClick = onRepositoriesClick
            )
        }

        // Popular Repositories Section
        if (popularReposState is PopularReposState.Success && popularReposState.repositories.isNotEmpty()) {
            item {
                Text(
                    text = "Popular repositories",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(popularReposState.repositories.take(5)) { repo ->
                        PopularRepositoryCard(
                            repository = repo,
                            onClick = { onRepositoryClick(repo) }
                        )
                    }
                }
            }
        }

        // Bio Section
        if (!user.bio.isNullOrBlank()) {
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "About",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = user.bio,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Additional Info
        item {
            ProfileAdditionalInfo(user = user)
        }
    }
}

@Composable
fun ProfileHeader(
    user: User,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = user.avatar_url,
                contentDescription = "User avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = user.name ?: user.login,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            if (user.name != null && user.name != user.login) {
                Text(
                    text = "@${user.login}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (!user.bio.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ProfileStats(
    user: User,
    onRepositoriesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                count = user.followers,
                label = "Followers",
                onClick = { /* Navigate to followers */ }
            )
            StatItem(
                count = user.following,
                label = "Following",
                onClick = { /* Navigate to following */ }
            )
            StatItem(
                count = user.public_repos,
                label = "Repositories",
                onClick = onRepositoriesClick
            )
        }
    }
}

@Composable
fun StatItem(
    count: Int,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatNumber(count),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorItem(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Failed to load more repositories",
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}