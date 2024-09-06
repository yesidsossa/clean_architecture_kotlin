package com.instaleap.clean.ui.movies

data class MovieUiState(
    val showLoading: Boolean = true,
    val errorMessage: String? = null,
)

sealed class MovieNavigationState {
    data class MovieDetails(val movieId: Int) : MovieNavigationState()
}
