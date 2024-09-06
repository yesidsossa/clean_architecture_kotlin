package com.instaleap.data.util

import com.instaleap.domain.util.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

/**
 * Created by Yesid Hernandez 02/09/2024
 **/
class DispatchersProviderImpl(
    override val io: CoroutineDispatcher,
    override val main: MainCoroutineDispatcher,
    override val default: CoroutineDispatcher
) : DispatchersProvider