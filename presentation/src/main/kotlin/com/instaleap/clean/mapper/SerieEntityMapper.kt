package com.instaleap.clean.mapper

import com.instaleap.clean.entities.SerieListItem
import com.instaleap.domain.entities.SerieEntity

/**
 * @author by Yesid Hernandez 02/09/2024
 */

fun SerieEntity.toPresentation() = SerieListItem.Serie(
    id = id,
    imageUrl = image,
    category = category
)

fun SerieEntity.toSerieListItem(): SerieListItem = this.toPresentation()