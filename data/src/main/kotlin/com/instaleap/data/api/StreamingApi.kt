package com.instaleap.data.api

import com.instaleap.data.entities.MovieData
import com.instaleap.data.entities.MoviesResponse
import com.instaleap.data.entities.SerieData
import com.instaleap.data.entities.SeriesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Yesid Hernandez 02/09/2024
 */
interface StreamingApi {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
        @Query("_limit") limit: Int,
    ): MoviesResponse

    @GET("tv/popular")
    suspend fun getSeries(
        @Query("api_key") api_key: String,
        @Query("page") page: Int,
        @Query("_limit") limit: Int,
    ): SeriesResponse

    @GET("tv/{tv_id}")
    suspend fun getSerie(
        @Query("api_key") api_key: String,
        @Path("tv_id") serieId: Int
    ): SerieData

    @GET("/movies")
    suspend fun getMovies(@Query("id") movieIds: List<Int>): List<MovieData>

    @GET("movie/{movie_id}")
    suspend fun getMovie(
        @Query("api_key") api_key: String,
        @Path("movie_id") movieId: Int
    ): MovieData

    @GET("search/multi")
    suspend fun search(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("_limit") limit: Int,
    ): MoviesResponse
}