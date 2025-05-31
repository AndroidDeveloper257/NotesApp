@file:OptIn(ExperimentalMaterial3Api::class)

package uz.alimov.notesapp.presentation.screen.category

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import uz.alimov.notesapp.R
import uz.alimov.notesapp.domain.model.CategoryData
import uz.alimov.notesapp.presentation.components.EmptyAnimation
import uz.alimov.notesapp.presentation.components.NotesActionIcon
import uz.alimov.notesapp.presentation.components.NotesAppBar
import uz.alimov.notesapp.presentation.components.NotesBottomSheet
import uz.alimov.notesapp.presentation.extension.smartPadding
import uz.alimov.notesapp.presentation.ui.theme.NotesAppTheme

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = koinViewModel(),
    selectedCategoryId: Long? = null,
    navigateBack: (Long?) -> Unit,
) {
    val state = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(selectedCategoryId) {
        viewModel.onIntent(CategoryIntent.SelectCategory(selectedCategoryId ?: 0))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CategoryEffect.NavigateBack -> {
                    navigateBack(effect.id)
                }

                is CategoryEffect.ShowError -> {
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
                title = R.string.category,
                navigateUp = {
                    viewModel.onIntent(CategoryIntent.NavigateUpClicked)
                },
                actions = {
                    CategoryAppBarMenu {
                        viewModel.onIntent(CategoryIntent.AddCategoryClicked)
                    }
                }
            )
        }
    ) { padding ->
        if (state.value.showBottomSheet) {
            state.value.bottomSheetType?.let { bottomSheetType ->
                when (bottomSheetType) {
                    CategoryBottomSheetType.ADD_EDIT -> {
                        AddEditCategoryDialog(
                            categoryName = state.value.addingEditingCategory.name,
                            onCategoryValueChange = {
                                viewModel.onIntent(CategoryIntent.CategoryNameChanged(it))
                            },
                            onSave = {
                                viewModel.onIntent(CategoryIntent.SaveCategoryConfirmed)
                            },
                            onDismiss = {
                                viewModel.onIntent(CategoryIntent.SaveCategoryCanceled)
                            }
                        )
                    }

                    CategoryBottomSheetType.DELETE_EDIT_OPTIONS -> {
                        CategoryOptions(
                            category = state.value.addingEditingCategory,
                            onDeleteClicked = {
                                viewModel.onIntent(CategoryIntent.DeleteCategoryClicked(it.id))
                            },
                            onEditClicked = {
                                viewModel.onIntent(CategoryIntent.EditCategoryClicked(it))
                            },
                            onDismiss = {
                                viewModel.onIntent(CategoryIntent.OptionsDismissed)
                            }
                        )
                    }

                    CategoryBottomSheetType.DELETE -> {
                        DeleteCategoryDialog(
                            onDismiss = {
                                viewModel.onIntent(CategoryIntent.DeleteCategoryCanceled)
                            },
                            onConfirm = {
                                viewModel.onIntent(CategoryIntent.DeleteCategoryConfirmed(state.value.addingEditingCategory.id))
                            },
                            onCancel = {
                                viewModel.onIntent(CategoryIntent.DeleteCategoryCanceled)
                            }
                        )
                    }
                }
            }
        }

        if (state.value.isListEmpty) {
            CategoriesEmpty(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                    vertical = 10.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    CategoryCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        category = CategoryData(name = "All"),
                        onCategoryClicked = {
                            viewModel.onIntent(CategoryIntent.CategoryClicked(it))
                        },
                        onCategoryLongClicked = {}
                    )
                }
                items(state.value.categoryList.size) { index ->
                    CategoryCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        category = state.value.categoryList[index],
                        selectedCategoryId = state.value.selectedCategoryId,
                        onCategoryClicked = {
                            viewModel.onIntent(CategoryIntent.CategoryClicked(it))
                        },
                        onCategoryLongClicked = {
                            viewModel.onIntent(CategoryIntent.CategoryLongClicked(it))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryAppBarMenu(
    onAddClick: () -> Unit
) {
    NotesActionIcon(
        imageVector = Icons.Filled.Add,
        contentDescription = stringResource(R.string.add_category),
        onClick = onAddClick
    )
}

@Composable
fun DeleteCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    NotesBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        onDismiss = {
            onDismiss()
        }
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
fun CategoriesEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.categories_empty),
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
            text = stringResource(R.string.add_categories),
            fontSize = 15.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoryOptions(
    category: CategoryData,
    onDeleteClicked: (CategoryData) -> Unit,
    onEditClicked: (CategoryData) -> Unit,
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
                    onDeleteClicked(category)
                }
            ) {
                Text(
                    text = stringResource(R.string.delete)
                )
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onEditClicked(category)
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
fun AddEditCategoryDialog(
    categoryName: String,
    onCategoryValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    NotesBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        sheetState = sheetState,
        onDismiss = {
            onDismiss()
        }
    ) {
        Column(
            modifier = Modifier
                .smartPadding(
                    horizontal = 10.dp,
                    bottom = 10.dp
                ),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = categoryName,
                onValueChange = {
                    onCategoryValueChange(it)
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onSave()
                    }
                ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.new_category)
                    )
                }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = 5.dp),
                    onClick = onDismiss
                ) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 5.dp),
                    onClick = {
                        onSave()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.save_button)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    category: CategoryData,
    backgroundColor: Color = Color.DarkGray,
    selectedCategoryId: Long? = null,
    onCategoryClicked: (Long) -> Unit,
    onCategoryLongClicked: (CategoryData) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .combinedClickable(
                onClick = {
                    onCategoryClicked(category.id)
                },
                onLongClick = {
                    if (!category.isDefault) {
                        onCategoryLongClicked(category)
                    }
                },
                onLongClickLabel = "test",
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple()
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (category.id == selectedCategoryId) {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(20.dp),
                imageVector = Icons.Filled.Done,
                contentDescription = stringResource(R.string.selected),
                tint = Color.White
            )
        }
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = category.name,
            color = Color.White
        )
    }
}

@Preview
@Composable
private fun CategoryScreenPreview() {
    NotesAppTheme {
        CategoryScreen(
            navigateBack = {}
        )
    }
}