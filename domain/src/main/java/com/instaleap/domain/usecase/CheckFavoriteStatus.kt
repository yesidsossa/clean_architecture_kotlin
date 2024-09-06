package com.instaleap.domain.usecase

import com.instaleap.domain.repository.StreamingRepository
import com.instaleap.domain.util.Result

/**
 * @author by Yesid Hernandez 02/09/2024
 */
class CheckFavoriteStatus(
    private val streamingRepository: StreamingRepository
) {
    suspend operator fun invoke(movieId: Int): Result<Boolean> = streamingRepository.checkFavoriteStatus(movieId)
}