package com.instaleap.domain.usecase

import com.instaleap.domain.repository.StreamingRepository

/**
 * @author by Yesid Hernandez 02/09/2024
 */
class RemoveMovieFromFavorite(
    private val streamingRepository: StreamingRepository
) {
    suspend operator fun invoke(movieId: Int) = streamingRepository.removeMovieFromFavorite(movieId)
}