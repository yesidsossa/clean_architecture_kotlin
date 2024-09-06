package com.instaleap.clean.ui.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.instaleap.clean.navigation.Graph
import com.instaleap.clean.navigation.Page
import com.instaleap.clean.ui.contentdetails.ContentDetailsPage
import com.instaleap.clean.ui.contentdetails.ContentDetailsViewModel
import com.instaleap.clean.ui.navigationbar.NavigationBarNestedGraph
import com.instaleap.clean.ui.navigationbar.NavigationBarScreen
import com.instaleap.clean.ui.search.SearchPage
import com.instaleap.clean.ui.search.SearchViewModel
import com.instaleap.clean.util.composableHorizontalSlide
import com.instaleap.clean.util.sharedViewModel

@Composable
fun MainGraph(
    mainNavController: NavHostController,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit
) {
    NavHost(
        navController = mainNavController,
        startDestination = Page.NavigationBar,
        route = Graph.Main::class
    ) {
        composableHorizontalSlide<Page.NavigationBar> { backStack ->
            val nestedNavController = rememberNavController()
            NavigationBarScreen(
                sharedViewModel = backStack.sharedViewModel(navController = mainNavController),
                mainRouter = MainRouter(mainNavController),
                darkMode = darkMode,
                onThemeUpdated = onThemeUpdated,
                nestedNavController = nestedNavController
            ) {
                NavigationBarNestedGraph(
                    navController = nestedNavController,
                    mainNavController = mainNavController,
                    parentRoute = Graph.Main::class
                )
            }
        }

        composableHorizontalSlide<Page.Search> {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchPage(
                mainNavController = mainNavController,
                viewModel = viewModel,
            )
        }

        composableHorizontalSlide<Page.ContentDetails> {
            val viewModel = hiltViewModel<ContentDetailsViewModel>()
            ContentDetailsPage(
                mainNavController = mainNavController,
                viewModel = viewModel,
            )
        }
    }
}