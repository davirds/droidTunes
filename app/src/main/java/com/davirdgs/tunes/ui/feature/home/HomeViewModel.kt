package com.davirdgs.tunes.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davirdgs.tunes.domain.repositories.TunesRepository
import com.davirdgs.tunes.domain.models.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val tunesRepository: TunesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState>
        get() = _uiState

    private var currentPage = 0

    init {
        viewModelScope.launch {
            _uiState.map { it.query }
                .debounce(200L)
                .distinctUntilChanged()
                .collectLatest(::onSearch)
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onSearch(query: String = uiState.value.query) {
        _uiState.update { it.copy(showLoading = true) }
        viewModelScope.launch {
            tunesRepository.searchSongs(query, offset = 0, limit = PAGE_SIZE)
                .collectLatest { result ->
                    _uiState.update {
                        result.fold(
                            onSuccess = { songs -> it.copy(songs = songs, showLoading = false) },
                            onFailure = { e -> it.copy(showLoading = false, showError = true) }
                        )
                    }
                }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            val query = uiState.value.query
            val offset = PAGE_SIZE * currentPage
            tunesRepository.searchSongs(query, offset, PAGE_SIZE)
                .collectLatest { result ->
                    result.onSuccess { songs ->
                        if (songs.isNotEmpty()) {
                            _uiState.update {
                                val newState = it.copy(songs = it.songs + songs)
                                currentPage++
                                newState
                            }
                        }
                    }
                }
        }
    }

    companion object {
        const val PAGE_SIZE = 15
    }
}

internal data class HomeUiState(
    val query: String = "",
    val songs: List<Song> = emptyList(),
    val showError: Boolean = false,
    val showLoading: Boolean = false,
)
