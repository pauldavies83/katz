package dev.pauldavies.katz.android.screen.katzlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import dev.pauldavies.katz.android.R
import dev.pauldavies.katz.viewModel.BreedDrawerItem
import kotlinx.coroutines.launch
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
                title = state.value.title ?: stringResource(R.string.app_name),
                details = state.value.topBarDetails,
                openDrawer = openDrawer
            )
        },
        drawerElevation = 4.dp,
        drawerContent = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                title = { Text(text = stringResource(R.string.drawer_title)) },
                actions = {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                    ) {
                        IconButton(onClick = { closeDrawer() }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.announce_close_drawer)
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
                KatzGrid(it)
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
                                contentDescription = stringResource(id = R.string.announce_open_drawer)
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
                                contentDescription = stringResource(id = R.string.announce_open_breed_info)
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
                                    Text(text = stringResource(R.string.breed_details_detail), fontWeight = Bold)
                                    Text(text = it)
                                }
                            }
                            details?.originCountry?.let {
                                item {
                                    Text(text = stringResource(R.string.breed_details_origin), fontWeight = Bold)
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
