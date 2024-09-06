package com.instaleap.data.repository.movie.favorite

import androidx.paging.PagingSource
import com.instaleap.data.db.favoritemovies.FavoriteMovieDao
import com.instaleap.data.entities.FavoriteMovieDbData
import com.instaleap.data.entities.MovieDbData
import com.instaleap.data.exception.DataNotAvailableException
import com.instaleap.domain.util.Result

/**
 * @author by Yesid Hernandez 02/09/2024
 */
class FavoriteMoviesLocalDataSource(
    private val favoriteMovieDao: FavoriteMovieDao,
) : FavoriteMoviesDataSource.Local {

    override fun favoriteMovies(): PagingSource<Int, MovieDbData> = favoriteMovieDao.favoriteMovies()

    override suspend fun addMovieToFavorite(movieId: Int) {
        favoriteMovieDao.add(FavoriteMovieDbData(movieId))
    }

    override suspend fun removeMovieFromFavorite(movieId: Int) {
        favoriteMovieDao.remove(movieId)
    }

    override suspend fun checkFavoriteStatus(movieId: Int): Result<Boolean> {
        return Result.Success(favoriteMovieDao.get(movieId) != null)
    }

    override suspend fun getFavoriteMovieIds(): Result<List<Int>> {
        val movieIds = favoriteMovieDao.getAll().map { it.movieId }
        return if (movieIds.isNotEmpty()) {
            Result.Success(movieIds)
        } else {
            Result.Error(DataNotAvailableException())
        }
    }
}
