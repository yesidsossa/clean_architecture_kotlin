package com.instaleap.clean.di.module

import android.content.Context
import androidx.room.Room
import com.instaleap.data.db.series.SerieDao
import com.instaleap.data.db.favoritemovies.FavoriteMovieDao
import com.instaleap.data.db.movies.MovieDao
import com.instaleap.data.db.movies.MovieDatabase
import com.instaleap.data.db.movies.MovieRemoteKeyDao
import com.instaleap.data.db.series.SeriesRemoteKeyDao
import com.instaleap.data.util.DiskExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Yesid Hernandez 02/09/2024
 **/
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext context: Context,
        diskExecutor: DiskExecutor
    ): MovieDatabase {
        return Room
            .databaseBuilder(context, MovieDatabase::class.java, "movie.db")
            .setQueryExecutor(diskExecutor)
            .setTransactionExecutor(diskExecutor)
            .build()
    }

    @Provides
    @Singleton
    fun provideSeriesRemoteKeyDao(movieDatabase: MovieDatabase): SeriesRemoteKeyDao {
        return movieDatabase.serieRemoteKeysDao()
    }
    @Provides
    @Singleton
    fun provideSerieDao(movieDatabase: MovieDatabase): SerieDao {
        return movieDatabase.serieDao()
    }
    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao {
        return movieDatabase.movieDao()
    }

    @Provides
    fun provideMovieRemoteKeyDao(movieDatabase: MovieDatabase): MovieRemoteKeyDao {
        return movieDatabase.movieRemoteKeysDao()
    }

    @Provides
    fun provideFavoriteMovieDao(movieDatabase: MovieDatabase): FavoriteMovieDao {
        return movieDatabase.favoriteMovieDao()
    }
}