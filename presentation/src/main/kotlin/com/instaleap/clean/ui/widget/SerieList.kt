package com.instaleap.clean.ui.widget

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.instaleap.clean.R
import com.instaleap.clean.entities.SerieListItem
import com.instaleap.clean.ui.theme.colors
import com.instaleap.clean.util.ImageSize
import com.instaleap.clean.util.preview.PreviewContainer
import com.instaleap.clean.util.toPX
import com.instaleap.data.BuildConfig
import kotlinx.coroutines.delay

@Composable
fun SerieList(
    series: LazyPagingItems<SerieListItem>,
    onSerieClick: (SerieId: Int) -> Unit,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    config: SerieSpanSizeConfig = SerieSpanSizeConfig(3),
) {
    val imageSize = ImageSize.getImageFixedSize()
    LazyVerticalGrid(
        modifier = Modifier.background(colors.background),
        columns = GridCells.Fixed(config.gridSpanSize),
        state = lazyGridState
    ) {
        items(series.itemCount, span = { index ->
            val spinSize = when (series[index]) {
                is SerieListItem.Serie -> config.SerieColumnSpanSize
                is SerieListItem.Separator -> config.separatorColumnSpanSize
                null -> config.footerColumnSpanSize
            }
            GridItemSpan(spinSize)
        }) { index ->

            val itemVisible by remember {
                derivedStateOf {
                    val visibleItems = lazyGridState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.index == index }
                }
            }

            when (val serie = series[index]) {
                is SerieListItem.Serie -> SerieItem(
                    Serie = serie,
                    imageSize = imageSize,
                    onSerieClick = onSerieClick,
                    itemVisible = itemVisible
                )

                is SerieListItem.Separator -> Separator(serie.category)
                else -> Loader()
            }
        }
    }
}

@Composable
private fun SerieItem(
    Serie: SerieListItem.Serie,
    imageSize: ImageSize,
    itemVisible: Boolean,
    onSerieClick: (SerieId: Int) -> Unit = {},
) {
    var scale by remember { mutableFloatStateOf(0.70f) }
    val animatedScale by animateFloatAsState(targetValue = scale, label = "FloatAnimation")

    LaunchedEffect(itemVisible) {
        if (itemVisible) {
            delay(100)
            scale = 1f
        } else {
            scale = 0.70f
        }
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(BuildConfig.IMAGE_URL+Serie.imageUrl)
            .scale(Scale.FILL)
            .size(imageSize.width.toPX(), imageSize.height.toPX())
            .build(),
        loading = { SerieItemPlaceholder() },
        error = { SerieItemPlaceholder() },
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(3.dp)
            .aspectRatio(9 / 16f)
            .scale(animatedScale)
            .clip(RoundedCornerShape(2))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        scale = 0.90f
                        tryAwaitRelease()
                        scale = 1f
                    },
                    onTap = {
                        onSerieClick(Serie.id)
                    }
                )
            }
    )
}

@Composable
private fun Separator(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun SerieItemPlaceholder() {
    Image(
        painter = painterResource(id = R.drawable.bg_image),
        contentDescription = "",
        contentScale = ContentScale.Crop,
    )
}

/**
 * @property gridSpanSize - The total number of columns in the grid.
 * @property separatorColumnSpanSize - Returns the number of columns that the item occupies.
 * @property footerColumnSpanSize - Returns the number of columns that the item occupies.
 **/
data class SerieSpanSizeConfig(val gridSpanSize: Int) {
    val SerieColumnSpanSize: Int = 1
    val separatorColumnSpanSize: Int = gridSpanSize
    val footerColumnSpanSize: Int = gridSpanSize
}

@Preview("Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SeparatorAndSerieItemPreview() {
    PreviewContainer {
        Surface {
            val imageSize = ImageSize.getImageFixedSize()
            Column {
                Separator("Action")
                Row {
                    SerieItem(SerieListItem.Serie(1, "https://i.stack.imgur.com/lDFzt.jpg", ""), imageSize, true)
                    SerieItem(SerieListItem.Serie(1, "https://i.stack.imgur.com/lDFzt.jpg", ""), imageSize, true)
                    SerieItem(SerieListItem.Serie(1, "https://i.stack.imgur.com/lDFzt.jpg", ""), imageSize, true)
                }
            }
        }
    }
}
