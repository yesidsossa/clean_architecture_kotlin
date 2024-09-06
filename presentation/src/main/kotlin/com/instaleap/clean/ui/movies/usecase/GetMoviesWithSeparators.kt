package com.instaleap.clean.ui.movies.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.instaleap.clean.entities.MovieListItem
import com.instaleap.clean.mapper.toPresentation
import com.instaleap.domain.repository.StreamingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author by Yesid Hernandez 02/09/2024
 */
class GetMoviesWithSeparators @Inject constructor(
    private val streamingRepository: StreamingRepository,
    private val insertSeparatorIntoPagingData: InsertSeparatorIntoPagingData
) {

    fun movies(pageSize: Int): Flow<PagingData<MovieListItem>> = streamingRepository.movies(pageSize).map {
        val pagingData: PagingData<MovieListItem.Movie> = it.map { movie -> movie.toPresentation() }
        insertSeparatorIntoPagingData.insert(pagingData)
    }
}
