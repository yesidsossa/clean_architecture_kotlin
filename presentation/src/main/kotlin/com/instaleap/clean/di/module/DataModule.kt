package com.instaleap.clean.di.module

import android.content.Context
import com.instaleap.data.api.StreamingApi
import com.instaleap.data.db.series.SerieDao
import com.instaleap.data.db.favoritemovies.FavoriteMovieDao
import com.instaleap.data.db.movies.MovieDao
import com.instaleap.data.db.movies.MovieRemoteKeyDao
import com.instaleap.data.db.series.SeriesRemoteKeyDao
import com.instaleap.data.repository.Serie.SerieRemoteMediator
import com.instaleap.data.repository.movie.StreamingDataSource
import com.instaleap.data.repository.movie.StreamingLocalDataSource
import com.instaleap.data.repository.movie.MovieRemoteDataSource
import com.instaleap.data.repository.movie.MovieRemoteMediator
import com.instaleap.data.repository.movie.StreamingRepositoryImpl
import com.instaleap.data.repository.movie.favorite.FavoriteMoviesDataSource
import com.instaleap.data.repository.movie.favorite.FavoriteMoviesLocalDataSource
import com.instaleap.data.util.NetworkMonitorImpl
import com.instaleap.domain.repository.StreamingRepository
import com.instaleap.domain.usecase.AddMovieToFavorite
import com.instaleap.domain.usecase.CheckFavoriteStatus
import com.instaleap.domain.usecase.GetFavoriteMovies
import com.instaleap.domain.usecase.GetMovieDetails
import com.instaleap.domain.usecase.GetSerieDetails
import com.instaleap.domain.usecase.RemoveMovieFromFavorite
import com.instaleap.domain.usecase.SearchMovies
import com.instaleap.domain.util.NetworkMonitor
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
class DataModule {


    @Provides
    fun provideGetSerieDetails(
        streamingRepository: StreamingRepository
    ): GetSerieDetails {
        return GetSerieDetails(streamingRepository)
    }

    @Provides
    @Singleton
    fun provideSerieRemoteMediator(
        local: StreamingDataSource.Local,
        remote: StreamingDataSource.Remote
    ): SerieRemoteMediator {
        return SerieRemoteMediator(local, remote)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieRemote: StreamingDataSource.Remote,
        movieLocal: StreamingDataSource.Local,
        movieRemoteMediator: MovieRemoteMediator,
        favoriteLocal: FavoriteMoviesDataSource.Local,
        serieRemoteMediator: SerieRemoteMediator,
    ): StreamingRepository {
        return StreamingRepositoryImpl(movieRemote, movieLocal, movieRemoteMediator,serieRemoteMediator, favoriteLocal)
    }

    @Provides
    @Singleton
    fun provideMovieRemoveDataSource(streamingApi: StreamingApi): StreamingDataSource.Remote {
        return MovieRemoteDataSource(streamingApi)
    }

    @Provides
    @Singleton
    fun provideMovieLocalDataSource(
        movieDao: MovieDao,
        serieDao: SerieDao,
        movieRemoteKeyDao: MovieRemoteKeyDao,
        serieRemoteKeyDao: SeriesRemoteKeyDao,
    ): StreamingDataSource.Local {
        return StreamingLocalDataSource(movieDao,serieDao, movieRemoteKeyDao,serieRemoteKeyDao)
    }

    @Provides
    @Singleton
    fun provideMovieMediator(
        movieLocalDataSource: StreamingDataSource.Local,
        movieRemoteDataSource: StreamingDataSource.Remote
    ): MovieRemoteMediator {
        return MovieRemoteMediator(movieLocalDataSource, movieRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideFavoriteMovieLocalDataSource(
        favoriteMovieDao: FavoriteMovieDao
    ): FavoriteMoviesDataSource.Local {
        return FavoriteMoviesLocalDataSource(favoriteMovieDao)
    }

    @Provides
    fun provideSearchMoviesUseCase(streamingRepository: StreamingRepository): SearchMovies {
        return SearchMovies(streamingRepository)
    }

    @Provides
    fun provideGetMovieDetailsUseCase(streamingRepository: StreamingRepository): GetMovieDetails {
        return GetMovieDetails(streamingRepository)
    }

    @Provides
    fun provideGetFavoriteMoviesUseCase(streamingRepository: StreamingRepository): GetFavoriteMovies {
        return GetFavoriteMovies(streamingRepository)
    }

    @Provides
    fun provideCheckFavoriteStatusUseCase(streamingRepository: StreamingRepository): CheckFavoriteStatus {
        return CheckFavoriteStatus(streamingRepository)
    }

    @Provides
    fun provideAddMovieToFavoriteUseCase(streamingRepository: StreamingRepository): AddMovieToFavorite {
        return AddMovieToFavorite(streamingRepository)
    }

    @Provides
    fun provideRemoveMovieFromFavoriteUseCase(streamingRepository: StreamingRepository): RemoveMovieFromFavorite {
        return RemoveMovieFromFavorite(streamingRepository)
    }

    @Provides
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor = NetworkMonitorImpl(context)
}