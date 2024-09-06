package com.instaleap.clean.ui.series.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.instaleap.clean.entities.SerieListItem
import com.instaleap.clean.mapper.toPresentation
import com.instaleap.domain.repository.StreamingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author by Yesid Hernandez 02/09/2024
 */
class GetSeriesWithSeparators @Inject constructor(
    private val streamingRepository: StreamingRepository,
    private val insertSerieSeparatorIntoPagingData: InsertSerieSeparatorIntoPagingData
) {

    fun series(pageSize: Int): Flow<PagingData<SerieListItem>> = streamingRepository.series(pageSize).map {
        val pagingData: PagingData<SerieListItem.Serie> = it.map { serie -> serie.toPresentation() }
        insertSerieSeparatorIntoPagingData.insert(pagingData)
    }
}
