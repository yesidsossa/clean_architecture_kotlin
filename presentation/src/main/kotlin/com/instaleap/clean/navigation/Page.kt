package com.instaleap.clean.navigation

import kotlinx.serialization.Serializable

sealed class Page {
    @Serializable
    data object NavigationBar : Page()

    @Serializable
    data object Movie : Page()

    @Serializable
    data object Favorites : Page()

    @Serializable
    data object Serie : Page()

    @Serializable
    data object Search : Page()

    @Serializable
    data class ContentDetails(val contentId: Int, val isMovie: Boolean) : Page()
}

sealed class Graph {
    @Serializable
    data object Main : Graph()
}

fun Page.route(): String? {
    return this.javaClass.canonicalName
}