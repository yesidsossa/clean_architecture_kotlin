package com.instaleap.clean.ui.movies

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.instaleap.clean.entities.MovieListItem
import com.instaleap.clean.navigation.Page
import com.instaleap.clean.ui.movies.MovieNavigationState.MovieDetails
import com.instaleap.clean.ui.main.MainRouter
import com.instaleap.clean.ui.navigationbar.NavigationBarSharedViewModel
import com.instaleap.clean.ui.widget.LoaderFullScreen
import com.instaleap.clean.ui.widget.MovieList
import com.instaleap.clean.ui.widget.PullToRefresh
import com.instaleap.clean.util.collectAsEffect
import com.instaleap.clean.util.preview.PreviewContainer
import kotlinx.coroutines.flow.flowOf

/**
 * @author by Yesid Hernandez 02/09/2024
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MoviePage(
    mainRouter: MainRouter,
    viewModel: MovieViewModel,
    sharedViewModel: NavigationBarSharedViewModel,
) {
    val moviesPaging = viewModel.movies.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullRefreshState(uiState.showLoading, { viewModel.onRefresh() })
    val lazyGridState = rememberLazyGridState()

    viewModel.navigationState.collectAsEffect { navigationState ->
        when (navigationState) {
            is MovieDetails -> mainRouter.navigateToMovieDetails(navigationState.movieId)
        }
    }
    viewModel.refreshListState.collectAsEffect {
        moviesPaging.refresh()
    }

    sharedViewModel.bottomItem.collectAsEffect {
        if (it.page == Page.Movie) {
            lazyGridState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(key1 = moviesPaging.loadState) {
        viewModel.onLoadStateUpdate(moviesPaging.loadState)
    }

    PullToRefresh(state = pullToRefreshState, refresh = uiState.showLoading) {
        MovieScreen(
            movies = moviesPaging,
            uiState = uiState,
            lazyGridState = lazyGridState,
            onMovieClick = viewModel::onMovieClicked
        )
    }
}

@Composable
private fun MovieScreen(
    movies: LazyPagingItems<MovieListItem>,
    uiState: MovieUiState,
    lazyGridState: LazyGridState,
    onMovieClick: (movieId: Int) -> Unit
) {
    Surface {
        if (uiState.showLoading) {
            LoaderFullScreen()
        } else {
            MovieList(movies, onMovieClick, lazyGridState)
        }
    }
}

@Preview(device = Devices.PIXEL_3, showSystemUi = true)
@Composable
private fun MovieScreenPreview() {
    val movies = flowOf(
        PagingData.from(
            listOf<MovieListItem>(
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
                MovieListItem.Movie(9, "", ""),
            )
        )
    ).collectAsLazyPagingItems()
    PreviewContainer {
        MovieScreen(
            movies = movies,
            uiState = MovieUiState(
                showLoading = false,
                errorMessage = null,
            ),
            lazyGridState = rememberLazyGridState(),
            onMovieClick = {}
        )
    }
}