package com.instaleap.clean.ui.series

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.instaleap.clean.entities.SerieListItem
import com.instaleap.clean.ui.base.BaseViewModel
import com.instaleap.clean.ui.series.usecase.GetSeriesWithSeparators
import com.instaleap.clean.util.singleSharedFlow
import com.instaleap.domain.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created by Yesid Hernandez 02/09/2024
 */
@HiltViewModel
class SerieViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    getseriesWithSeparators: GetSeriesWithSeparators,
) : BaseViewModel() {

    val series: Flow<PagingData<SerieListItem>> = getseriesWithSeparators.series(
        pageSize = 20
    ).cachedIn(viewModelScope)

    private val _uiState: MutableStateFlow<SerieUiState> = MutableStateFlow(SerieUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState: MutableSharedFlow<SerieNavigationState> = singleSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    private val _refreshListState: MutableSharedFlow<Unit> = singleSharedFlow()
    val refreshListState = _refreshListState.asSharedFlow()

    init {
        observeNetworkStatus()
    }

    private fun observeNetworkStatus() {
        networkMonitor.networkState
            .onEach { if (it.shouldRefresh) onRefresh() }
            .launchIn(viewModelScope)
    }

    fun onserieClicked(serieId: Int) =
        _navigationState.tryEmit(SerieNavigationState.SerieDetails(serieId))

    fun onLoadStateUpdate(loadState: CombinedLoadStates) {
        val showLoading = loadState.refresh is LoadState.Loading

        val error = when (val refresh = loadState.refresh) {
            is LoadState.Error -> refresh.error.message
            else -> null
        }

        _uiState.update { it.copy(showLoading = showLoading, errorMessage = error) }
    }

    fun onRefresh() = launch {
        _refreshListState.emit(Unit)
    }
}
