package com.rahul.githubusersearchapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rahul.githubusersearchapp.R
import com.rahul.githubusersearchapp.data.model.User

@Composable
fun ProfileAdditionalInfo(
    user: User,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (!user.company.isNullOrBlank()) {
                InfoRow(
                    icon = painterResource(R.drawable.baseline_business_24),
                    text = user.company
                )
            }
            if (!user.location.isNullOrBlank()) {
                InfoRow(
                    icon = painterResource(R.drawable.baseline_location_on_24),
                    text = user.location
                )
            }
            if (!user.email.isNullOrBlank()) {
                InfoRow(
                    icon = painterResource(R.drawable.baseline_email_24),
                    text = user.email
                )
            }
            if (!user.blog.isNullOrBlank()) {
                InfoRow(
                    icon = painterResource(R.drawable.baseline_link_24),
                    text = user.blog
                )
            }
            InfoRow(
                icon = painterResource(R.drawable.baseline_calendar_month_24),
                text = "Joined ${formatDate(user.created_at)}"
            )
        }
    }
}