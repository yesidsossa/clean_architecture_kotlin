package com.instaleap.clean.ui.main

import androidx.navigation.NavHostController
import com.instaleap.clean.navigation.Page

class MainRouter(
    private val mainNavController: NavHostController
) {

    fun navigateToSearch() {
        mainNavController.navigate(Page.Search)
    }

    fun navigateToMovieDetails(movieId: Int) {
        mainNavController.navigate(Page.ContentDetails(movieId,true))
    }

    fun navigateToSerieDetails(serieId: Int) {
        mainNavController.navigate(Page.ContentDetails(serieId,false))
    }
}