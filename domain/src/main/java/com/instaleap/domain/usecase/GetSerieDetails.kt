package com.instaleap.domain.usecase

import com.instaleap.domain.entities.SerieEntity
import com.instaleap.domain.repository.StreamingRepository
import com.instaleap.domain.util.Result

/**
 * Created by Yesid Hernandez 02/09/2024
 **/
class GetSerieDetails(
    private val streamingRepository: StreamingRepository
) {
    suspend operator fun invoke(serieId: Int): Result<SerieEntity> = streamingRepository.getSerie(serieId)
}
