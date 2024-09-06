package com.instaleap.clean.ui.widget

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.instaleap.clean.util.preview.PreviewContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    darkMode: Boolean,
    fontFamily: FontFamily = FontFamily.SansSerif,
    fontSize: TextUnit = 28.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    onThemeUpdated: () -> Unit,
    onSearchClick: () -> Unit
) {
    // Definir colores para Light y Dark
    val backgroundColor = if (darkMode) Color.Black else Color.White
    val contentColor = if (darkMode) Color.White else Color.Black

    Column {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontSize = fontSize,
                    fontFamily = fontFamily,
                    color = contentColor,
                    fontWeight = fontWeight
                )
            },
            actions = {
                IconButton(onClick = { onSearchClick() }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = contentColor
                    )
                }
                IconButton(onClick = { onThemeUpdated() }) {
                    val icon = if (darkMode) {
                        Filled.DarkMode
                    } else {
                        Outlined.DarkMode
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = "Dark Mode",
                        tint = contentColor
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor,
                titleContentColor = contentColor,
                actionIconContentColor = contentColor
            )
        )
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TopBarPreview() {
    PreviewContainer {
        Column {
            TopBar("App Light Mode", darkMode = false, onThemeUpdated = {}, onSearchClick = {})
            Spacer(modifier = Modifier.padding(10.dp))
            TopBar("App Dark Mode", darkMode = true, onThemeUpdated = {}, onSearchClick = {})
        }
    }
}
