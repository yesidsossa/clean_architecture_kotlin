package com.instaleap.clean.ui.contentdetails

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.instaleap.clean.R
import com.instaleap.data.BuildConfig
import kotlinx.coroutines.delay

/**
 * @author by Yesid Hernandez 02/09/2024
 */

@Composable
fun ContentDetailsPage(
    mainNavController: NavHostController,
    viewModel: ContentDetailsViewModel,
) {
    val state by viewModel.uiState.collectAsState()
    ContentDetailsScreen(state, viewModel::onFavoriteClicked, mainNavController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDetailsScreen(
    state: ContentDetailsState,
    onMyListClick: () -> Unit,
    appNavController: NavHostController
) {
    var animationVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        animationVisible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        SubcomposeAsyncImage(
            model = BuildConfig.IMAGE_URL_LARGE + state.imageUrl,
            loading = { ContentItemPlaceholder() },
            error = { ContentItemPlaceholder() },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Superposición de contenido sobre la imagen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Título del contenido
            AnimatedVisibility(visible = animationVisible, enter = fadeIn()) {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Metadatos (como "TV Shows", "US", etc.)
            ContentDetailsMetadata()

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de acción
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botón My List
                OutlinedButton(
                    onClick = onMyListClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_favorite_fill_white_48), // Reemplazar con ícono de "My List"
                        contentDescription = "My List",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "My List")
                }

                // Botón Play
                Button(
                    onClick = { /* Acción de reproducir */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Play")
                }

                // Botón Info
                OutlinedButton(
                    onClick = { /* Acción de información */ },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Filled.Info, contentDescription = "Info", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Info")
                }
            }
        }
    }
}

@Composable
fun ContentDetailsMetadata() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "TV Shows", color = Color.White)
        Text(text = "US", color = Color.White)
        Text(text = "Drama", color = Color.White)
    }
}

@Composable
private fun ContentItemPlaceholder() {
    Image(
        painter = painterResource(id = R.drawable.bg_image),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(name = "ContentDetailsScreenPreview")
@Preview(name = "ContentDetailsScreenPreview (Dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ContentDetailsScreenPreview() {
    val navController = rememberNavController()

    ContentDetailsScreen(
        state = ContentDetailsState(
            title = "Stranger Things",
            description = "A group of kids confronts strange and terrifying supernatural events.",
            imageUrl = "https://i.stack.imgur.com/lDFzt.jpg",
            isFavorite = false
        ),
        onMyListClick = {},
        appNavController = navController
    )
}
