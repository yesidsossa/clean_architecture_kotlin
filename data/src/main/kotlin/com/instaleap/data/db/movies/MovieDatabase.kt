package com.instaleap.data.db.movies

import androidx.room.Database
import androidx.room.RoomDatabase
import com.instaleap.data.db.series.SerieDao
import com.instaleap.data.db.favoritemovies.FavoriteMovieDao
import com.instaleap.data.db.series.SeriesRemoteKeyDao
import com.instaleap.data.entities.FavoriteMovieDbData
import com.instaleap.data.entities.MovieDbData
import com.instaleap.data.entities.MovieRemoteKeyDbData
import com.instaleap.data.entities.SerieDbData
import com.instaleap.data.entities.SerieRemoteKeyDbData

/**
 * Created by Yesid Hernandez 02/09/2024
 */
@Database(
    entities = [MovieDbData::class, FavoriteMovieDbData::class, MovieRemoteKeyDbData::class, SerieRemoteKeyDbData::class, SerieDbData::class,],
    version = 2,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeyDao
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun serieDao(): SerieDao
    abstract fun serieRemoteKeysDao(): SeriesRemoteKeyDao
}