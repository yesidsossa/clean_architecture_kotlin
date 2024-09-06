package com.instaleap.data.repository.movie

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.instaleap.core.test.base.BaseTest
import com.instaleap.data.entities.MovieData
import com.instaleap.data.entities.MovieDbData
import com.instaleap.data.entities.SerieData
import com.instaleap.data.entities.toDomain
import com.instaleap.data.repository.Serie.SerieRemoteMediator
import com.instaleap.data.repository.movie.favorite.FavoriteMoviesDataSource
import com.instaleap.domain.entities.MovieEntity
import com.instaleap.domain.entities.SerieEntity
import com.instaleap.domain.util.Result
import com.instaleap.domain.util.asSuccessOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class StreamingRepositoryImplTest : BaseTest() {

    private val remote: StreamingDataSource.Remote = mock()
    private val local: StreamingDataSource.Local = mock()
    private val remoteMediator: MovieRemoteMediator = mock()
    private val serieRemoteMediator: SerieRemoteMediator = mock()
    private val localFavorite: FavoriteMoviesDataSource.Local = mock()

    private lateinit var sut: StreamingRepositoryImpl

    @Before
    fun setUp() {
        sut = StreamingRepositoryImpl(remote, local, remoteMediator, serieRemoteMediator, localFavorite)
    }

    // Movies Test Cases

    @Test
    fun `test getMovie returns movie from local if available`() = runUnconfinedTest {
        val movieEntity = MovieEntity(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        whenever(local.getMovie(any())).thenReturn(Result.Success(movieEntity))

        val result = sut.getMovie(1)

        assertTrue(result is Result.Success)
        assertEquals(movieEntity, (result as Result.Success).data)
    }

    @Test
    fun `test getMovie returns movie from remote if not available locally`() = runUnconfinedTest {
        val movieData = MovieData(
            id = 1,
            description = "Title",
            image = "Description",
            backgroundUrl = "Image",
            title = "Category",
            category = "BackgroundUrl"
        )
        whenever(local.getMovie(any())).thenReturn(Result.Error(Exception()))
        whenever(remote.getMovie(any())).thenReturn(Result.Success(movieData))

        val result = sut.getMovie(1)

        assertTrue(result is Result.Success)
        assertEquals(movieData.toDomain(), result.asSuccessOrNull())
    }

    @Test
    fun `test checkFavoriteStatus returns correct status`() = runUnconfinedTest {
        whenever(localFavorite.checkFavoriteStatus(any())).thenReturn(Result.Success(true))

        val result = sut.checkFavoriteStatus(1)

        assertTrue(result is Result.Success)
        assertEquals(true, (result as Result.Success).data)
    }

    @Test
    fun `test addMovieToFavorite adds movie to favorites, success`() = runUnconfinedTest {
        val movieEntity = MovieEntity(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        whenever(local.getMovie(any())).thenReturn(Result.Success(movieEntity))

        sut.addMovieToFavorite(1)

        verify(localFavorite).addMovieToFavorite(1)
    }

    @Test
    fun `test addMovieToFavorite adds movie to favorites, error`() = runUnconfinedTest {
        val movieData = MovieData(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        whenever(local.getMovie(any())).thenReturn(Result.Error(Exception()))
        whenever(remote.getMovie(any())).thenReturn(Result.Success(movieData))

        sut.addMovieToFavorite(1)

        verify(remote).getMovie(1)
        verify(local).saveMovies(listOf(movieData))
        verify(localFavorite).addMovieToFavorite(1)
    }

    @Test
    fun `test removeMovieFromFavorite removes movie from favorites`() = runUnconfinedTest {
        sut.removeMovieFromFavorite(1)
        verify(localFavorite).removeMovieFromFavorite(1)
    }

    @Test
    fun `test favoriteMovies returns paging source for favorite movies`() = runUnconfinedTest {
        // Simulamos una fuente de paginación (PagingSource)
        val pagingSource = mock<PagingSource<Int, MovieDbData>>()

        // Simulamos que localFavorite.favoriteMovies() devuelve una PagingSource
        whenever(localFavorite.favoriteMovies()).thenReturn(pagingSource)

        // Llamamos a la función favoriteMovies()
        val result = sut.favoriteMovies(20)

        // Verificamos que se devuelva una instancia de Flow<PagingData<MovieEntity>>
        assertTrue(result is Flow<PagingData<MovieEntity>>)
    }


    @Test
    fun `test sync updates local with remote movies`() = runUnconfinedTest {
        val movieEntity = MovieEntity(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        val movieData = MovieData(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        whenever(local.getMovies()).thenReturn(Result.Success(listOf(movieEntity)))
        whenever(remote.getMovies(any<List<Int>>())).thenReturn(Result.Success(listOf(movieData)))

        val result = sut.sync()

        assertTrue(result)
        verify(local).saveMovies(listOf(movieData))
    }

    @Test
    fun `test sync returns false when local getMovies fails`() = runUnconfinedTest {
        whenever(local.getMovies()).thenReturn(Result.Error(Exception()))
        val result = sut.sync()
        assertTrue(!result)
    }

    @Test
    fun `test sync returns false when remote getMovies fails`() = runUnconfinedTest {
        val movieEntity = MovieEntity(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        whenever(local.getMovies()).thenReturn(Result.Success(listOf(movieEntity)))
        whenever(remote.getMovies(any<List<Int>>())).thenReturn(Result.Error(Exception()))

        val result = sut.sync()

        assertTrue(!result)
    }

    // Series Test Cases

    @Test
    fun `test getSerie returns series from local if available`() = runUnconfinedTest {
        val serieEntity = SerieEntity(1, "Title", "Description", "Image", "Category", "BackgroundUrl")
        whenever(local.getSerie(any())).thenReturn(Result.Success(serieEntity))

        val result = sut.getSerie(1)

        assertTrue(result is Result.Success)
        assertEquals(serieEntity, (result as Result.Success).data)
    }

    @Test
    fun `test getSerie returns series from remote if not available locally`() = runUnconfinedTest {
        val serieData = SerieData(
            id = 1,
            description = "Title",
            image = "Description",
            backgroundUrl = "Image",
            title = "Category",
            category = "BackgroundUrl"
        )
        whenever(local.getSerie(any())).thenReturn(Result.Error(Exception()))
        whenever(remote.getSerie(any())).thenReturn(Result.Success(serieData))

        val result = sut.getSerie(1)

        assertTrue(result is Result.Success)
        assertEquals(serieData.toDomain(), result.asSuccessOrNull())
    }
}
