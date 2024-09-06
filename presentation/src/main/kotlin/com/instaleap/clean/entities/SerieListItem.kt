package com.instaleap.clean.entities

/**
 * @author by Yesid Hernandez 02/09/2024
 */
sealed class SerieListItem {
    data class Serie(
        val id: Int,
        val imageUrl: String,
        val category: String,
    ) : SerieListItem()

    data class Separator(val category: String) : SerieListItem()
}
