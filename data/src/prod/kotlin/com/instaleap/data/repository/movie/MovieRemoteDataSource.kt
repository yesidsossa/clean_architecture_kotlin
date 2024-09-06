package com.instaleap.data.repository.movie

import com.instaleap.data.BuildConfig
import com.instaleap.data.api.StreamingApi
import com.instaleap.data.entities.MovieData
import com.instaleap.data.entities.SerieData
import com.instaleap.data.util.safeApiCall
import com.instaleap.domain.util.Result

/**
 * Created by Yesid Hernandez 02/09/2024
 */
class MovieRemoteDataSource(
    private val streamingApi: StreamingApi
) : StreamingDataSource.Remote {

    override suspend fun getMovies(page: Int, limit: Int): Result<List<MovieData>> = safeApiCall {
        val response = streamingApi.getMovies(BuildConfig.THE_MOVIE_API_KEY,page, limit)
        response.results
    }

    override suspend fun getMovies(movieIds: List<Int>): Result<List<MovieData>> = safeApiCall {
        streamingApi.getMovies(movieIds)
    }

    override suspend fun getMovie(movieId: Int): Result<MovieData> = safeApiCall {
        streamingApi.getMovie(BuildConfig.THE_MOVIE_API_KEY,movieId)
    }

    override suspend fun search(query: String, page: Int, limit: Int): Result<List<MovieData>> = safeApiCall {
        val response  = streamingApi.search(BuildConfig.THE_MOVIE_API_KEY,query, page, limit)
        response.results
    }

    override suspend fun getSeries(page: Int, limit: Int): Result<List<SerieData>> = safeApiCall {
        val response = streamingApi.getSeries(BuildConfig.THE_MOVIE_API_KEY,page, limit)
        response.results
    }

    override suspend fun getSerie(serieId: Int): Result<SerieData> = safeApiCall{
        streamingApi.getSerie(BuildConfig.THE_MOVIE_API_KEY,serieId)
    }
}
