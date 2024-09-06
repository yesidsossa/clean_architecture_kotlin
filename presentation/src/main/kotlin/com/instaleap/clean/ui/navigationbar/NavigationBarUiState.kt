package com.instaleap.clean.ui.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import com.instaleap.clean.navigation.Page

data class NavigationBarUiState(
    val bottomItems: List<BottomNavigationBarItem> = listOf(
        BottomNavigationBarItem.Serie,
        BottomNavigationBarItem.Movie,
        BottomNavigationBarItem.MyFavorites
    )
)

sealed class BottomNavigationBarItem(
    val tabName: String,
    val imageVector: ImageVector,
    val page: Page,
) {
    data object Serie : BottomNavigationBarItem("Series", imageVector = Icons.Default.Tv, Page.Serie)
    data object Movie : BottomNavigationBarItem("Movies", imageVector = Icons.Default.Movie, Page.Movie)
    data object MyFavorites : BottomNavigationBarItem("My Favorites", imageVector = Icons.Default.FavoriteBorder, Page.Favorites)
}
