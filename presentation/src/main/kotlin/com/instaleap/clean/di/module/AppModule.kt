package com.instaleap.clean.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.instaleap.clean.di.AppSettingsSharedPreference
import com.instaleap.data.util.NetworkMonitorImpl
import com.instaleap.data.util.DiskExecutor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Yesid Hernandez 02/09/2024
 **/

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideDiskExecutor(): DiskExecutor {
        return DiskExecutor()
    }

    @Provides
    @AppSettingsSharedPreference
    fun provideAppSettingsSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitorImpl = NetworkMonitorImpl(context)
}