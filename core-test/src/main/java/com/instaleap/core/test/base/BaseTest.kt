package com.instaleap.core.test.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.instaleap.core.test.rules.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

/**
 * Created by Yesid Hernandez 02/09/2024
 **/
@OptIn(ExperimentalCoroutinesApi::class)
open class BaseTest {

    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(
        testDispatcher = unconfinedTestDispatcher
    )

    fun runUnconfinedTest(block: suspend TestScope.() -> Unit) = runTest(
        context = unconfinedTestDispatcher,
        testBody = block
    )
}
