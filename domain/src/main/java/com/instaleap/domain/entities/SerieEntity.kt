package com.instaleap.domain.entities

/**
 * Created by Yesid Hernandez 02/09/2024
 */
data class SerieEntity(
    val id: Int,
    val title: String,
    val description: String,
    val image: String,
    val category: String,
    val backgroundUrl: String
)