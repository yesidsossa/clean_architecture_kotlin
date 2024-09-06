package com.instaleap.data.repository.movie

import com.instaleap.data.api.MovieApi
import com.instaleap.data.entities.MovieData
import com.instaleap.data.entities.toDomain
import com.instaleap.data.exception.DataNotAvailableException
import com.instaleap.data.util.JsonLoader
import com.instaleap.domain.entities.MovieEntity
import com.instaleap.domain.util.Result
import kotlinx.coroutines.delay

/**
 * Created by Yesid Hernandez 02/09/2024
 */
@Suppress("UnusedPrivateMember")
class MovieRemoteDataSource(
    private val movieApi: MovieApi
) : MovieDataSource.Remote {

    companion object {
        const val DEFAULT_API_DELAY = 500L
    }

    override suspend fun getMovies(page: Int, limit: Int): Result<List<MovieData>> {
        delay(DEFAULT_API_DELAY)
        return if (page == 2) {
            Result.Success(emptyList())
        } else {
            Result.Success(JsonLoader.loadMovies())
        }
    }

    override suspend fun getMovies(movieIds: List<Int>): Result<List<MovieData>> {
        delay(DEFAULT_API_DELAY)
        val movies = JsonLoader.loadMovies().filter { it.id in movieIds }
        return Result.Success(movies)
    }

    override suspend fun getMovie(movieId: Int): Result<MovieData> {
        delay(DEFAULT_API_DELAY)
        val movie = JsonLoader.loadMovies().find { it.id == movieId }
        return if (movie != null) {
            Result.Success(movie)
        } else {
            Result.Error(DataNotAvailableException())
        }
    }

    override suspend fun search(query: String, page: Int, limit: Int): Result<List<MovieData>> {
        delay(DEFAULT_API_DELAY)
        return if (page == 2) {
            Result.Success(emptyList())
        } else {
            val filteredMovies = JsonLoader.loadMovies().filter {
                it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
            }
            Result.Success(filteredMovies)
        }
    }
}
