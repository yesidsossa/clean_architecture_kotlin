package com.instaleap.clean.ui.series.usecase

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import com.instaleap.clean.entities.SerieListItem
import javax.inject.Inject

/**
 * @author by Yesid Hernandez 02/09/2024
 */
class InsertSerieSeparatorIntoPagingData @Inject constructor() {
    fun insert(pagingData: PagingData<SerieListItem.Serie>): PagingData<SerieListItem> {
        return pagingData.insertSeparators { before: SerieListItem.Serie?, after: SerieListItem.Serie? ->
            when {
                isListEmpty(before, after) -> null
                isHeader(before) -> insertHeaderItem(after)
                isFooter(after) -> insertFooterItem()
                isDifferentCategory(before, after) -> insertSeparatorItem(after)
                else -> null
            }
        }
    }

    private fun isListEmpty(before: SerieListItem.Serie?, after: SerieListItem.Serie?): Boolean = before == null && after == null

    private fun isHeader(before: SerieListItem.Serie?): Boolean = before == null

    private fun isFooter(after: SerieListItem.Serie?): Boolean = after == null

    private fun isDifferentCategory(before: SerieListItem.Serie?, after: SerieListItem.Serie?): Boolean =
        before?.category != after?.category

    /**
     * Insert Header; return null to skip adding a header.
     * **/
    private fun insertHeaderItem(after: SerieListItem.Serie?): SerieListItem? = createSeparator(after)

    /**
     * Insert Footer; return null to skip adding a footer.
     * **/
    @Suppress("FunctionOnlyReturningConstant")
    private fun insertFooterItem(): SerieListItem? = null

    /**
     * Insert a separator between two items that start with different date.
     * **/
    private fun insertSeparatorItem(after: SerieListItem.Serie?): SerieListItem.Separator? = createSeparator(after)

    private fun createSeparator(item: SerieListItem.Serie?) = item?.let {
        SerieListItem.Separator(it.category)
    }
}
