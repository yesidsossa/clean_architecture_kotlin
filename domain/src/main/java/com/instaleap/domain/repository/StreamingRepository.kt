package com.instaleap.domain.repository

import androidx.paging.PagingData
import com.instaleap.domain.entities.MovieEntity
import com.instaleap.domain.entities.SerieEntity
import com.instaleap.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Created by Yesid Hernandez 02/09/2024
 */
interface StreamingRepository {
    fun movies(pageSize: Int): Flow<PagingData<MovieEntity>>
    fun series(pageSize: Int): Flow<PagingData<SerieEntity>>
    fun favoriteMovies(pageSize: Int): Flow<PagingData<MovieEntity>>
    fun search(query: String, pageSize: Int): Flow<PagingData<MovieEntity>>
    suspend fun getMovie(movieId: Int): Result<MovieEntity>
    suspend fun getSerie(serieId: Int): Result<SerieEntity>
    suspend fun checkFavoriteStatus(movieId: Int): Result<Boolean>
    suspend fun addMovieToFavorite(movieId: Int)
    suspend fun removeMovieFromFavorite(movieId: Int)
    suspend fun sync(): Boolean
}