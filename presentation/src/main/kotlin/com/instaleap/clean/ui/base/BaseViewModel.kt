package com.instaleap.clean.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Yesid Hernandez 02/09/2024
 */
open class BaseViewModel : ViewModel() {
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job = viewModelScope.launch(block = block)
}
