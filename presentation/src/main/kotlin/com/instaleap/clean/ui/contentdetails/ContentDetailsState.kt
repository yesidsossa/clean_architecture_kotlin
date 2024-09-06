package com.instaleap.clean.ui.contentdetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.instaleap.clean.navigation.Page
import javax.inject.Inject

data class ContentDetailsState(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
)


class ContentDetailsBundle @Inject constructor(
    savedStateHandle: SavedStateHandle
) {
    val contentId: Int = savedStateHandle.toRoute<Page.ContentDetails>().contentId
    val isMovie: Boolean = savedStateHandle.toRoute<Page.ContentDetails>().isMovie
}