package com.davirdgs.tunes.ui.feature.home

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.davirdgs.tunes.ui.songsMock
import com.davirdgs.tunes.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenStateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_HomeScreen_error_state() {
        val uiState = HomeUiState(showError = true)
        composeTestRule.run {
            homeScreen(uiState)
            onNodeWithText("Search for songs").assertExists()
            onNodeWithText("It looks like something wrong happened.").assertExists()
            onNodeWithText("try again").assertHasClickAction()
        }
    }

    @Test
    fun test_HomeScreen_loading_state() {
        val uiState = HomeUiState(showLoading = true)
        composeTestRule.run {
            homeScreen(uiState)
            onNodeWithText("Search for songs").assertExists()
            onNodeWithTag("Loading").assertExists()
        }
    }

    @Test
    fun test_HomeScreen_success_state() {
        val songs = songsMock(12)
        val uiState = HomeUiState(songs = songs)
        composeTestRule.run {
            homeScreen(uiState)
            onNodeWithText("Search for songs").assertExists()
            onNodeWithTag("SongsList").assertExists()
            songs.forEach {
                onNodeWithTag("SongsList")
                    .performScrollToNode(hasText(it.name))
                onNodeWithText(it.name).assertExists()
            }
            onNodeWithTag("SongsList")
                .onChildren()
                .assertCountEquals(songs.size)
        }
    }
}

private fun ComposeContentTestRule.homeScreen(
    uiState: HomeUiState
) {
    this.setContent {
        AppTheme {
            HomeScreen(
                uiState = uiState,
                onQueryChange = { },
                onSearch = { },
                onSongClick = { },
                onLoadMore = { },
                onRetry = { }
            )
        }
    }
}
