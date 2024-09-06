package com.instaleap.data.entities

import com.instaleap.domain.entities.MovieEntity
import com.google.gson.annotations.SerializedName
import com.instaleap.domain.entities.SerieEntity

/**
 * Created by Yesid Hernandez 02/09/2024
 */
data class MovieData(
    @SerializedName("id") val id: Int,
    @SerializedName("overview") val description: String?,
    @SerializedName("poster_path") val image: String?,
    @SerializedName("backdrop_path") val backgroundUrl: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("original_language") val category: String?,
)
data class SerieData(
    @SerializedName("id") val id: Int,
    @SerializedName("overview") val description: String?,
    @SerializedName("poster_path") val image: String?,
    @SerializedName("backdrop_path") val backgroundUrl: String?,
    @SerializedName("name") val title: String?,
    @SerializedName("original_language") val category: String?,
)

data class MoviesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieData>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class SeriesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<SerieData>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)


fun MovieData.toDomain() = MovieEntity(
    id = id,
    image = image ?: "/qDSKxeHoN9pvVChXXc2YZN4KwBJ.jpg",
    backgroundUrl = backgroundUrl ?: "" ,
    description = description ?: "" ,
    title = title ?: "" ,
    category = category ?: ""
)

fun MovieData.toDbData() = MovieDbData(
    id = id,
    image = image ?: "/qDSKxeHoN9pvVChXXc2YZN4KwBJ.jpg",
    description = description ?: "" ,
    title = title ?: "" ,
    category = category ?: "" ,
    backgroundUrl = backgroundUrl ?: ""
)

fun SerieData.toDomain() = SerieEntity(
    id = id,
    image = image ?: "/qDSKxeHoN9pvVChXXc2YZN4KwBJ.jpg",
    backgroundUrl = backgroundUrl ?: "" ,
    description = description ?: "" ,
    title = title ?: "" ,
    category = category ?: ""
)

fun SerieData.toDbData() = SerieDbData(
    id = id,
    image = image ?: "/qDSKxeHoN9pvVChXXc2YZN4KwBJ.jpg",
    description = description ?: "" ,
    title = title ?: "" ,
    category = category ?: "" ,
    backgroundUrl = backgroundUrl ?: ""
)