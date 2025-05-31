@file:OptIn(ExperimentalMaterial3Api::class)

package uz.alimov.notesapp.presentation.screen.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import uz.alimov.notesapp.R
import uz.alimov.notesapp.domain.model.CategoryData
import uz.alimov.notesapp.domain.model.NoteData
import uz.alimov.notesapp.presentation.components.EmptyAnimation
import uz.alimov.notesapp.presentation.components.NotesActionIcon
import uz.alimov.notesapp.presentation.components.NotesAppBar
import uz.alimov.notesapp.presentation.components.NotesBottomSheet
import uz.alimov.notesapp.presentation.components.NotesFloatingActionButton
import uz.alimov.notesapp.presentation.extension.scrollToRevealItem
import uz.alimov.notesapp.presentation.extension.smartPadding
import uz.alimov.notesapp.presentation.ui.theme.NotesAppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    categoryId: Long? = null,
    onNavigateToNote: (NoteData?) -> Unit,
    onNavigateToCategory: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(categoryId) {
        viewModel.onIntent(HomeIntent.CategoryClicked(categoryId ?: 0))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToNote -> {
                    onNavigateToNote(effect.note)
                }

                is HomeEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                HomeEffect.NavigateToCategory -> {
                    onNavigateToCategory()
                }

                HomeEffect.NavigateToSettings -> {
                    onNavigateToSettings()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            NotesAppBar(
                title = R.string.app_name,
                actions = {
                    HomeAppBarMenu(
                        onCategoryClick = {
                            viewModel.onIntent(HomeIntent.CategoryActionClicked)
                        },
                        onSettingsClick = {
                            viewModel.onIntent(HomeIntent.SettingsActionClicked)
                        }
                    )
                }
            )
        },
        floatingActionButton = {
            NotesFloatingActionButton(
                icon = Icons.Filled.Add,
                contentDescription = stringResource(R.string.add_button),
                onClick = {
                    viewModel.onIntent(HomeIntent.AddNoteClicked)
                }
            )
        }
    ) { innerPadding ->
        if (state.value.isNoteListEmpty) {
            EmptyNotesMessage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                isCategoryEmpty = state.value.isCategoryListEmpty
            )
        }

        if (state.value.showOptionsSheet) {
            NoteOptions(
                onDeleteClicked = {
                    viewModel.onIntent(HomeIntent.DeleteClicked)
                },
                onEditClicked = {
                    viewModel.onIntent(HomeIntent.EditClicked)
                },
                onDismiss = {
                    viewModel.onIntent(HomeIntent.OptionsDismissed)
                }
            )
        }

        if (state.value.showDeleteSheet) {
            DeleteNoteDialog(
                onDismiss = {
                    viewModel.onIntent(HomeIntent.DeleteCanceled)
                },
                onConfirm = {
                    viewModel.onIntent(HomeIntent.DeleteConfirmed)
                },
                onCancel = {
                    viewModel.onIntent(HomeIntent.DeleteCanceled)
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                vertical = 10.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (!state.value.isCategoryListEmpty) {
                item {
                    CategoryList(
                        modifier = Modifier.fillMaxWidth(),
                        categoryList = state.value.categoryList,
                        selectedCategoryId = state.value.selectedCategoryId,
                        onCategoryClick = {
                            viewModel.onIntent(HomeIntent.CategoryClicked(it))
                        }
                    )
                }
            }
            items(state.value.noteList.size) { index ->
                NoteCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    note = state.value.noteList[index],
                    onNoteClicked = {
                        viewModel.onIntent(HomeIntent.NoteClicked(it))
                    },
                    onNoteLongClicked = {
                        viewModel.onIntent(HomeIntent.NoteLongClicked(it))
                    }
                )
            }
        }
    }
}

@Composable
fun DeleteNoteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    NotesBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        onDismiss = onDismiss
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .smartPadding(
                    horizontal = 10.dp,
                    bottom = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 5.dp)
                    .weight(1f),
                onClick = {
                    onCancel()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = stringResource(R.string.no),
                    color = Color.White
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp)
                    .weight(1f),
                onClick = {
                    onConfirm()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text(
                    text = stringResource(R.string.yes),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun NoteOptions(
    onDeleteClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    NotesBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        onDismiss = {
            onDismiss()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .smartPadding(
                    horizontal = 10.dp,
                    bottom = 10.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onDeleteClicked()
                }
            ) {
                Text(
                    text = stringResource(R.string.delete)
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onEditClicked()
                }
            ) {
                Text(
                    text = stringResource(R.string.edit)
                )
            }
        }
    }
}

