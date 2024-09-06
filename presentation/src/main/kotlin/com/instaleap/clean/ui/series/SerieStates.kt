package com.instaleap.clean.ui.series

data class SerieUiState(
    val showLoading: Boolean = true,
    val errorMessage: String? = null,
)

sealed class SerieNavigationState {
    data class SerieDetails(val serieId: Int) : SerieNavigationState()
}
