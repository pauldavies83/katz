package dev.pauldavies.katz.android.screen.katzlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dev.pauldavies.katz.android.R
import dev.pauldavies.katz.viewModel.BreedDrawerItem
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@Composable
internal fun KatzListScreen(viewModel: KatzListViewModel = getViewModel()) {
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val openDrawer: () -> Unit = {
        coroutineScope.launch { scaffoldState.drawerState.open() }
    }
    val closeDrawer: () -> Unit = {
        coroutineScope.launch { scaffoldState.drawerState.close() }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            KatzListTopAppBar(
                title = state.value.title,
                details = state.value.topBarDetails,
                openDrawer = openDrawer
            )
        },
        drawerElevation = 4.dp,
        drawerContent = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                title = { Text(text = "Breedz") },
                actions = {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                    ) {
                        IconButton(onClick = { closeDrawer() }) {
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
                    BreedList(it, closeDrawer)
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
private fun KatzListTopAppBar(
    title: String,
    details: BreedDrawerItem.Details? = null,
    openDrawer: () -> Unit,
) {
    var expanded by mutableStateOf(false)

    CompositionLocalProvider(
        LocalContentAlpha provides ContentAlpha.high,
    ) {
        Surface(elevation = 4.dp) {
            Column(
                modifier = Modifier
                    .heightIn(min = 56.dp, max = 356.dp)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { openDrawer() }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null // TODO accessibility announce
                            )
                        }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.h6
                        )
                    }
                    AnimatedVisibility(visible = details?.description != null) {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = null // TODO accessibility announce
                            )
                        }
                    }
                }
                AnimatedVisibility(expanded) {
                    ProvideTextStyle(value = MaterialTheme.typography.body1) {
                        LazyColumn(
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            details?.description?.let {
                                item {
                                    Text(text = "Details", fontWeight = Bold)
                                    Text(text = it)
                                }
                            }
                            details?.originCountry?.let {
                                item {
                                    Text(text = "Origin Country", fontWeight = Bold)
                                    Text(text = it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BreedList(breeds: List<BreedDrawerItem>, onItemSelected: () -> Unit) {
    LazyColumn {
        itemsIndexed(items = breeds) { index, breed ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        breed.onClick()
                        onItemSelected()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = breed.name,
                    style = MaterialTheme.typography.body1
                )
                if (breed.selected) {
                    Icon(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        imageVector = Icons.Filled.Star,
                        contentDescription = null
                    )
                }
            }
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
        contentPadding = PaddingValues(vertical = 4.dp),
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
