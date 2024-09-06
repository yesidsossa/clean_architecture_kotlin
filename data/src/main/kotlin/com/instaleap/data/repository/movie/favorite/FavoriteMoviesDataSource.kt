package com.instaleap.data.repository.movie.favorite

import androidx.paging.PagingSource
import com.instaleap.data.entities.MovieDbData
import com.instaleap.domain.util.Result

/**
 * @author by Yesid Hernandez 02/09/2024
 */
interface FavoriteMoviesDataSource {

    interface Local {
        fun favoriteMovies(): PagingSource<Int, MovieDbData>
        suspend fun getFavoriteMovieIds(): Result<List<Int>>
        suspend fun addMovieToFavorite(movieId: Int)
        suspend fun removeMovieFromFavorite(movieId: Int)
        suspend fun checkFavoriteStatus(movieId: Int): Result<Boolean>
    }
}
