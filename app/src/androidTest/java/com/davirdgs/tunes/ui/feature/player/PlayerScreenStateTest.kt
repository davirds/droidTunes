package com.davirdgs.tunes.ui.feature.player

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.davirdgs.tunes.ui.songMock
import com.davirdgs.tunes.ui.songsMock
import com.davirdgs.tunes.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerScreenStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_PlayerScreen_playing_state() {
        val song = songMock()
        val uiState = PlayerUiState(song, isPlaying = true)
        composeTestRule.run {
            playerScreen(uiState)
            onNodeWithContentDescription("Options").assertExists()
            onNodeWithContentDescription("Options").assertHasClickAction()
            onNodeWithContentDescription("Navigate back").assertExists()
            onNodeWithContentDescription("Navigate back").assertHasClickAction()
            onAllNodesWithText(song.name)[0].isDisplayed()
            onAllNodesWithText(song.artist.name)[0].isDisplayed()
            onNodeWithContentDescription("Pause").assertExists()
            onNodeWithContentDescription("Pause").assertHasClickAction()
            onNodeWithContentDescription("Backward").assertExists()
            onNodeWithContentDescription("Backward").assertHasClickAction()
            onNodeWithContentDescription("Forward").assertExists()
            onNodeWithContentDescription("Forward").assertHasClickAction()
        }
    }

    @Test
    fun test_PlayerScreen_paused_state() {
        val song = songMock()
        val uiState = PlayerUiState(song, isPlaying = false)
        composeTestRule.run {
            playerScreen(uiState)
            onNodeWithContentDescription("Options").assertExists()
            onNodeWithContentDescription("Options").assertHasClickAction()
            onNodeWithContentDescription("Navigate back").assertExists()
            onNodeWithContentDescription("Navigate back").assertHasClickAction()
            onAllNodesWithText(song.name)[0].isDisplayed()
            onAllNodesWithText(song.artist.name)[0].isDisplayed()
            onNodeWithContentDescription("Play").assertExists()
            onNodeWithContentDescription("Play").assertHasClickAction()
            onNodeWithContentDescription("Backward").assertExists()
            onNodeWithContentDescription("Backward").assertHasClickAction()
            onNodeWithContentDescription("Forward").assertExists()
            onNodeWithContentDescription("Forward").assertHasClickAction()
        }
    }

    @Test
    fun test_PlayerScreen_album_error_state() {
        val uiState = PlayerUiState(songMock(), showAlbumError = true)
        composeTestRule.run {
            playerScreen(uiState)
            onNodeWithContentDescription("Options").assertExists()
            onNodeWithContentDescription("Options").performClick()
            onNodeWithText("It looks like something wrong happened.").assertExists()
            onNodeWithText("try again").assertHasClickAction()
        }
    }

    @Test
    fun test_PlayerScreen_album_loading_state() {
        val uiState = PlayerUiState(songMock(), showAlbumLoading = true)
        composeTestRule.run {
            playerScreen(uiState)
            onNodeWithContentDescription("Options").assertExists()
            onNodeWithContentDescription("Options").performClick()
            onNodeWithTag("Loading").assertExists()
        }
    }

    @Test
    fun test_PlayerScreen_album_success_state() {
        val songs = songsMock(12)
        val uiState = PlayerUiState(songMock(), album = songs)
        composeTestRule.run {
            playerScreen(uiState)
            onNodeWithContentDescription("Options").assertExists()
            onNodeWithContentDescription("Options").performClick()
            onNodeWithTag("SongsList").assertExists()
            onNodeWithTag("SongsList")
                .performScrollToIndex(songs.size - 1)
                .onChildren()
                .assertCountEquals(songs.size)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun ComposeContentTestRule.playerScreen(
    uiState: PlayerUiState
) {
    this.setContent {
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState(
                initialValue = SheetValue.Hidden,
                skipHiddenState = false
            )
        )
        AppTheme {
            PlayerScreen(
                scaffoldState = scaffoldState,
                uiState = uiState,
                onOpenAlbum = { scope.launch { scaffoldState.bottomSheetState.expand() } },
                onRetry = { },
                onNavigateBack = { },
                onPLaySong = { },
                onSeekChange = { },
                onPlay = { },
                onPause = { },
                onForward = { },
                onBackward = { }
            )
        }
    }
}
