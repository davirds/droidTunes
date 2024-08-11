package com.davirdgs.tunes.ui.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davirdgs.tunes.data.TunesRepository
import com.davirdgs.tunes.data.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val tunesRepository: TunesRepository
) : ViewModel() {
    private var _uiState by mutableStateOf(HomeUiState())
    val uiState: HomeUiState get() = _uiState

    init {
        viewModelScope.launch {
            snapshotFlow { _uiState.query }
                .debounce(200L)
                .distinctUntilChanged()
                .collectLatest(::onSearch)
        }
    }

    fun onQueryChange(query: String) {
        _uiState = _uiState.copy(query = query)
    }

    fun onSearch(query: String = _uiState.query) {
        _uiState = _uiState.copy(showLoading = true)
        viewModelScope.launch {
            tunesRepository.searchSongs(query)
                .collectLatest { result ->
                    _uiState = result.fold(
                        onSuccess = { songs -> _uiState.copy(songs = songs, showLoading = false) },
                        onFailure = { _uiState.copy(showLoading = false, showError = true) }
                    )
                }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            tunesRepository.searchSongs(query = _uiState.query, offset = _uiState.songs.size)
                .collectLatest { result ->
                    result.onSuccess { songs ->
                        if (songs.isNotEmpty()) {
                            _uiState = _uiState.copy(songs = _uiState.songs + songs)
                        }
                    }
                }
        }
    }
}

internal data class HomeUiState(
    val query: String = "",
    val songs: List<Song> = emptyList(),
    val showError: Boolean = false,
    val showLoading: Boolean = false,
)
