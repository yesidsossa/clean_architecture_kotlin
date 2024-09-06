package com.instaleap.domain.usecase

import androidx.paging.PagingData
import com.instaleap.domain.entities.MovieEntity
import com.instaleap.domain.repository.StreamingRepository
import kotlinx.coroutines.flow.Flow

/**
 * @author by Yesid Hernandez 02/09/2024
 */
class GetFavoriteMovies(
    private val streamingRepository: StreamingRepository
) {
    operator fun invoke(pageSize: Int): Flow<PagingData<MovieEntity>> = streamingRepository.favoriteMovies(pageSize)
}