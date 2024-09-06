package com.instaleap.data.db.favoritemovies

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.instaleap.data.entities.FavoriteMovieDbData
import com.instaleap.data.entities.MovieDbData

/**
 * @author by Yesid Hernandez 02/09/2024
 */
@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM movies where id in (SELECT movieId FROM favorite_movies)")
    fun favoriteMovies(): PagingSource<Int, MovieDbData>

    @Query("SELECT * FROM favorite_movies")
    suspend fun getAll(): List<FavoriteMovieDbData>

    @Query("SELECT * FROM favorite_movies where movieId=:movieId")
    suspend fun get(movieId: Int): FavoriteMovieDbData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(favoriteMovieDbData: FavoriteMovieDbData)

    @Query("DELETE FROM favorite_movies WHERE movieId=:movieId")
    suspend fun remove(movieId: Int)
}
