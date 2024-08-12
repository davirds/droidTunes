package com.davirdgs.tunes.ui.feature.home

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import com.davirdgs.tunes.apiServiceMock
import com.davirdgs.tunes.returnsEmpty
import com.davirdgs.tunes.returnsError
import com.davirdgs.tunes.returnsSuccess
import com.davirdgs.tunes.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

//    private lateinit var navHostController: NavHostController

    @Before
    fun before() {
        hiltRule.inject()
//        composeTestRule.setContent {
//            navHostController = rememberNavController()
//            Application { Navigation(navHostController = navHostController) }
//        }
    }

    @Test
    fun test_HomeScreen_query_song_result_empty() {
        val query = "<empty>"
        apiServiceMock.returnsEmpty { search(term = query, offset = 0, limit = 25) }
        composeTestRule.run {
            onNodeWithTag("SearchField").performTextInput(query)
            onNodeWithText("No songs found yet").assertExists()
        }
    }

    @Test
    fun test_HomeScreen_query_song_result_error() {
        val query = "<error>"
        apiServiceMock.returnsError { search(term = query, offset = 0, limit = 25) }
        composeTestRule.run {
            onNodeWithTag("SearchField").performTextInput(query)
            onNodeWithText("It looks like something wrong happened.").assertExists()
            onNodeWithText("try again").assertHasClickAction()
        }
    }

    @Test
    fun test_HomeScreen_query_song_retry_after_error() {
        val query = "<error>"
        composeTestRule.run {
            apiServiceMock.returnsError { search(term = query, offset = 0, limit = 25) }
            onNodeWithTag("SearchField").performTextInput(query)
            onNodeWithText("It looks like something wrong happened.").assertExists()
            onNodeWithText("try again").assertHasClickAction()
            apiServiceMock.returnsSuccess { search(term = query, offset = 0, limit = 25) }
            onNodeWithText("try again").performClick()
            onNodeWithTag("SongsList").performScrollToIndex(4)
            onNodeWithTag("SongsList")
                .onChildren()
                .assertCountEquals(5)
        }
    }

    @Test
    fun test_HomeScreen_query_song_result_success() {
        val query = "<success>"
        composeTestRule.run {
            apiServiceMock.returnsSuccess { search(term = query, offset = 0, limit = 25) }
            onNodeWithTag("SearchField").performTextInput(query)
            onNodeWithTag("SongsList").performScrollToIndex(4)
            onNodeWithTag("SongsList")
                .onChildren()
                .assertCountEquals(5)
        }
    }
}
