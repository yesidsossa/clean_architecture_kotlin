package com.instaleap.clean.presentation.ui.contentdetails

import app.cash.turbine.test
import com.instaleap.core.test.base.BaseTest
import com.instaleap.domain.entities.MovieEntity
import com.instaleap.domain.entities.SerieEntity
import com.instaleap.domain.usecase.AddMovieToFavorite
import com.instaleap.domain.usecase.CheckFavoriteStatus
import com.instaleap.domain.usecase.GetMovieDetails
import com.instaleap.domain.usecase.GetSerieDetails
import com.instaleap.domain.usecase.RemoveMovieFromFavorite
import com.instaleap.domain.util.Result
import com.google.common.truth.Truth.assertThat
import com.instaleap.clean.ui.contentdetails.ContentDetailsBundle
import com.instaleap.clean.ui.contentdetails.ContentDetailsViewModel
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

/**
 * Created by Yesid Hernandez 02/09/2024
 **/
class ContentDetailsViewModelTest : BaseTest() {

    private val getMovieDetails: GetMovieDetails = mock()
    private val getSeriesDetails: GetSerieDetails = mock() // Mock del caso de uso para series
    private val checkFavoriteStatus: CheckFavoriteStatus = mock()
    private val addMovieToFavorite: AddMovieToFavorite = mock()
    private val removeMovieFromFavorite: RemoveMovieFromFavorite = mock()
    private val contentDetailsBundle: ContentDetailsBundle = mock()

    private lateinit var sut: ContentDetailsViewModel

    @Test
    fun `test ui state reflects movie details correctly`() = runUnconfinedTest {
        val movieId = 1413
        val movie = MovieEntity(movieId, "title", "desc", "image", "category", "backgroundUrl")

        createViewModel(
            contentId = movieId,
            isMovie = true,
            movieDetailsResult = Result.Success(movie),
            seriesDetailsResult = null,
            favoriteStatusResult = Result.Success(false)
        )

        sut.uiState.test {
            val emission = awaitItem()
            assertThat(emission.title).isEqualTo(movie.title)
            assertThat(emission.description).isEqualTo(movie.description)
            assertThat(emission.imageUrl).isEqualTo(movie.backgroundUrl)
            assertThat(emission.isFavorite).isFalse()
        }

        verify(getMovieDetails).invoke(movieId)
        verify(checkFavoriteStatus).invoke(movieId)
    }

    @Test
    fun `test ui state reflects series details correctly`() = runUnconfinedTest {
        val serieId = 2020
        val serie = SerieEntity(serieId, "title", "desc", "image", "category", "backgroundUrl")

        createViewModel(
            contentId = serieId,
            isMovie = false,
            movieDetailsResult = null,
            seriesDetailsResult = Result.Success(serie),
            favoriteStatusResult = Result.Success(false)
        )

        sut.uiState.test {
            val emission = awaitItem()
            assertThat(emission.title).isEqualTo(serie.title)
            assertThat(emission.description).isEqualTo(serie.description)
            assertThat(emission.imageUrl).isEqualTo(serie.backgroundUrl)
            assertThat(emission.isFavorite).isFalse()
        }

        verify(getSeriesDetails).invoke(serieId)
        verify(checkFavoriteStatus).invoke(serieId)
    }

    @Test
    fun `test movie marked as favorite`() = runUnconfinedTest {
        val movieId = 555
        createViewModel(
            contentId = movieId,
            isMovie = true,
            movieDetailsResult = Result.Error(mock()),
            seriesDetailsResult = null,
            favoriteStatusResult = Result.Success(false)
        )

        sut.onFavoriteClicked()

        sut.uiState.test {
            val emission = awaitItem()
            assertThat(emission.isFavorite).isTrue()
        }

        verify(addMovieToFavorite).invoke(movieId)
        verifyNoInteractions(removeMovieFromFavorite)
    }

    @Test
    fun `test series marked as favorite does not invoke addMovieToFavorite`() = runUnconfinedTest {
        val serieId = 555
        createViewModel(
            contentId = serieId,
            isMovie = false,
            movieDetailsResult = null,
            seriesDetailsResult = Result.Success(SerieEntity(serieId, "title", "desc", "image", "category", "backgroundUrl")),
            favoriteStatusResult = Result.Success(false)
        )

        sut.onFavoriteClicked()

        sut.uiState.test {
            val emission = awaitItem()
            assertThat(emission.isFavorite).isTrue()
        }

        // Verificamos que no se invoque addMovieToFavorite para una serie
        verifyNoInteractions(addMovieToFavorite)
    }


    @Test
    fun `test movie removed from favorites`() = runUnconfinedTest {
        val movieId = 555
        createViewModel(
            contentId = movieId,
            isMovie = true,
            movieDetailsResult = Result.Error(mock()),
            seriesDetailsResult = null,
            favoriteStatusResult = Result.Success(true)
        )
        sut.onFavoriteClicked()

        sut.uiState.test {
            val emission = awaitItem()
            assertThat(emission.isFavorite).isFalse()
        }

        verify(removeMovieFromFavorite).invoke(movieId)
        verifyNoInteractions(addMovieToFavorite)
    }

    private suspend fun createViewModel(
        contentId: Int,
        isMovie: Boolean,
        movieDetailsResult: Result<MovieEntity>?,
        seriesDetailsResult: Result<SerieEntity>?,
        favoriteStatusResult: Result<Boolean>
    ) {
        whenever(contentDetailsBundle.contentId).thenReturn(contentId)
        whenever(contentDetailsBundle.isMovie).thenReturn(isMovie)

        movieDetailsResult?.let { whenever(getMovieDetails(contentId)).thenReturn(it) }
        seriesDetailsResult?.let { whenever(getSeriesDetails(contentId)).thenReturn(it) }
        whenever(checkFavoriteStatus.invoke(contentId)).thenReturn(favoriteStatusResult)

        sut = ContentDetailsViewModel(
            getMovieDetails = getMovieDetails,
            getSeriesDetails = getSeriesDetails,
            checkFavoriteStatus = checkFavoriteStatus,
            removeMovieFromFavorite = removeMovieFromFavorite,
            addMovieToFavorite = addMovieToFavorite,
            contentDetailsBundle = contentDetailsBundle
        )
    }
}
