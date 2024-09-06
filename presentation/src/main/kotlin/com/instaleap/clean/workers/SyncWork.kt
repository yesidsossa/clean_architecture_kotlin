package com.instaleap.clean.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.instaleap.domain.util.DispatchersProvider
import com.instaleap.domain.repository.StreamingRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

const val SYNC_WORK_MAX_ATTEMPTS = 3

/**
 * @author by Yesid Hernandez 02/09/2024
 */
@HiltWorker
class SyncWork @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val streamingRepository: StreamingRepository,
    private val dispatchers: DispatchersProvider,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(dispatchers.io) {
        return@withContext if (streamingRepository.sync()) {
            Log.d("XXX", "SyncWork: doWork() called -> success")
            Result.success()
        } else {
            val lastAttempt = runAttemptCount >= SYNC_WORK_MAX_ATTEMPTS
            if (lastAttempt) {
                Log.d("XXX", "SyncWork: doWork() called -> failure")
                Result.failure()
            } else {
                Log.d("XXX", "SyncWork: doWork() called -> retry")
                Result.retry()
            }
        }
    }

    companion object {
        fun getOneTimeWorkRequest() = OneTimeWorkRequestBuilder<SyncWork>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
    }
}