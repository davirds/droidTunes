package com.davirdgs.tunes.ui.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davirdgs.tunes.domain.models.Song
import com.davirdgs.tunes.domain.repositories.TunesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        get() = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            _uiState.map { it.query }
                .debounce(300L)
                .distinctUntilChanged()
                .collectLatest(::onSearch)
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onSearch(query: String = _uiState.value.query) {
        fetchSongs(query = query, isNewSearch = true)
    }

    fun loadMore() {
        if (_uiState.value.showLoading) return
        Log.d("HomeViewModel", "loadMore")
        fetchSongs(query = _uiState.value.query, isNewSearch = false)
    }

    private fun fetchSongs(query: String, isNewSearch: Boolean) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(showLoading = true, showError = false) }

            val offset = if (isNewSearch) 0 else _uiState.value.songs.size

            tunesRepository.searchSongs(
                query = query,
                offset = offset,
                limit = PAGE_SIZE
            ).collectLatest { result ->
                result
                    .onSuccess { newSongs ->
                        _uiState.update { currentState ->
                            val updatedSongs = if (isNewSearch) {
                                newSongs
                            } else {
                                currentState.songs + newSongs
                            }

                            currentState.copy(
                                songs = updatedSongs,
                                showLoading = false,
                            )
                        }
                    }
                    .onFailure { e ->
                        Log.e("HomeViewModel", "fetchSongs error", e)
                        _uiState.update {
                            it.copy(
                                showError = true,
                                showLoading = false,
                            )
                        }
                    }
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 25
    }
}

internal data class HomeUiState(
    val query: String = "Foo Fighter",
    val songs: List<Song> = emptyList(),
    val showError: Boolean = false,
    val showLoading: Boolean = false,
)
