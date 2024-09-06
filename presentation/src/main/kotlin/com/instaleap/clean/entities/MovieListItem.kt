package com.instaleap.clean.entities

/**
 * @author by Yesid Hernandez 02/09/2024
 */
sealed class MovieListItem {
    data class Movie(
        val id: Int,
        val imageUrl: String,
        val category: String,
    ) : MovieListItem()

    data class Separator(val category: String) : MovieListItem()
}
