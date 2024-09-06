package com.instaleap.clean.ui.contentdetails

import com.instaleap.clean.ui.base.BaseViewModel
import com.instaleap.clean.util.orFalse
import com.instaleap.domain.entities.MovieEntity
import com.instaleap.domain.entities.SerieEntity
import com.instaleap.domain.usecase.AddMovieToFavorite
import com.instaleap.domain.usecase.CheckFavoriteStatus
import com.instaleap.domain.usecase.GetMovieDetails
import com.instaleap.domain.usecase.GetSerieDetails
import com.instaleap.domain.usecase.RemoveMovieFromFavorite
import com.instaleap.domain.util.Result
import com.instaleap.domain.util.asSuccessOrNull
import com.instaleap.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created by Yesid Hernandez 02/09/2024
 */
@HiltViewModel
class ContentDetailsViewModel @Inject constructor(
    private val getMovieDetails: GetMovieDetails,
    private val getSeriesDetails: GetSerieDetails, // Nueva inyección de caso de uso para series
    private val checkFavoriteStatus: CheckFavoriteStatus,
    private val addMovieToFavorite: AddMovieToFavorite,
    private val removeMovieFromFavorite: RemoveMovieFromFavorite,
    contentDetailsBundle: ContentDetailsBundle,
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<ContentDetailsState> = MutableStateFlow(ContentDetailsState())
    val uiState = _uiState.asStateFlow()

    private val contentId: Int = contentDetailsBundle.contentId
    private val isMovie: Boolean = contentDetailsBundle.isMovie // Identificador para saber si es película o serie

    init {
        onInitialState()
    }

    private fun onInitialState() = launch {
        val isFavorite = async { checkFavoriteStatus(contentId).asSuccessOrNull().orFalse() }
        if (isMovie) {
            getMovieById(contentId).onSuccess {
                _uiState.value = ContentDetailsState(
                    title = it.title,
                    description = it.description,
                    imageUrl = it.backgroundUrl,
                    isFavorite = isFavorite.await()
                )
            }
        } else {
            getSeriesById(contentId).onSuccess {
                _uiState.value = ContentDetailsState(
                    title = it.title,
                    description = it.description,
                    imageUrl = it.backgroundUrl,
                    isFavorite = isFavorite.await()
                )
            }
        }
    }

    fun onFavoriteClicked() = launch {
        checkFavoriteStatus(contentId).onSuccess { isFavorite ->
            if (isMovie) {
                if (isFavorite) removeMovieFromFavorite(contentId) else addMovieToFavorite(contentId)
            } else {
            }
            _uiState.update { it.copy(isFavorite = !isFavorite) }
        }
    }

    private suspend fun getMovieById(contentId: Int): Result<MovieEntity> = getMovieDetails(contentId)
    private suspend fun getSeriesById(contentId: Int): Result<SerieEntity> = getSeriesDetails(contentId)
}
