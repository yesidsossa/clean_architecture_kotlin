package com.instaleap.clean.mapper

import com.instaleap.clean.entities.MovieListItem
import com.instaleap.domain.entities.MovieEntity

/**
 * @author by Yesid Hernandez 02/09/2024
 */

fun MovieEntity.toPresentation() = MovieListItem.Movie(
    id = id,
    imageUrl = image,
    category = category
)

fun MovieEntity.toMovieListItem(): MovieListItem = this.toPresentation()