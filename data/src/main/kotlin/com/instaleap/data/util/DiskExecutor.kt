package com.instaleap.data.util

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by Yesid Hernandez 02/09/2024
 */
class DiskExecutor : Executor {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    override fun execute(runnable: Runnable) {
        executor.execute(runnable)
    }
}