@Composable
fun HomeAppBarMenu(
    onCategoryClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    NotesActionIcon(
        painter = painterResource(R.drawable.ic_category),
        contentDescription = stringResource(R.string.category_button),
        onClick = {
            onCategoryClick()
        }
    )
    NotesActionIcon(
        painter = painterResource(R.drawable.ic_settings),
        contentDescription = stringResource(R.string.settings_button),
        onClick = {
            onSettingsClick()
        }
    )
}

@Composable
fun EmptyNotesMessage(
    modifier: Modifier = Modifier,
    isCategoryEmpty: Boolean
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(
                id = if (isCategoryEmpty)
                    R.string.notes_empty
                else
                    R.string.category_empty
            ),
            fontSize = 20.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        EmptyAnimation(
            modifier = Modifier
                .size(300.dp)
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.add_notes),
            fontSize = 15.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    categoryList: List<CategoryData>,
    selectedCategoryId: Long? = null,
    onCategoryClick: (Long) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyRow(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 10.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            CategoryItem(
                category = CategoryData(name = "All"),
                selectedCategoryId = selectedCategoryId,
                onClick = {
                    onCategoryClick(it)
                    coroutineScope.launch {
                        lazyListState.scrollToRevealItem(0)
                    }
                }
            )
        }

        items(categoryList.size) { index ->
            CategoryItem(
                category = categoryList[index],
                selectedCategoryId = selectedCategoryId,
                onClick = {
                    onCategoryClick(it)
                    coroutineScope.launch {
                        lazyListState.scrollToRevealItem(index + 1)
                    }
                }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: CategoryData,
    selectedCategoryId: Long?,
    onClick: (Long) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (category.id == selectedCategoryId) Color.Gray else Color.Transparent
        ),
        shape = RoundedCornerShape(15.dp),
        onClick = {
            onClick(category.id)
        }
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp),
            text = category.name,
            color = Color.White,
            fontWeight = if (category.id == selectedCategoryId) FontWeight.Bold else FontWeight.Normal,
            fontSize = 15.sp
        )
    }
}

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: NoteData,
    backgroundColor: Color = Color.DarkGray,
    onNoteClicked: (NoteData) -> Unit,
    onNoteLongClicked: (NoteData) -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(backgroundColor)
            .combinedClickable(
                onClick = {
                    onNoteClicked(note)
                },
                onLongClick = {
                    onNoteLongClicked(note)
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 15.dp,
                    vertical = 10.dp
                )
        ) {
            if (note.title.isNotEmpty()) {
                Text(
                    text = note.title,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(
                    modifier = Modifier.height(5.dp)
                )
            }
            if (note.body.isNotEmpty()) {
                Text(
                    text = note.body,
                    fontSize = if (note.title.isNotEmpty()) 15.sp else 20.sp,
                    color = if (note.title.isNotEmpty()) Color.LightGray else Color.White,
                    maxLines = 3
                )
                Spacer(
                    modifier = Modifier.height(5.dp)
                )
            }
            NoteDateText(note.createdAt)
        }
    }
}

@Composable
fun NoteDateText(
    dateInMillis: Long
) {
    Text(
        text = formatDate(dateInMillis),
        fontSize = 13.sp,
        color = Color.Gray
    )
}

private fun formatDate(dateInMillis: Long): String {
    val date = Date(dateInMillis)
    val calendar = Calendar.getInstance()
    val noteCalendar = Calendar.getInstance().apply {
        time = date
    }
    val pattern = if (
        calendar.get(Calendar.YEAR) == noteCalendar.get(Calendar.YEAR)
    ) "MMM dd"
    else
        "MMM dd, yyyy"
    return SimpleDateFormat(
        pattern,
        Locale.getDefault()
    ).format(date)
}

@Preview
@Composable
private fun HomeScreenPreview() {
    NotesAppTheme {
        HomeScreen(
            onNavigateToNote = {},
            onNavigateToCategory = {},
            onNavigateToSettings = {}
        )
    }
}