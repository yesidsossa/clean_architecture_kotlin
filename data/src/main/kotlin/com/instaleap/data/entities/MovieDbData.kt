package com.instaleap.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.instaleap.domain.entities.MovieEntity

/**
 * Created by Yesid Hernandez 02/09/2024
 */
@Entity(tableName = "movies")
data class MovieDbData(
    @PrimaryKey val id: Int,
    val description: String,
    val image: String,
    val backgroundUrl: String,
    val title: String,
    val category: String,
)

fun MovieDbData.toDomain() = MovieEntity(
    id = id,
    title = title,
    description = description,
    image = image,
    category = category,
    backgroundUrl = backgroundUrl
)