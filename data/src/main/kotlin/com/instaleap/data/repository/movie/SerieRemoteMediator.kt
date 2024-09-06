package com.instaleap.data.repository.Serie

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.instaleap.data.entities.SerieDbData
import com.instaleap.data.entities.SerieRemoteKeyDbData
import com.instaleap.data.repository.movie.StreamingDataSource
import com.instaleap.domain.util.Result

/**
 * @author by Yesid Hernandez 02/09/2024
 */

private const val SERIE_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class SerieRemoteMediator(
    private val local: StreamingDataSource.Local,
    private val remote: StreamingDataSource.Remote
) : RemoteMediator<Int, SerieDbData>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, SerieDbData>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> SERIE_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> local.getSerieLastRemoteKey()?.nextPage ?: return MediatorResult.Success(
                endOfPaginationReached = true
            )
        }

        Log.d(
            "XXX",
            "sERIERemoteMediator: load() called with: loadType = $loadType, page: $page, stateLastItem = ${state.isEmpty()}"
        )

        // There was a lag in loading the first page; as a result, it jumps to the end of the pagination.
        if (state.isEmpty() && page == 2) return MediatorResult.Success(endOfPaginationReached = false)

        when (val result = remote.getSeries(page, state.config.pageSize)) {
            is Result.Success -> {
                Log.d("XXX", "SerieRemoteMediator: get Series from remote")
                if (loadType == LoadType.REFRESH) {
                    local.clearSeries()
                    local.clearSeriesRemoteKeys()
                }

                val series = result.data

                val endOfPaginationReached = series.isEmpty()

                val prevPage = if (page == SERIE_STARTING_PAGE_INDEX) null else page - 1
                val nextPage = if (endOfPaginationReached) null else page + 1

                val key = SerieRemoteKeyDbData(prevPage = prevPage, nextPage = nextPage)

                local.saveSeries(series)
                local.saveSerieRemoteKey(key)

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }

            is Result.Error -> {
                return MediatorResult.Error(result.error)
            }
        }
    }
}