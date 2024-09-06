package com.instaleap.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.instaleap.domain.entities.MovieEntity
import com.instaleap.domain.entities.SerieEntity

/**
 * Created by Yesid Hernandez 02/09/2024
 */
@Entity(tableName = "series")
data class SerieDbData(
    @PrimaryKey val id: Int,
    val description: String,
    val image: String,
    val backgroundUrl: String,
    val title: String,
    val category: String,
)

fun SerieDbData.toDomain() = SerieEntity(
    id = id,
    title = title,
    description = description,
    image = image,
    category = category,
    backgroundUrl = backgroundUrl
)