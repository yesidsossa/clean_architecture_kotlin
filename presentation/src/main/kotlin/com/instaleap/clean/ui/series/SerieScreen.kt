package com.instaleap.clean.ui.series

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
import com.instaleap.clean.entities.SerieListItem
import com.instaleap.clean.navigation.Page
import com.instaleap.clean.ui.main.MainRouter
import com.instaleap.clean.ui.navigationbar.NavigationBarSharedViewModel
import com.instaleap.clean.ui.widget.LoaderFullScreen
import com.instaleap.clean.ui.widget.PullToRefresh
import com.instaleap.clean.ui.widget.SerieList
import com.instaleap.clean.util.collectAsEffect
import com.instaleap.clean.util.preview.PreviewContainer
import kotlinx.coroutines.flow.flowOf

/**
 * @author by Yesid Hernandez 02/09/2024
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SeriePage(
    mainRouter: MainRouter,
    viewModel: SerieViewModel,
    sharedViewModel: NavigationBarSharedViewModel,
) {
    val seriesPaging = viewModel.series.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()
    val pullToRefreshState = rememberPullRefreshState(uiState.showLoading, { viewModel.onRefresh() })
    val lazyGridState = rememberLazyGridState()

    viewModel.navigationState.collectAsEffect { navigationState ->
        when (navigationState) {
            is SerieNavigationState.SerieDetails -> mainRouter.navigateToSerieDetails(navigationState.serieId)
        }
    }
    viewModel.refreshListState.collectAsEffect {
        seriesPaging.refresh()
    }

    sharedViewModel.bottomItem.collectAsEffect {
        if (it.page == Page.Serie) {
            lazyGridState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(key1 = seriesPaging.loadState) {
        viewModel.onLoadStateUpdate(seriesPaging.loadState)
    }

    PullToRefresh(state = pullToRefreshState, refresh = uiState.showLoading) {
        SerieScreen(
            series = seriesPaging,
            uiState = uiState,
            lazyGridState = lazyGridState,
            onSerieClick = viewModel::onserieClicked
        )
    }
}

@Composable
private fun SerieScreen(
    series: LazyPagingItems<SerieListItem>,
    uiState: SerieUiState,
    lazyGridState: LazyGridState,
    onSerieClick: (SerieId: Int) -> Unit
) {
    Surface {
        if (uiState.showLoading) {
            LoaderFullScreen()
        } else {
            SerieList(series, onSerieClick, lazyGridState)
        }
    }
}

@Preview(device = Devices.PIXEL_3, showSystemUi = true)
@Composable
private fun SerieScreenPreview() {
    val series = flowOf(
        PagingData.from(
            listOf<SerieListItem>(
                SerieListItem.Serie(9, "", ""),
                SerieListItem.Serie(9, "", ""),
                SerieListItem.Serie(9, "", ""),
                SerieListItem.Serie(9, "", ""),
                SerieListItem.Serie(9, "", ""),
                SerieListItem.Serie(9, "", ""),
                SerieListItem.Serie(9, "", ""),
                SerieListItem.Serie(9, "", ""),
            )
        )
    ).collectAsLazyPagingItems()
    PreviewContainer {
        SerieScreen(
            series = series,
            uiState = SerieUiState(
                showLoading = false,
                errorMessage = null,
            ),
            lazyGridState = rememberLazyGridState(),
            onSerieClick = {}
        )
    }
}