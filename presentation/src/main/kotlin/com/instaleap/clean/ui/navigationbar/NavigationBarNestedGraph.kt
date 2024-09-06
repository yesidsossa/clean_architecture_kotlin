package com.instaleap.clean.ui.navigationbar

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.instaleap.clean.navigation.Page
import com.instaleap.clean.ui.favorites.FavoritesPage
import com.instaleap.clean.ui.favorites.FavoritesViewModel
import com.instaleap.clean.ui.movies.MovieViewModel
import com.instaleap.clean.ui.main.MainRouter
import com.instaleap.clean.ui.movies.MoviePage
import com.instaleap.clean.ui.series.SeriePage
import com.instaleap.clean.ui.series.SerieViewModel
import com.instaleap.clean.util.composableHorizontalSlide
import com.instaleap.clean.util.sharedViewModel
import kotlin.reflect.KClass

@Composable
fun NavigationBarNestedGraph(
    navController: NavHostController,
    mainNavController: NavHostController,
    parentRoute: KClass<*>?
) {
    NavHost(
        navController = navController,
        startDestination = Page.Serie,
        route = parentRoute
    ) {
        composableHorizontalSlide<Page.Serie> { backStack ->
            val viewModel = hiltViewModel<SerieViewModel>()
            SeriePage(
                mainRouter = MainRouter(mainNavController),
                viewModel = viewModel,
                sharedViewModel = backStack.sharedViewModel(navController = mainNavController)
            )
        }
        composableHorizontalSlide<Page.Movie> { backStack ->
            val viewModel = hiltViewModel<MovieViewModel>()
            MoviePage(
                mainRouter = MainRouter(mainNavController),
                viewModel = viewModel,
                sharedViewModel = backStack.sharedViewModel(navController = mainNavController)
            )
        }

        composableHorizontalSlide<Page.Favorites> {
            val viewModel = hiltViewModel<FavoritesViewModel>()
            FavoritesPage(
                mainRouter = MainRouter(mainNavController),
                viewModel = viewModel,
            )
        }
    }
}