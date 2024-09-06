package com.instaleap.domain.usecase

import com.instaleap.domain.entities.MovieEntity
import com.instaleap.domain.repository.StreamingRepository
import com.instaleap.domain.util.Result

/**
 * Created by Yesid Hernandez 02/09/2024
 **/
class GetMovieDetails(
    private val streamingRepository: StreamingRepository
) {
    suspend operator fun invoke(movieId: Int): Result<MovieEntity> = streamingRepository.getMovie(movieId)
}
