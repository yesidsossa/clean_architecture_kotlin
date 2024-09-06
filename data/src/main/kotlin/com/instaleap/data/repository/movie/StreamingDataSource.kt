package com.instaleap.data.repository.movie

import androidx.paging.PagingSource
import com.instaleap.data.entities.MovieData
import com.instaleap.data.entities.MovieDbData
import com.instaleap.data.entities.MovieRemoteKeyDbData
import com.instaleap.data.entities.SerieData
import com.instaleap.data.entities.SerieDbData
import com.instaleap.data.entities.SerieRemoteKeyDbData
import com.instaleap.domain.entities.MovieEntity
import com.instaleap.domain.entities.SerieEntity
import com.instaleap.domain.util.Result

/**
 * Created by Yesid Hernandez 02/09/2024
 */
interface StreamingDataSource {

    interface Remote {
        suspend fun getMovies(page: Int, limit: Int): Result<List<MovieData>>
        suspend fun getMovies(movieIds: List<Int>): Result<List<MovieData>>
        suspend fun getMovie(movieId: Int): Result<MovieData>
        suspend fun search(query: String, page: Int, limit: Int): Result<List<MovieData>>
        suspend fun getSeries(page: Int, limit: Int): Result<List<SerieData>>
        suspend fun getSerie(serieId: Int): Result<SerieData>
    }

    interface Local {
        fun movies(): PagingSource<Int, MovieDbData>
        fun series(): PagingSource<Int, SerieDbData>
        suspend fun getMovies(): Result<List<MovieEntity>>
        suspend fun getSeries(): Result<List<SerieEntity>>
        suspend fun getSerie(serieId: Int): Result<SerieEntity>
        suspend fun getMovie(movieId: Int): Result<MovieEntity>
        suspend fun saveMovies(movies: List<MovieData>)
        suspend fun saveSeries(series: List<SerieData>)
        suspend fun getLastRemoteKey(): MovieRemoteKeyDbData?
        suspend fun saveRemoteKey(key: MovieRemoteKeyDbData)

        suspend fun getSerieLastRemoteKey(): SerieRemoteKeyDbData?
        suspend fun saveSerieRemoteKey(key: SerieRemoteKeyDbData)
        suspend fun clearMovies()
        suspend fun clearSeries()
        suspend fun clearRemoteKeys()
        suspend fun clearSeriesRemoteKeys()
    }
}