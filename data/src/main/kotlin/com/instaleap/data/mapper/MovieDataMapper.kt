package com.instaleap.data.mapper

import com.instaleap.data.entities.MovieDbData
import com.instaleap.domain.entities.MovieEntity

/**
 * Created by Yesid Hernandez 02/09/2024
 **/

fun MovieEntity.toDbData() = MovieDbData(
    id = id,
    image = image,
    description = description,
    title = title,
    category = category,
    backgroundUrl = backgroundUrl
)
