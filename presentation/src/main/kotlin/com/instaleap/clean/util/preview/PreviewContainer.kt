package com.instaleap.clean.util.preview

import androidx.compose.runtime.Composable
import com.instaleap.clean.ui.theme.AppTheme

@Composable
fun PreviewContainer(
    content: @Composable () -> Unit
) {
    AppTheme {
        content()
    }
}