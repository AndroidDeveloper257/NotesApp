@file:OptIn(ExperimentalMaterial3Api::class)

package uz.alimov.notesapp.presentation.screen.note

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import uz.alimov.notesapp.R
import uz.alimov.notesapp.domain.model.CategoryData
import uz.alimov.notesapp.presentation.components.NotesActionIcon
import uz.alimov.notesapp.presentation.components.NotesAppBar
import uz.alimov.notesapp.presentation.components.NotesBottomSheet
import uz.alimov.notesapp.presentation.navigation.Note
import uz.alimov.notesapp.presentation.ui.theme.NotesAppTheme

@Composable
fun NoteScreen(
    viewModel: NoteViewModel = koinViewModel(),
    note: Note,
    navigateUp: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var titleFocused by remember {
        mutableStateOf(false)
    }
    var bodyFocused by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(note.categoryId) {
        Log.d("TAG", "NoteScreen: NoteIntent.CategorySelected ${note.categoryId}")
        viewModel.onIntent(NoteIntent.CategorySelected(note.categoryId ?: 0))
    }

    LaunchedEffect(titleFocused, bodyFocused) {
        viewModel.onIntent(NoteIntent.InputFocusChanged(titleFocused || bodyFocused))
    }

    LaunchedEffect(note.noteId) {
        viewModel.onIntent(NoteIntent.LoadNote(note.noteId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                NoteEffect.NavigateBack -> {
                    navigateUp()
                }

                is NoteEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            NotesAppBar(
                title = R.string.note,
                navigateUp = {
                    viewModel.onIntent(NoteIntent.NavigateBackClicked)
                },
                actions = {
                    if (state.value.inputsFocused) {
                        NotesActionIcon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(R.string.save_button),
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.onIntent(NoteIntent.DoneClicked)
                            }
                        )
                    } else {
                        NoteAppBarMenu(
                            expanded = state.value.actionsExpanded,
                            onActionsClicked = {
                                viewModel.onIntent(NoteIntent.ActionsClicked)
                            },
                            onDeleteClick = {
                                viewModel.onIntent(NoteIntent.DeleteClicked)
                            },
                            onCategoryClick = {
                                viewModel.onIntent(NoteIntent.CategoryClicked)
                            },
                            onDismiss = {
                                viewModel.onIntent(NoteIntent.ActionsDismissed)
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (state.value.showCategorySheet) {
            CategorySheet(
                categoryList = state.value.categoryList,
                selectedCategoryId = state.value.selectedCategoryId,
                onCategorySelected = {
                    viewModel.onIntent(NoteIntent.CategorySelected(it))
                },
                onDismiss = {
                    viewModel.onIntent(NoteIntent.CategorySheetDismissed)
                }
            )
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .clip(RoundedCornerShape(0.dp))
                .imePadding()
        ) {
            Column {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            titleFocused = it.isFocused
                        },
                    value = state.value.title,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 25.sp
                    ),
                    placeholder = {
                        if (!state.value.isLoading && state.value.title.isEmpty()) {
                            Text(
                                text = stringResource(R.string.title_placeholder),
                                color = Color.LightGray,
                                fontSize = 25.sp
                            )
                        }
                    },
                    onValueChange = {
                        viewModel.onIntent(NoteIntent.TitleChanged(it))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(
                                focusDirection = FocusDirection.Down
                            )
                        }
                    )
                )

                TextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(0.dp))
                        .background(Color.Transparent)
                        .onFocusChanged {
                            bodyFocused = it.isFocused
                        },
                    value = state.value.body,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp
                    ),
                    placeholder = {
                        if (!state.value.isLoading && state.value.body.isEmpty()) {
                            Text(
                                text = stringResource(R.string.note_placeholder),
                                color = Color.LightGray,
                                fontSize = 20.sp
                            )
                        }
                    },
                    onValueChange = {
                        viewModel.onIntent(NoteIntent.BodyChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
            }
        }
    }
}

@Composable
fun CategorySheet(
    categoryList: List<CategoryData>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long) -> Unit,
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
                .selectableGroup()
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.choose_category),
                fontSize = 20.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            categoryList.forEach { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = category.id == selectedCategoryId,
                            onClick = {
                                onCategorySelected(category.id)
                            },
                            role = Role.RadioButton
                        )
                        .padding(
                            vertical = 10.dp,
                            horizontal = 20.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = category.id == selectedCategoryId,
                        onClick = null
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = category.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun NoteAppBarMenu(
    expanded: Boolean,
    onActionsClicked: () -> Unit,
    onDeleteClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val actions = listOf(
        stringResource(R.string.delete),
        stringResource(R.string.category)
    )
    NotesActionIcon(
        imageVector = Icons.Filled.MoreVert,
        contentDescription = stringResource(R.string.more),
        onClick = {
            onActionsClicked()
        }
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        actions.forEachIndexed { index, action ->
            DropdownMenuItem(
                text = {
                    Text(action)
                },
                onClick = {
                    when (action) {
                        actions[0] -> onDeleteClick()
                        actions[1] -> onCategoryClick()
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun NoteScreenPreview() {
    NotesAppTheme {
        NoteScreen(
            note = Note(),
            navigateUp = {}
        )
    }
}