package com.instaleap.data.repository.movie

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.instaleap.data.entities.MovieData
import com.instaleap.domain.util.Result.Error
import com.instaleap.domain.util.Result.Success

private const val STARTING_PAGE_INDEX = 1

/**
 * @author by Yesid Hernandez 02/09/2024
 */
class SearchMoviePagingSource(
    private val query: String,
    private val remote: StreamingDataSource.Remote
) : PagingSource<Int, MovieData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieData> {
        val page = params.key ?: STARTING_PAGE_INDEX

        return when (val result = remote.search(query, page, params.loadSize)) {
            is Success -> LoadResult.Page(
                data = result.data.distinctBy { movie -> movie.id },
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (result.data.isEmpty()) null else page + 1
            )

            is Error -> LoadResult.Error(result.error)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}