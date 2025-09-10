package com.rahul.githubusersearchapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rahul.githubusersearchapp.R
import com.rahul.githubusersearchapp.data.model.User
import com.rahul.githubusersearchapp.presentation.navigation.Screen
import com.rahul.githubusersearchapp.presentation.state.SimilarUsersState
import com.rahul.githubusersearchapp.presentation.state.UserState
import com.rahul.githubusersearchapp.presentation.viewmodel.MainViewModel
import com.rahul.githubusersearchapp.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchState by viewModel.searchState.collectAsState()
    val userState by viewModel.userState.collectAsState()
    val similarUsersState by viewModel.similarUsersState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
  /*  val pullRefreshState = rememberPullToRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshData() }
    )*/


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "GitHub User Search",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    DarkModeToggle(mainViewModel)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SearchBar(
                viewModel = viewModel,
                searchText = searchState.searchText,
                onSearchTextChange = viewModel::onSearchTextChange,
                onSearch = viewModel::searchUser,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = userState) {
                is UserState.Idle -> {
                    // Show similar users when typing
                    if (searchState.searchText.isNotBlank() && similarUsersState is SimilarUsersState.Success) {
                        SimilarUsersList(
                            users = (similarUsersState as SimilarUsersState.Success).users,
                            onUserClick = { username ->
                                viewModel.onSearchTextChange(username)
                                viewModel.searchUser()
                            }
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Enter a GitHub username to search",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                is UserState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UserState.Success -> {
                    LazyColumn {
                        item {
                            UserProfilePreview(
                                user = state.user,
                                onViewProfile = {
                                    navController.navigate(Screen.Profile.createRoute(state.user.login))
                                }
                            )
                        }

                        // Show similar users below the main result
                        if (similarUsersState is SimilarUsersState.Success && (similarUsersState as SimilarUsersState.Success).users.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "Similar Users",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }
                            items((similarUsersState as SimilarUsersState.Success).users) { user ->
                                SimilarUserItem(
                                    user = user,
                                    onClick = {
                                        navController.navigate(Screen.Profile.createRoute(user.login))
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
                is UserState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        onRetry = { viewModel.searchUser() }
                    )
                }
            }
        }
    }
}
@Composable
fun DarkModeToggle(mainViewModel: MainViewModel) {
    val isDarkMode by mainViewModel.isDarkMode.collectAsState()


    IconButton(
        onClick = { mainViewModel.toggleDarkMode() }
    ) {
        Icon(
            painter = if (isDarkMode) painterResource(id = R.drawable.baseline_light_mode_24) else painterResource(id = R.drawable.baseline_dark_mode_24),
            contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode"
        )
    }
}

@Composable
fun SimilarUsersList(
    users: List<User>,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Similar Users",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(users) { user ->
            SimilarUserItem(
                user = user,
                onClick = { onUserClick(user.login) }
            )
        }
    }
}

@Composable
fun SimilarUserItem(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatar_url,
                contentDescription = "User avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.login,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (user.name != null && user.name != user.login) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            /*Text(
                text = "${user.} repos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )*/
        }
    }
}
@Composable
fun SearchBar(
    viewModel: SearchViewModel,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 56.dp),
            placeholder = {
                Text("Enter GitHub username")
            },
            trailingIcon = {
                if (searchText.isNotBlank()) {
                    IconButton(
                        onClick = {
                            onSearchTextChange("")
                            viewModel.resetUserState()
                        }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { onSearch() }
            ),
            shape = MaterialTheme.shapes.extraLarge
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onSearch,
            enabled = searchText.isNotBlank(),
            modifier = Modifier.height(56.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search")
            Spacer(modifier = Modifier.width(4.dp))
            Text("Search")
        }
    }
}

@Composable
fun UserProfilePreview(
    user: User,
    onViewProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = user.avatar_url,
                contentDescription = "User avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = user.name ?: user.login,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "${user.followers} followers",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${user.following} following",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "${user.public_repos} repos",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onViewProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Full Profile")
            }
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Error",
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Try Again")
        }
    }
}
