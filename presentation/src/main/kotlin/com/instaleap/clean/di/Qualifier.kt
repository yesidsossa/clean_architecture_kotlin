@file:Suppress("MatchingDeclarationName", "Filename")

package com.instaleap.clean.di

import javax.inject.Qualifier

/**
 * @author by Yesid Hernandez 02/09/2024
 */

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppSettingsSharedPreference

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher