package com.instaleap.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author by Yesid Hernandez 02/09/2024
 */
@Entity(tableName = "favorite_movies")
data class FavoriteMovieDbData(
    @PrimaryKey val movieId: Int
)