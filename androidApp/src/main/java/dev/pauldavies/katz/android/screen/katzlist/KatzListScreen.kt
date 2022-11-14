package dev.pauldavies.katz.android.screen.katzlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
internal fun KatzListScreen(viewModel: KatzListViewModel = getViewModel()) {
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Katz") },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null // TODO accessibility announce
                        )
                    }
                },
            )
        },
        drawerElevation = 4.dp,
        drawerContent = {
            TopAppBar(
                title = { Text(text = "Breedz") },
                actions = {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                    ) {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.close()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null // TODO accessibility announce
                            )
                        }
                    }
                }
            )
            Box(modifier = Modifier.fillMaxSize()) {
                state.value.breeds?.let {
                    BreedList(it)
                } ?: CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            state.value.kats?.let {
                KatzList(it)
            } ?: CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun BreedList(breeds: List<String>) {
    LazyColumn {
        itemsIndexed(items = breeds) { index, breed ->
            Text(
                modifier = Modifier.padding(16.dp),
                text = breed,
                style = MaterialTheme.typography.body1
            )
            if (index != breeds.lastIndex) {
                Divider(color = Color.LightGray, thickness = 1.dp, startIndent = 16.dp)
            }
        }
    }
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
                modifier = Modifier
                    .height(128.dp)
                    .fillMaxWidth(),
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
