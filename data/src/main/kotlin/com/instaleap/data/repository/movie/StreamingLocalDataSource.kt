package com.instaleap.data.repository.movie

import androidx.paging.PagingSource
import com.instaleap.data.db.series.SerieDao
import com.instaleap.data.db.movies.MovieDao
import com.instaleap.data.db.movies.MovieRemoteKeyDao
import com.instaleap.data.db.series.SeriesRemoteKeyDao
import com.instaleap.data.entities.MovieData
import com.instaleap.data.entities.MovieDbData
import com.instaleap.data.entities.MovieRemoteKeyDbData
import com.instaleap.data.entities.SerieData
import com.instaleap.data.entities.SerieDbData
import com.instaleap.data.entities.SerieRemoteKeyDbData
import com.instaleap.data.entities.toDbData
import com.instaleap.data.entities.toDomain
import com.instaleap.data.exception.DataNotAvailableException
import com.instaleap.domain.entities.MovieEntity
import com.instaleap.domain.entities.SerieEntity
import com.instaleap.domain.util.Result

/**
 * Created by Yesid Hernandez 02/09/2024
 */
class StreamingLocalDataSource(
    private val movieDao: MovieDao,
    private val serieDao: SerieDao,
    private val remoteKeyDao: MovieRemoteKeyDao,
    private val serieRemoteKeyDao: SeriesRemoteKeyDao,
) : StreamingDataSource.Local {

    override fun movies(): PagingSource<Int, MovieDbData> = movieDao.movies()
    override fun series(): PagingSource<Int, SerieDbData> = serieDao.series()

    override suspend fun getMovies(): Result<List<MovieEntity>> {
        val movies = movieDao.getMovies()
        return if (movies.isNotEmpty()) {
            Result.Success(movies.map { it.toDomain() })
        } else {
            Result.Error(DataNotAvailableException())
        }
    }

    override suspend fun getSeries(): Result<List<SerieEntity>> {
        val series = serieDao.getSeries()
        return if (series.isNotEmpty()) {
            Result.Success(series.map { it.toDomain() })
        } else {
            Result.Error(DataNotAvailableException())
        }
    }

    override suspend fun getSerie(serieId: Int): Result<SerieEntity> {
        return serieDao.getSerie(serieId)?.let {
            Result.Success(it.toDomain())
        } ?: Result.Error(DataNotAvailableException())
    }

    override suspend fun getMovie(movieId: Int): Result<MovieEntity> {
        return movieDao.getMovie(movieId)?.let {
            Result.Success(it.toDomain())
        } ?: Result.Error(DataNotAvailableException())
    }

    override suspend fun saveMovies(movies: List<MovieData>) {
        movieDao.saveMovies(movies.map { it.toDbData() })
    }

    override suspend fun saveSeries(series: List<SerieData>) {
        serieDao.saveSeries(series.map { it.toDbData() })
    }

    override suspend fun getLastRemoteKey(): MovieRemoteKeyDbData? {
        return remoteKeyDao.getLastRemoteKey()
    }

    override suspend fun saveRemoteKey(key: MovieRemoteKeyDbData) {
        remoteKeyDao.saveRemoteKey(key)
    }

    override suspend fun getSerieLastRemoteKey(): SerieRemoteKeyDbData? {
        return serieRemoteKeyDao.getLastRemoteKey()
    }

    override suspend fun saveSerieRemoteKey(key: SerieRemoteKeyDbData) {
        serieRemoteKeyDao.saveRemoteKey(key)
    }

    override suspend fun clearMovies() {
        movieDao.clearMoviesExceptFavorites()
    }

    override suspend fun clearSeries() {
        serieDao.clearSeries()
    }

    override suspend fun clearRemoteKeys() {
        remoteKeyDao.clearRemoteKeys()
    }

    override suspend fun clearSeriesRemoteKeys() {
        serieRemoteKeyDao.clearRemoteKeys()
    }
}