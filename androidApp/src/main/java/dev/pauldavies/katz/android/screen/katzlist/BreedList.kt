package dev.pauldavies.katz.android.screen.katzlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.pauldavies.katz.viewModel.BreedListItem

@Composable
internal fun BreedList(breeds: List<BreedListItem>, onItemSelected: () -> Unit) {
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
