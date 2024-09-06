package com.instaleap.clean.ui.favorites

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.instaleap.clean.entities.MovieListItem
import com.instaleap.clean.mapper.toMovieListItem
import com.instaleap.clean.ui.base.BaseViewModel
import com.instaleap.clean.util.singleSharedFlow
import com.instaleap.domain.usecase.GetFavoriteMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * @author by Yesid Hernandez 02/09/2024
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteMovies: GetFavoriteMovies,
) : BaseViewModel() {

    val movies: Flow<PagingData<MovieListItem>> = getFavoriteMovies(30)
        .map { pagingData ->
            pagingData.map { movieEntity ->
                movieEntity.toMovieListItem()
            }
        }.cachedIn(viewModelScope)

    private val _uiState: MutableStateFlow<FavoriteUiState> = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState: MutableSharedFlow<FavoritesNavigationState> = singleSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    fun onMovieClicked(movieId: Int) =
        _navigationState.tryEmit(FavoritesNavigationState.MovieDetails(movieId))

    fun onLoadStateUpdate(loadState: CombinedLoadStates, itemCount: Int) {
        val showLoading = loadState.refresh is LoadState.Loading
        val showNoData = loadState.append.endOfPaginationReached && itemCount < 1

        _uiState.update {
            it.copy(
                isLoading = showLoading,
                noDataAvailable = showNoData
            )
        }
    }
}
