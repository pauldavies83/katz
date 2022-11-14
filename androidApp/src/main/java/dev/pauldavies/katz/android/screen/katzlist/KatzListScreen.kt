package dev.pauldavies.katz.android.screen.katzlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dev.pauldavies.katz.android.R
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
internal fun KatzListScreen(viewModel: KatzListViewModel = getViewModel()) {
    val state = viewModel.state.collectAsState()
    KatzList(katz = state.value)
}

@Composable
fun KatzList(katz: List<String>, imageLoader: ImageLoader = get()) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        items(items = katz, key = { it }) { kat ->
            val painter = rememberAsyncImagePainter(
                imageLoader = imageLoader,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data = kat)
                    .build(),
            )
            Box(
                modifier = Modifier.height(128.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
                if (painter.state !is AsyncImagePainter.State.Success) {
                    Icon(
                        painter = painterResource(id = R.drawable.pokeball),
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }
    }
}
